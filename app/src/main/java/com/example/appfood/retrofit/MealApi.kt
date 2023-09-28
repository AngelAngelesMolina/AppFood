package com.example.appfood.retrofit

import com.example.appfood.pojo.CategoryList
import com.example.appfood.pojo.MealsByCategoryList
import com.example.appfood.pojo.MealList
import com.example.appfood.pojo.MealsByCategory
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("random.php")
    fun getRandomMeal(): Call<MealList>

//    /lookup.php?i=52772
    @GET("lookup.php")
    fun getMealDetails(@Query("i") id: String): Call<MealList>
    @GET("filter.php")
    fun getPopularItems(@Query("c") categoryName: String): Call<MealsByCategoryList>
//    www.themealdb.com/api/json/v1/1/categories.php
    @GET("categories.php")
    fun getCategories() : Call<CategoryList>
//    www.themealdb.com/api/json/v1/1/filter.php?c=Seafood
    @GET("filter.php")
    fun getMealsByCategory(@Query("c") categoryName:String):Call<MealsByCategoryList>
//    https://www.themealdb.com/api/json/v1/1/search.php?s=Arrabiata
    @GET("search.php")
    fun searchMeals(@Query("s") mealName:String) : Call<MealList>
}