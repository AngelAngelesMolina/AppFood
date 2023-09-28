package com.example.appfood.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appfood.db.MealDataBase
import com.example.appfood.pojo.Category
import com.example.appfood.pojo.CategoryList
import com.example.appfood.pojo.MealsByCategoryList
import com.example.appfood.pojo.MealsByCategory
import com.example.appfood.pojo.Meal
import com.example.appfood.pojo.MealList
import com.example.appfood.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private val mealDatabase: MealDataBase
) : ViewModel() {


    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData = MutableLiveData<List<MealsByCategory>>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()
    private var favoritesMealsLiveData = mealDatabase.mealDao().getAllMeals()
    private var bottomSheelMealLiveData = MutableLiveData<Meal>()
    private var searchMealsLiveData = MutableLiveData<List<Meal>>()

    init { //get randomMeal at the begining, no matter if it's recreated
        getRandomMeal()
    }

//    private var saveStateRandomMeal: Meal? = null
    fun getRandomMeal() {
//        saveStateRandomMeal?.let { randomMeal ->
//            randomMealLiveData.postValue(randomMeal)
//            return
//        }
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val randomMeal: Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
//                    saveStateRandomMeal = randomMeal
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.i("Home Fragment FAIL", t.message.toString())
            }

        })
    }

    fun getCategories() {
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList> {
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                response.body()?.let { categoryList ->
                    categoriesLiveData.postValue(categoryList.categories)
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.i("HomeVM categories FAIL", t.message.toString())
            }

        })
    }

    fun observerCategoriesLiveData(): LiveData<List<Category>> {
        return categoriesLiveData
    }

    fun searchMeals(searchQuery: String) {
        RetrofitInstance.api.searchMeals(searchQuery).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val mealList = response.body()?.meals
                mealList?.let {
                    searchMealsLiveData.postValue(it)
                }

            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.i("Search meals FAIL", t.message.toString())
            }

        })
    }

    fun getPopularItems() {
        RetrofitInstance.api.getPopularItems("Seafood")
            .enqueue(object : Callback<MealsByCategoryList> {
                override fun onResponse(
                    call: Call<MealsByCategoryList>,
                    response: Response<MealsByCategoryList>
                ) {
                    if (response.body() != null) {
                        popularItemsLiveData.value = response.body()!!.meals
                    } else {
                        return
                    }
                }

                override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                    Log.i("Get popular items FAIL", t.message.toString())
                }

            })
    }

    fun getMealById(id: String) {
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meal = response.body()?.meals?.first() //getting first meal of this list
                meal?.let { meal ->
                    bottomSheelMealLiveData.postValue(meal)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.i("HomeVM MealId FAIL", t.message.toString())
            }

        })
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }

    fun insertMeal(meal: Meal) {
        //As we're using corroutines we use viewModelScope
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }

    fun observeRandomMeal(): LiveData<Meal> {
        return randomMealLiveData
    }

    fun observePopularItems(): LiveData<List<MealsByCategory>> {
        return popularItemsLiveData
    }

    fun observeFavoritesMealLiveData(): LiveData<List<Meal>> {
        return favoritesMealsLiveData
    }

    fun observeSearchedMealsLiveData(): LiveData<List<Meal>> {
        return searchMealsLiveData
    }

    fun observeBottomSheetMeal(): LiveData<Meal> {
        return bottomSheelMealLiveData
    }
}