package com.weather.feature.settings

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.weather.model.SettingsData
import com.weather.model.TemperatureUnits
import com.weather.model.WindSpeedUnits
import org.junit.Rule
import org.junit.Test

class SettingsTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val tempUnit = TemperatureUnits.C
    private val windSpeedUnits = WindSpeedUnits.KM
    private val settingsState = SettingsUIState.Success(
        settingsData = SettingsData(windSpeedUnits, tempUnit)
    )

    //item buttons
    private val temperatureItem = hasText(text = "Temperature")
    private val windSpeedItem = hasText(text = "Wind Speed")

    @Test
    fun click_on_temperature_item_test() {
        composeTestRule.setContent {
            SettingsContent(
                settingsState = settingsState,
                tempUnit = "°C",
                windUnit = "km/h",
                onBackPressed = {  },
                setTemperature = {_-> },
                setWindSpeed = { _-> }
            )
        }
        composeTestRule.onNode(
            matcher = temperatureItem
        )
            .assertExists()
            .assertHasClickAction()
            .performClick()
        composeTestRule
            .onNode(
                hasText("°F")
                and
                hasClickAction()
            )
            .assertExists()
            .performClick()
    }

    @Test
    fun click_on_wind_speed_item_test() {
        composeTestRule.setContent {
            SettingsContent(
                settingsState = settingsState,
                tempUnit = "°C",
                windUnit = "Kilometer per hour",
                onBackPressed = {  },
                setTemperature = {_-> },
                setWindSpeed = { _-> }
            )
        }
        composeTestRule.onNode(
            matcher = windSpeedItem
        )
            .assertExists()
            .assertHasClickAction()
            .performClick()
        composeTestRule
            .onNodeWithText("km/h")
            .assertExists()
            .assertHasClickAction()
            .performClick()
    }
}