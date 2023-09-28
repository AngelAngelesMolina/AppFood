package com.example.appfood.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.appfood.R
import com.example.appfood.activities.CategoryMealsActivity
import com.example.appfood.activities.MainActivity
import com.example.appfood.activities.MealActivity
import com.example.appfood.adapters.CategoriesAdapter
import com.example.appfood.adapters.MostPopularAdapter
import com.example.appfood.databinding.FragmentHomeBinding
import com.example.appfood.fragments.bottomsheet.MealBottomSheetFragment
import com.example.appfood.pojo.MealsByCategory
import com.example.appfood.pojo.Meal
import com.example.appfood.viewModel.HomeViewModel


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter: MostPopularAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    companion object {
        const val MEAL_ID = "com.example.appfood.fragments.idMeal"
        const val MEAL_NAME = "com.example.appfood.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.appfood.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.example.appfood.fragments.categoryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        homeViewModel = ViewModelProviders.of(HomeFragment)[HomeViewModel::class.java]
//        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        homeViewModel = (activity as MainActivity).viewModel
        popularItemsAdapter = MostPopularAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prepatePopularItemRecyclerView()
        super.onViewCreated(view, savedInstanceState)
//        homeViewModel.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()
//        GET POPULAR ITEMS
        homeViewModel.getPopularItems()
        observerPopularItems()
        //CLICKING IN THE RV
        onPopularItemClick()
        //LONG CLICK ON POPULAR ITEM
        onPopularLongItemClick()

//        CATEGORIES
        prepareCategoriesRecyclerView()
        homeViewModel.getCategories()
        observerCategories()
        onCategoryClick()
        onSearchIconClick()

    }

    private fun onSearchIconClick() {
        binding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)//navigate to the searchFragment
        }
    }

    private fun onPopularLongItemClick() {
        popularItemsAdapter.onLongItemClick = { meal ->
            val mealbottomSheetFragment = MealBottomSheetFragment.newInstance(meal.idMeal)
            //We're doing a new instance with this id,  bc we need it the fragment
            mealbottomSheetFragment.show(childFragmentManager, "Meal info")
        }
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = { category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareCategoriesRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun observerCategories() {
        homeViewModel.observerCategoriesLiveData().observe(viewLifecycleOwner) { categories ->
            //Set RV ADAPTERS
            categoriesAdapter.setCategoriesList(categories)
        }
    }

    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun prepatePopularItemRecyclerView() {
        binding.rvMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }
    }

    private fun observerPopularItems() {
        homeViewModel.observePopularItems().observe(viewLifecycleOwner) { mealList ->
            //ADAPTER
            popularItemsAdapter.setMeals(mealList as ArrayList<MealsByCategory>)
        }
    }

    private fun onRandomMealClick() {
        binding.imgRandomMeal.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observerRandomMeal() {
        homeViewModel.observeRandomMeal().observe(viewLifecycleOwner) { meal ->
            Glide.with(this@HomeFragment)
                .load(meal!!.strMealThumb)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(binding.imgRandomMeal)
            this.randomMeal = meal //meal cada que se recargue
        }
    }


}