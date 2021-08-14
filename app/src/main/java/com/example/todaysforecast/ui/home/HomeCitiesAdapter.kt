package com.example.todaysforecast.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todaysforecast.databinding.CitiesItemCardBinding
import com.example.todaysforecast.db.ForecastDao
import com.example.todaysforecast.model.bookmarked.BookmarkedCities

class HomeCitiesAdapter(
    private val myCitiesList: Array<BookmarkedCities>,
    val clickListener: (isDelete:Boolean,BookmarkedCities) -> Unit
) :
    RecyclerView.Adapter<HomeCitiesAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CitiesItemCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CitiesItemCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = myCitiesList[position]
        holder.binding.cityName.text = city.name
        holder.binding.root.setOnClickListener {
            clickListener(false, myCitiesList[position])
        }
        holder.binding.deleteLocation.setOnClickListener {
            clickListener(true, myCitiesList[position])
        }
    }

    override fun getItemCount() = myCitiesList.size
}