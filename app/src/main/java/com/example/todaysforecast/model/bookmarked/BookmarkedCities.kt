package com.example.todaysforecast.model.bookmarked

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "bookmarked_cities")
@Parcelize
data class BookmarkedCities(
    @PrimaryKey val name: String,
    val latitude: Double,
    val longitude: Double
) : Parcelable