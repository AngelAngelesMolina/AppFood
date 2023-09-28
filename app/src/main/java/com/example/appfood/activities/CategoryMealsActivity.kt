package com.example.appfood.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appfood.adapters.CategoryMealsAdapter
import com.example.appfood.databinding.ActivityCategoryMealsActivitiesBinding
import com.example.appfood.fragments.HomeFragment
import com.example.appfood.viewModel.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryMealsActivitiesBinding
    private lateinit var categoryViewModel: CategoryMealsViewModel
    private lateinit var categoryMealsAdapter: CategoryMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsActivitiesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        categoryViewModel = ViewModelProvider(this)[CategoryMealsViewModel::class.java]
        categoryViewModel.getMealsByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)
        //prepare RV
        prepareRecyclerView()
        categoryViewModel.observeMealsLiveData().observe(this) { mealList ->
            binding.tvCategoryCount.text = mealList.size.toString()
            //Set mealList
            categoryMealsAdapter.setMealList(mealList)
        }
        onItemClick()

    }

    private fun onItemClick() {
        categoryMealsAdapter.onClickItem = { currentMeal ->
            val intent = Intent(this, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, currentMeal.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, currentMeal.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, currentMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun prepareRecyclerView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = categoryMealsAdapter
        }
    }

}