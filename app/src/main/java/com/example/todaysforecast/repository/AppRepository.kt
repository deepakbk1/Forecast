package com.example.todaysforecast.repository

import com.example.todaysforecast.network.ApiService
import com.example.todaysforecast.network.BaseDataSource
import javax.inject.Inject

class AppRepository @Inject constructor(private val apiService: ApiService) :
    BaseDataSource() {

    suspend fun getWeather(lat: Double, lon: Double,unit:String) = getResult { apiService.getWeather(lat, lon,unit) }

    suspend fun getFiveDayForecast(lat: Double, lon: Double,unit:String) = getResult { apiService.getFiveDayForecast(lat, lon,unit) }


}