package com.allandrp.weatherapplication.data

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("weather")
    suspend fun getWeatherData(
        @Query("q") location: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String

    ): ResponseWeatherData
}