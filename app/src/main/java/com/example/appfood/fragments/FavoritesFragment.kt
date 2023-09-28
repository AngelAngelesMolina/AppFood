package com.example.appfood.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.appfood.activities.MainActivity
import com.example.appfood.activities.MealActivity
import com.example.appfood.adapters.MealsAdapter
import com.example.appfood.databinding.FragmentFavoritesBinding
import com.example.appfood.viewModel.HomeViewModel
import com.google.android.material.snackbar.Snackbar


class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var mealsAdapter: MealsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRv()
        observeFavorites()
        onClickFavoriteMeal()
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, // way we usually scroll our rv
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT //the direction we need to take an action
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = true
            //When we scroll up or down

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //Delete aur favoriteMeal
                val position = viewHolder.adapterPosition // we get the position of the meal
//                val deletedMeal = viewModel.deleteMeal(favoritesMealsAdapter.differ.currentList[position])
                val deletedMeal = mealsAdapter.differ.currentList[position]
                viewModel.deleteMeal(deletedMeal)
                Snackbar.make(requireView(), "Meal deleted", Snackbar.LENGTH_SHORT)
                    .apply {
                        setAction("Undo", View.OnClickListener {
                            viewModel.insertMeal(deletedMeal)
                        }).show()
                    }
            }
        }
        //Attach itemTouchHelper to our rv
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavorites)
    }

    private fun onClickFavoriteMeal() {
        mealsAdapter.onClickItem = { favoriteMeal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, favoriteMeal.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, favoriteMeal.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, favoriteMeal.strMealThumb)
            startActivity(intent)
        }

    }

    private fun prepareRv() {
        mealsAdapter = MealsAdapter()
        binding.rvFavorites.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = mealsAdapter
        }
    }

    private fun observeFavorites() {
        viewModel.observeFavoritesMealLiveData().observe(requireActivity()) { meals ->
            mealsAdapter.differ.submitList(meals)
        }
    }
}