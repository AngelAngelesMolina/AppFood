package com.example.appfood.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.appfood.pojo.Meal

@Database(entities = [Meal::class], version = 1, exportSchema = false)
@TypeConverters(MealTypeConverter::class)
abstract class MealDataBase : RoomDatabase() {
    abstract fun mealDao(): MealDAO

    companion object {
        @Volatile
        var INSTANCE: MealDataBase? = null

        @Synchronized // only 1 thread can have an instance from this room bd
        fun getInstance(context:Context):MealDataBase{
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context,
                    MealDataBase::class.java,
                    "meal.db"
                ).fallbackToDestructiveMigration().build()
            }
            return INSTANCE as MealDataBase
        }
    }

}