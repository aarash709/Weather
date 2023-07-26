package com.weather.feature.settings

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
    ) {
        SettingsContent(
            modifier = Modifier.padding(horizontal = 16.dp),
            settingsState = settingsUIState,
            onBackPressed = { onBackPressed() },
            setTemperature = viewModel::setTemperatureUnit,
            setWindSpeed = viewModel::setWindSpeedUnit

        )
    }
}

@Composable
internal fun SettingsContent(
    modifier : Modifier = Modifier,
    settingsState: SettingsUIState,
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
                val tempUnit = when (settingsState.settingsData.temperatureUnits) {
                    TemperatureUnits.C -> "°C"
                    TemperatureUnits.F -> "°F"
                    null -> "null"
                }
                val windUnit = when (settingsState.settingsData.windSpeedUnits) {
                    null -> "null"
                    WindSpeedUnits.KM -> "Kilometer per hour"
                    WindSpeedUnits.MS -> "Meters per second"
                    WindSpeedUnits.MPH -> "Miles per hour"
                }
                CustomTopBar(modifier = Modifier.fillMaxWidth(), text = "Settings") {
                    onBackPressed()
                }
                Column(modifier = Modifier) {
                    Text(
                        text = "Units",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = .5f),
                        fontSize = 12.sp
                    )
                    Surface(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.background,
                        shadowElevation = 0.dp
                    ) {
                        var expanded by remember {
                            mutableStateOf(false)
                        }
                        SettingItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expanded = true }
                                .padding(vertical = 8.dp),
                        ) {
                            Text("Temperature Unit", color = MaterialTheme.colorScheme.onBackground)
                            TemperatureMenu(
                                tempUnit,
                                expanded,
                                setTemperature,
                                setExpanded = { expanded = it })
                        }
                    }
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.background,
                        shadowElevation = 0.dp
                    ) {
                        var expanded by remember {
                            mutableStateOf(false)
                        }
                        SettingItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expanded = true }
                                .padding(vertical = 8.dp)
                        ) {
                            Text("Wind Speed Unit", color = MaterialTheme.colorScheme.onBackground)
                            WindSpeedMenu(
                                windUnit = windUnit,
                                expanded = expanded,
                                setWindSpeed = setWindSpeed,
                                setExpanded = { expanded = it })
                        }
                    }
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
            text = "About",
            modifier = Modifier.padding(top = 16.dp),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = .5f),
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "This DEMO app is not a production application and is a work in progress 🚧.",
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
fun SettingItem(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {
        content()
    }
}

@Composable
private fun WindSpeedMenu(
    windUnit: String,
    expanded: Boolean,
    setWindSpeed: (WindSpeedUnits) -> Unit,
    setExpanded: (Boolean) -> Unit,
) {
    Column {
        Text(
            text = windUnit,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { setExpanded(false) },
            modifier = Modifier.wrapContentSize(),
            offset = DpOffset(x = 25.dp, y = 4.dp),
        ) {
            DropdownMenuItem(
                text = { Text(text = "km/h") },
                onClick = {
                    setWindSpeed(WindSpeedUnits.KM)
                    setExpanded(false)
                },
                enabled = true
            )
            DropdownMenuItem(
                text = { Text(text = "m/s") },
                onClick = {
                    setWindSpeed(WindSpeedUnits.MS)
                    setExpanded(false)
                },
                enabled = true
            )
            DropdownMenuItem(
                text = { Text(text = "mph") },
                onClick = {
                    setWindSpeed(WindSpeedUnits.MPH)
                    setExpanded(false)
                },
                enabled = true
            )
        }
    }
}

@Composable
private fun TemperatureMenu(
    tempUnit: String,
    expanded: Boolean,
    setTemperature: (TemperatureUnits) -> Unit,
    setExpanded: (Boolean) -> Unit,
) {
    Column {
        Text(
            text = tempUnit,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { setExpanded(false) },
            modifier = Modifier.wrapContentSize(),
            offset = DpOffset(x = 25.dp, y = 4.dp),
        ) {
            DropdownMenuItem(
                text = { Text(text = "°C") },
                onClick = {
                    setTemperature(TemperatureUnits.C)
                    setExpanded(false)
                },
                enabled = true
            )
            DropdownMenuItem(
                text = { Text(text = "°F") },
                onClick = {
                    setTemperature(TemperatureUnits.F)
                    setExpanded(false)
                },
                enabled = true
            )
        }
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
