package com.allandrp.weatherapplication.data

import com.allandrp.weatherapplication.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class ApiConfig {

    companion object{
        private val loggingInterceptor = if(BuildConfig.DEBUG){
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        }else{
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

        private val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
        private val gson = GsonBuilder().setLenient().create()

        fun getWeatherApiService(): ApiService{
            val retrofit = Retrofit.Builder().apply {
                baseUrl("https://api.openweathermap.org/data/2.5/")
                addConverterFactory(GsonConverterFactory.create(gson))
                client(client)
            }.build()

            return retrofit.create(ApiService::class.java)

        }
    }
}