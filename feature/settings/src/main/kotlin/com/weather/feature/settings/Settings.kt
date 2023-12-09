package com.weather.feature.settings

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
fun Settings(
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
    LaunchedEffect(key1 = settingsUIState){
        when(settingsUIState){
            is SettingsUIState.Success ->{
                tempUnit = when ((settingsUIState as SettingsUIState.Success).settingsData.temperatureUnits) {
                    TemperatureUnits.C -> "°C"
                    TemperatureUnits.F -> "°F"
                    else -> "null"
                }
                windUnit = when ((settingsUIState as SettingsUIState.Success).settingsData.windSpeedUnits) {
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
            modifier = Modifier.padding(horizontal = 16.dp),
            settingsState = settingsUIState,
            tempUnit = tempUnit,
            windUnit = windUnit,
            onBackPressed = { onBackPressed() },
            setTemperature = viewModel::setTemperatureUnit,
            setWindSpeed = viewModel::setWindSpeedUnit

        )
    }
}

@Composable
internal fun SettingsContent(
    modifier: Modifier = Modifier,
    settingsState: SettingsUIState,
    tempUnit: String,
    windUnit: String,
    onBackPressed: () -> Unit,
    setTemperature: (TemperatureUnits) -> Unit,
    setWindSpeed: (WindSpeedUnits) -> Unit,
) {
    when (settingsState) {
        is SettingsUIState.Loading -> ShowLoadingText()
        is SettingsUIState.Success -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
            ) {
                CustomTopBar(modifier = Modifier.fillMaxWidth(), text = "Settings") {
                    onBackPressed()
                }
                SettingGroup(
                    modifier = Modifier,
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
                SettingGroup(groupName = "About") {
                    About()
                }
            }
        }
    }
}

@Composable
private fun About() {
    Column {
        Text(
            text = "A work in progress 🚧 Weather sample app.",
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = .5f),
            fontSize = 12.sp,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Weather Data from Openweathermap.org",
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = .5f),
            fontSize = 12.sp,
        )
    }
}


@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
private fun SettingsPreview() {
    WeatherTheme() {
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

@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
private fun MenuPreview() {
    WeatherTheme() {
        var expanded by remember {
            mutableStateOf(true)
        }
        Text("empty preview")
    }
}
