package com.example.appfood.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appfood.activities.MealActivity
import com.example.appfood.db.MealDataBase
import com.example.appfood.pojo.Meal
import com.example.appfood.pojo.MealList
import com.example.appfood.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(val mealDataBase: MealDataBase) : ViewModel() {

    private var mealDetailLiveData = MutableLiveData<Meal>()

    fun insertMeal(meal: Meal) {
        //As we're using corroutines we use viewModelScope
        viewModelScope.launch {
            mealDataBase.mealDao().upsert(meal)
        }
    }


    fun getMealDetail(id: String) {
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    mealDetailLiveData.value = response.body()!!.meals[0]
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.i("MealActivity", t.message.toString())
            }

        })
    }

    fun observeMealDetailLiveData(): LiveData<Meal> {
        return mealDetailLiveData
    }


}