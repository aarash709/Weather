package com.weather.feature.settings

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.weather.core.design.components.CustomTopBar
import com.weather.core.design.components.ShowLoadingText
import com.weather.core.design.theme.WeatherTheme
import com.weather.model.SettingsData
import com.weather.model.TemperatureUnits
import com.weather.model.WindSpeedUnits

@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
) {
    val settingsUIState by viewModel.settingsUIState.collectAsStateWithLifecycle()
    var tempUnit by remember {
        mutableStateOf("")
    }
    var windUnit by remember {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = settingsUIState) {
        when (settingsUIState) {
            is SettingsUIState.Success -> {
                tempUnit =
                    when ((settingsUIState as SettingsUIState.Success).settingsData.temperatureUnits) {
                        TemperatureUnits.C -> "°C"
                        TemperatureUnits.F -> "°F"
                        else -> "null"
                    }
                windUnit =
                    when ((settingsUIState as SettingsUIState.Success).settingsData.windSpeedUnits) {
                        WindSpeedUnits.KM -> "Kilometer per hour"
                        WindSpeedUnits.MS -> "Meters per second"
                        WindSpeedUnits.MPH -> "Miles per hour"
                        else -> "null"
                    }
            }

            else -> {}
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
    ) {
        SettingsContent(
            modifier = Modifier.padding(horizontal = 0.dp),
            settingsState = settingsUIState,
            tempUnit = tempUnit,
            windUnit = windUnit,
            onBackPressed = { onBackPressed() },
            setTemperature = viewModel::setTemperatureUnit,
            setWindSpeed = viewModel::setWindSpeedUnit

        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    modifier: Modifier = Modifier,
    settingsState: SettingsUIState,
    tempUnit: String,
    windUnit: String,
    onBackPressed: () -> Unit,
    setTemperature: (TemperatureUnits) -> Unit,
    setWindSpeed: (WindSpeedUnits) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    AnimatedContent(
        targetState = settingsState,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "settings content"
    ) { state ->
        when (state) {
            is SettingsUIState.Loading -> ShowLoadingText()
            is SettingsUIState.Success -> {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                ) {
                    CustomTopBar(
                        modifier = Modifier,
                        text = "Settings",
                        scrollBehavior = scrollBehavior,
                        onBackPressed = { onBackPressed() })
                    SettingGroup(
                        modifier = Modifier.padding(16.dp),
                        groupName = "Units"
                    ) {
                        TemperatureSection(
                            title = "Temperature",
                            tempUnitName = tempUnit,
                            setTemperature = setTemperature
                        )
                        WindSpeedSection(
                            title = "Wind Speed",
                            windSpeedUnitName = windUnit,
                            setWindSpeed = setWindSpeed
                        )
                    }
                    SettingsHorizontalDivider()
                    SettingGroup(
                        modifier = Modifier.padding(16.dp),
                        groupName = "About"
                    ) {
                        About()
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsHorizontalDivider() {
    HorizontalDivider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
}

@Composable
private fun About(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxWidth() then modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "A work in progress 🚧 Weather sample app.",
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = .5f),
            fontSize = 14.sp,
        )
        Text(
            text = "Weather Data from Openweathermap.org",
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = .5f),
            fontSize = 14.sp,
        )
    }
}


@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
private fun SettingsPreview() {
    WeatherTheme {
        val temp = TemperatureUnits.F
        val wind = WindSpeedUnits.KM
        Surface(color = MaterialTheme.colorScheme.background) {
            SettingsContent(
                modifier = Modifier,
                settingsState = SettingsUIState
                    .Success(settingsData = SettingsData(wind, temp)),
                tempUnit = temp.toString(),
                windUnit = wind.toString(),
                onBackPressed = {},
                setTemperature = {},
                setWindSpeed = {}
            )
        }
    }
}