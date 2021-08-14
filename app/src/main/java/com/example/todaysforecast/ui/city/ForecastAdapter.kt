package com.example.todaysforecast.ui.city

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.todaysforecast.BuildConfig
import com.example.todaysforecast.R
import com.example.todaysforecast.databinding.WeatherItemCardBinding
import com.example.todaysforecast.model.todayforecast.WeatherResponse
import com.example.todaysforecast.utils.Utils
import com.example.todaysforecast.utils.makeTitleBold
import com.example.todaysforecast.utils.toReadableDate

class ForecastAdapter(
    private val city: String,
    private val forecastList: Array<WeatherResponse>
) :
    RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: WeatherItemCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            WeatherItemCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weatherInfo = forecastList[position]
        val context = holder.itemView.context
        holder.binding.cityName.text = city
        holder.binding.temperature.text =
            context.getString(R.string.temp, weatherInfo.main.temp).makeTitleBold()
        holder.binding.tempFeel.text = context.getString(
            R.string.temp_feel,
            weatherInfo.main.feelsLike
        ).makeTitleBold()
        holder.binding.tempMax.text =
            context.getString(R.string.temp_max, weatherInfo.main.tempMax).makeTitleBold()
        holder.binding.tempMin.text =
            context.getString(R.string.temp_min, weatherInfo.main.tempMin).makeTitleBold()
        holder.binding.humidity.text =
            context.getString(R.string.humidity, weatherInfo.main.humidity).makeTitleBold()
        holder.binding.windSpeed.text =
            context.getString(R.string.wind_speed, weatherInfo.wind.speed).makeTitleBold()
        holder.binding.windDeg.text =
            context.getString(R.string.wind_deg, weatherInfo.wind.deg).makeTitleBold()
        holder.binding.dateTime.text = weatherInfo.dt.toReadableDate()

        if (weatherInfo.weather.isNotEmpty()) {
            when (weatherInfo.weather[0].main) {
                "Rain", "Thunderstorm", "Drizzle" -> holder.binding.weatherTitle.text =
                    weatherInfo.weather[0].description.plus(
                        context.getString(
                            R.string.rain_volume,
                            weatherInfo.rain.h
                        )
                    )
                else -> holder.binding.weatherTitle.text =
                    weatherInfo.weather[0].description
            }
            holder.binding.weatherSmallIcon.load(
                context.getString(
                    R.string.url,
                    BuildConfig.IMG_URL,
                    weatherInfo.weather[0].icon
                )
            )
            holder.binding.weatherIcon.setAnimation(
                Utils.getWeatherRes(
                    weatherInfo.weather[0].main,
                    weatherInfo.sys.pod
                )
            )
        }
        holder.binding.weatherIcon.setAnimation(
            Utils.getWeatherRes(
                weatherInfo.weather[0].main,
                weatherInfo.sys.pod
            )
        )
    }

    override fun getItemCount() = forecastList.size
}