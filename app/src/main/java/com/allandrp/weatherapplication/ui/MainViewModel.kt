package com.allandrp.weatherapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.allandrp.weatherapplication.repository.WeatherRepository
import com.allandrp.weatherapplication.room.Weather
import kotlinx.coroutines.launch

class MainViewModel(private val repository: WeatherRepository): ViewModel() {

    fun insertWeather(weather: Weather){
        viewModelScope.launch {
            repository.insertWeather(weather)
        }
    }

    fun deleteLocation(weather: Weather){
        viewModelScope.launch {
            repository.deleteLocation(weather)
        }
    }

    fun getWeatherData(location: String) = repository.getWeatherData(location)
    fun getListWeather() = repository.getWeather()
    fun isLocationExist(location: String) = repository.isLocationExist(location)
}