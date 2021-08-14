package com.example.todaysforecast.di

import android.content.Context
import com.example.todaysforecast.BuildConfig
import com.example.todaysforecast.db.AppDatabase
import com.example.todaysforecast.db.DataManager
import com.example.todaysforecast.network.ApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesUrl() = BuildConfig.BASE_URL

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun providesApiService(url: String, gson: Gson): ApiService =
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideForecastDao(db: AppDatabase) = db.forecastDao()

    @Provides
    @Singleton
    fun provideDataManager(@ApplicationContext appContext: Context) = DataManager(appContext)

    @Provides
    fun provideAppContext(@ApplicationContext appContext: Context) = appContext
}