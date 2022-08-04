package com.allandrp.weatherapplication.injection

import android.content.Context
import com.allandrp.weatherapplication.data.ApiConfig
import com.allandrp.weatherapplication.repository.WeatherRepository
import com.allandrp.weatherapplication.room.DatabaseWeather

object Injection {
    fun provideRepositoryWeather(context: Context): WeatherRepository{
        val apiService = ApiConfig.getWeatherApiService()
        val database = DatabaseWeather.getInstance(context)
        val dao = database.weatherDao()
        return WeatherRepository.getInstance(apiService, dao)
    }
}