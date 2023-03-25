package com.weather.core.repository

import com.eweather.core.datastore.LocalUserPreferences
import com.weather.model.Coordinate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userPreferences: LocalUserPreferences,
) : UserRepository {
    override fun getFavoriteCityCoordinate(): Flow<Coordinate?> {
        return userPreferences.getFavoriteCity().map { stringCoordinate ->
            if (stringCoordinate != null) {
                Json.decodeFromString<Coordinate>(stringCoordinate)
            } else {
                null
            }
        }
    }
}