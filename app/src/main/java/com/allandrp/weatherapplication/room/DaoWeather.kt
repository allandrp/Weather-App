package com.allandrp.weatherapplication.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DaoWeather {

    @Query("SELECT * FROM weather")
    fun getWeather(): LiveData<List<Weather>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weather: Weather): Long

    @Delete
    suspend fun delete(weather: Weather)

    @Query("SELECT EXISTS(SELECT * FROM weather WHERE LOWER(location) = LOWER(:location))")
    fun isLocationExist(location: String): Boolean
}