package com.example.todaysforecast.db

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataManager(context: Context) {

    private val dataStore: DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile("data_prefs")
            }
        )

    companion object {
        val UNIT = stringPreferencesKey("UNIT")
    }

    suspend fun storeData(
        unitType: String = "metric"
    ) {
        dataStore.edit {
            it[UNIT] = unitType
        }

    }

    val unitType: Flow<String> = dataStore.data.map {
        it[UNIT] ?: "metric"
    }

}