package com.example.todaysforecast.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.example.todaysforecast.R
import com.example.todaysforecast.model.LocationData
import java.io.IOException
import java.util.*

object Utils {

    fun geoAddress(context: Context, lattitude: Double, longitude: Double): LocationData? {
        val addresses: List<Address>
        val geocoder = Geocoder(context, Locale.getDefault())
        var locationData: LocationData? = null
        try {
            addresses = geocoder.getFromLocation(lattitude, longitude, 1)
            if (addresses.isNotEmpty()) {
                locationData = LocationData(
                    addresses[0].getAddressLine(0),
                    addresses[0].locality,
                    addresses[0].adminArea,
                    addresses[0].postalCode,
                    addresses[0].countryName, lattitude.toString(), longitude.toString()
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return locationData
    }

    fun getWeatherRes(weather: String, pod: String): Int {
        return when (weather) {
            "Clouds" -> if (pod == "d") R.raw.clouds_few else R.raw.cloudynight
            "Thunderstorm" -> R.raw.rainy
            "Drizzle" -> R.raw.rainy
            "Rain" -> R.raw.rainy
            "Snow" -> R.raw.snowfall
            else -> if (pod == "d") R.raw.sunny else R.raw.clear_night
        }
    }

}

