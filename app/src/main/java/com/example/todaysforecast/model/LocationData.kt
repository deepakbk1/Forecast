package com.example.todaysforecast.model

data class LocationData(
    val address: String,
    val city: String,
    val state: String,
    val pincode: String,
    val country: String,
    val latitude: String,
    val longitude: String
)