package com.example.todaysforecast.model.fivedayforecast


import com.example.todaysforecast.model.todayforecast.WeatherResponse
import com.google.gson.annotations.SerializedName

data class FiveDaysForecastResponse(
    @SerializedName("city")
    val city: City,
    @SerializedName("cnt")
    val cnt: Int,
    @SerializedName("cod")
    val cod: String,
    @SerializedName("list")
    val list: List<WeatherResponse>,
    @SerializedName("message")
    val message: Int
)