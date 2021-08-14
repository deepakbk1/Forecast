package com.example.todaysforecast.network

import com.example.todaysforecast.BuildConfig
import com.example.todaysforecast.model.fivedayforecast.FiveDaysForecastResponse
import com.example.todaysforecast.model.todayforecast.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") unit: String,
        @Query("appid") appid: String = BuildConfig.OWM_KEY
    ): Response<WeatherResponse>

    @GET("data/2.5/forecast")
    suspend fun getFiveDayForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") unit: String,
        @Query("appid") appid: String = BuildConfig.OWM_KEY
    ): Response<FiveDaysForecastResponse>
}