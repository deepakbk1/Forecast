package com.example.todaysforecast.ui.city

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todaysforecast.model.fivedayforecast.FiveDaysForecastResponse
import com.example.todaysforecast.model.todayforecast.WeatherResponse
import com.example.todaysforecast.repository.AppRepository
import com.example.todaysforecast.utils.Resource
import com.example.todaysforecast.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityViewModel @Inject constructor(
    private val appRepository: AppRepository
) : ViewModel() {
    private val _res = SingleLiveEvent<Resource<WeatherResponse>>()

    val res: LiveData<Resource<WeatherResponse>>
        get() = _res


    fun getWeather(lat: Double, lon: Double, unit: String) = viewModelScope.launch {
        _res.postValue(appRepository.getWeather(lat, lon, unit))
    }

    private val _five_day_forecast = SingleLiveEvent<Resource<FiveDaysForecastResponse>>()

    val fiveDayForecast: LiveData<Resource<FiveDaysForecastResponse>>
        get() = _five_day_forecast


    fun getFiveDayForecast(lat: Double, lon: Double, unit: String) = viewModelScope.launch {
        _five_day_forecast.postValue(appRepository.getFiveDayForecast(lat, lon, unit))
    }
}