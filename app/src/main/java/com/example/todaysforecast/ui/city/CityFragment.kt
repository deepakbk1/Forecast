package com.example.todaysforecast.ui.city

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.todaysforecast.BuildConfig
import com.example.todaysforecast.R
import com.example.todaysforecast.databinding.CityFragmentBinding
import com.example.todaysforecast.db.DataManager
import com.example.todaysforecast.utils.Resource
import com.example.todaysforecast.utils.Utils
import com.example.todaysforecast.utils.makeTitleBold
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.datetime.Clock
import javax.inject.Inject

@AndroidEntryPoint
class CityFragment : Fragment() {

    private lateinit var binding: CityFragmentBinding
    private val viewModel: CityViewModel by viewModels()
    private val args: CityFragmentArgs by navArgs()
    private var selcetedUnit: String = ""

    @Inject
    lateinit var dataManager: DataManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CityFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cityInfo = args.cityInfo
        binding.cityName.text = cityInfo.name
        dataManager.unitType.asLiveData().observe(viewLifecycleOwner) {
            viewModel.getWeather(cityInfo.latitude, cityInfo.longitude, it)
        }
        viewModel.res.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {

                    it?.data?.let { weatherInfo ->
                        dataManager.unitType.asLiveData().observe(viewLifecycleOwner) { unit ->
                            if (unit.equals("metric")) {
                                binding.temperature.text =
                                    getString(R.string.temp, weatherInfo.main.temp).makeTitleBold()
                                binding.tempFeel.text = getString(
                                    R.string.temp_feel,
                                    weatherInfo.main.feelsLike
                                ).makeTitleBold()
                                binding.tempMax.text = getString(
                                    R.string.temp_max,
                                    weatherInfo.main.tempMax
                                ).makeTitleBold()
                                binding.tempMin.text = getString(
                                    R.string.temp_min,
                                    weatherInfo.main.tempMin
                                ).makeTitleBold()
                                binding.windSpeed.text = getString(
                                    R.string.wind_speed,
                                    weatherInfo.wind.speed
                                ).makeTitleBold()
                            } else {
                                binding.temperature.text =
                                    getString(
                                        R.string.temp_imperial,
                                        weatherInfo.main.temp
                                    ).makeTitleBold()
                                binding.tempFeel.text = getString(
                                    R.string.temp_feel_imperial,
                                    weatherInfo.main.feelsLike
                                ).makeTitleBold()
                                binding.tempMax.text = getString(
                                    R.string.temp_max_imperial,
                                    weatherInfo.main.tempMax
                                ).makeTitleBold()
                                binding.tempMin.text = getString(
                                    R.string.temp_min_imperial,
                                    weatherInfo.main.tempMin
                                ).makeTitleBold()
                                binding.windSpeed.text = getString(
                                    R.string.wind_speed_imperial,
                                    weatherInfo.wind.speed
                                ).makeTitleBold()
                            }
                        }

                        binding.humidity.text =
                            getString(R.string.humidity, weatherInfo.main.humidity).makeTitleBold()

                        binding.windDeg.text =
                            getString(R.string.wind_deg, weatherInfo.wind.deg).makeTitleBold()
                        binding.dateTime.text = weatherInfo.dtTxt
                        if (weatherInfo.weather.isNotEmpty()) {
                            when (weatherInfo.weather[0].main) {
                                "Rain", "Thunderstorm", "Drizzle" -> binding.weatherTitle.text =
                                    if (selcetedUnit.equals("metric", true))
                                        getString(
                                            R.string.rain_volume,
                                            weatherInfo.weather[0].description,
                                            weatherInfo.rain.h
                                        ) else
                                        getString(
                                            R.string.rain_volume_imperial,
                                            weatherInfo.weather[0].description,
                                            weatherInfo.rain.h
                                        )
                                else -> binding.weatherTitle.text =
                                    weatherInfo.weather[0].description
                            }
                            binding.weatherSmallIcon.load(
                                getString(
                                    R.string.url,
                                    BuildConfig.IMG_URL,
                                    weatherInfo.weather[0].icon
                                )
                            )
                            val now = Clock.System.now().epochSeconds
                            val pod =
                                if (now < weatherInfo.sys.sunset && now > weatherInfo.sys.sunrise) "d" else "n"

                            binding.weatherIcon.setAnimation(
                                Utils.getWeatherRes(
                                    weatherInfo.weather[0].main,
                                    pod
                                )
                            )
                        }
                        dataManager.unitType.asLiveData().observe(viewLifecycleOwner) { unit ->
                            selcetedUnit = unit
                            viewModel.getFiveDayForecast(
                                cityInfo.latitude,
                                cityInfo.longitude,
                                unit
                            )
                        }
                    }
                }
                Resource.Status.LOADING -> {

                }
                Resource.Status.ERROR -> {

                    Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

        viewModel.fiveDayForecast.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    it?.data?.let { forecast ->
                        binding.cityForecast.layoutManager = LinearLayoutManager(context)
                        binding.cityForecast.setHasFixedSize(true)
                        binding.cityForecast.adapter =
                            ForecastAdapter(
                                selcetedUnit,
                                cityInfo.name,
                                forecast.list.toTypedArray()
                            )
                    }
                }
                Resource.Status.LOADING -> {

                }
                Resource.Status.ERROR -> {

                    Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }
}