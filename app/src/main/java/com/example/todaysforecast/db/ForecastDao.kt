package com.example.todaysforecast.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todaysforecast.model.bookmarked.BookmarkedCities

@Dao
interface ForecastDao {


    @Query("SELECT * FROM bookmarked_cities")
    fun getAllCities(): LiveData<List<BookmarkedCities>>

    @Query("SELECT * FROM bookmarked_cities WHERE  name = :name")
    fun getCity(name: Int): LiveData<BookmarkedCities>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCities(characters: List<BookmarkedCities>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: BookmarkedCities)

    @Delete
    fun deleteCity(service: BookmarkedCities)

}
