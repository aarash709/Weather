package com.eweather.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import com.eweather.core.datastore.DataStoreKeys.WeatherDataStore.FAVORITE_CITY_COORDINATE_STRING_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.prefs.Preferences
import javax.inject.Inject

class LocalUserPreferences @Inject constructor(private val datastore: DataStore<Preferences>) {

//    fun getFavoriteCity(): Flow<String> {
//        return datastore.data.map { preferences ->
//            preferences.
//        }
//    }

}

object DataStoreKeys {
    object WeatherDataStore {
        val FAVORITE_CITY_STRING_KEY = stringPreferencesKey("favoriteCity")
        val FAVORITE_CITY_COORDINATE_STRING_KEY = stringPreferencesKey("favoriteCityCoordinate")
    }
}
