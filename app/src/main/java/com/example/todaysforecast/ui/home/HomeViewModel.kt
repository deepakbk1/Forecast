package com.example.todaysforecast.ui.home

import androidx.lifecycle.ViewModel
import com.example.todaysforecast.db.ForecastDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val forecastDao: ForecastDao
) : ViewModel() {

    fun getForecastDao() = forecastDao

}