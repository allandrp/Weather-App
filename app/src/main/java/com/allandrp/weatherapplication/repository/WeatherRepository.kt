package com.allandrp.weatherapplication.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.allandrp.weatherapplication.BuildConfig
import com.allandrp.weatherapplication.data.ApiService
import com.allandrp.weatherapplication.data.ResponseWeatherData
import com.allandrp.weatherapplication.room.DaoWeather
import com.allandrp.weatherapplication.room.Weather

class WeatherRepository(
    private val weatherApiService: ApiService,
    private val dao: DaoWeather
) {

    fun getWeatherData(location: String): LiveData<Result<ResponseWeatherData>> = liveData {
        emit(Result.Loading)

        try {
            val response = weatherApiService.getWeatherData(
                location = location,
                units = "metric",
                apiKey = BuildConfig.APIKEY
            )

            when (response.cod) {
                200 -> {
                    emit(Result.Success(response))
                }
                404 -> {
                    emit(Result.Error("Location Not Found"))
                }
                else -> {
                    emit(Result.Error("Error Occurred"))
                }
            }

        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun insertWeather(weather: Weather): Long {
        return dao.insert(weather)
    }

    suspend fun deleteLocation(weather: Weather){
        return dao.delete(weather)
    }

    fun getWeather(): LiveData<List<Weather>> = dao.getWeather()

    fun isLocationExist(location: String): Boolean = dao.isLocationExist(location)



    companion object {
        @Volatile
        private var instance: WeatherRepository? = null

        fun getInstance(apiService: ApiService, dao: DaoWeather): WeatherRepository {
            return instance ?: synchronized(this) {
                instance ?: WeatherRepository(apiService, dao)
            }.also {
                instance = it
            }
        }
    }
}