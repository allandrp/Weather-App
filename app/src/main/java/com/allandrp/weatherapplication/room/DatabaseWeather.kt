package com.allandrp.weatherapplication.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Weather::class], version = 1)
abstract class DatabaseWeather: RoomDatabase(){

    abstract fun weatherDao(): DaoWeather

    companion object{

        @Volatile
        private var INSTANCE: DatabaseWeather? = null

        fun getInstance(context: Context): DatabaseWeather{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseWeather::class.java,
                    "weather.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}