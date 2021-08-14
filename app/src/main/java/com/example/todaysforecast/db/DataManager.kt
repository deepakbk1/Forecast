package com.example.todaysforecast.db

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
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
        val IS_FIRST = booleanPreferencesKey("IS_FIRST")
        val UNIT = stringPreferencesKey("UNIT")
    }

    suspend fun storeData(
        isFirst: Boolean = false,
        unitType: String = "metric"
    ) {
        dataStore.edit {
            it[IS_FIRST] = isFirst
            it[UNIT] = unitType
        }

    }

    val isFirst: Flow<Boolean> = dataStore.data.map {
        it[IS_FIRST] ?: false
    }
    val unitType: Flow<String> = dataStore.data.map {
        it[UNIT] ?: "metric"
    }


    suspend fun clearData() {
        dataStore.edit {
            it.clear()
        }
    }

}