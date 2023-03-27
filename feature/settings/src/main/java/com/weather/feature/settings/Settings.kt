package com.weather.feature.settings

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
            .background(color = MaterialTheme.colors.background),
    ) {
        Settings(
            settingsState = settingsUIState,
            onBackPressed = { onBackPressed() },
            setTemperature = viewModel::setTemperatureUnit,
            setWindSpeed = viewModel::setWindSpeedUnit

        )
    }
}

@Composable
internal fun Settings(
    settingsState: SettingsUIState,
    onBackPressed: () -> Unit,
    setTemperature: (TemperatureUnits) -> Unit,
    setWindSpeed: (WindSpeedUnits) -> Unit,
) {
    when (settingsState) {
        is SettingsUIState.Loading -> Text(text = "loading")
        is SettingsUIState.Success -> {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)) {
                val tempUnit = when (settingsState.settingsData.temperatureUnits) {
                    TemperatureUnits.C -> "°C"
                    TemperatureUnits.F -> "°F"
                    null -> null
                }
                val windUnit = when (settingsState.settingsData.windSpeedUnits) {
                    null -> null
                    WindSpeedUnits.KM -> "Kilometer per hour"
                    WindSpeedUnits.MS -> "Meters per second"
                    WindSpeedUnits.MPH -> "Miles per hour"
                }
                TopAppBar(
                    backgroundColor = MaterialTheme.colors.background,
                    elevation = 0.dp
                ) {
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                        SettingsTopBar(onBackPressed)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Units", color = MaterialTheme.colors.onBackground.copy(alpha = .5f),
                    fontSize = 12.sp
                )
                Surface(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colors.background,
                    elevation = 0.dp
                ) {
                    var expanded by remember {
                        mutableStateOf(false)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = true }
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text("Temperature Unit", color = MaterialTheme.colors.onBackground)
                        Column {
                            Text(
                                text = tempUnit ?: "null",
                                color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f),
                                modifier = Modifier.clickable { expanded = true })
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.wrapContentSize(),
                                offset = DpOffset(x = 25.dp, y = 4.dp),
                            ) {
                                DropdownMenuItem(
                                    onClick = { setTemperature(TemperatureUnits.C) },
                                    enabled = true
                                ) {
                                    Text(text = "°C")
                                }
                                Divider(color = MaterialTheme.colors.onSurface)
                                DropdownMenuItem(
                                    onClick = { setTemperature(TemperatureUnits.F) },
                                    enabled = true
                                ) {
                                    Text(text = "°F")
                                }
                            }
                        }
                    }

                }
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colors.background,
                    elevation = 0.dp
                ) {
                    var expanded by remember {
                        mutableStateOf(false)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = true }
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Wind Speed Unit", color = MaterialTheme.colors.onBackground)
                        Column {
                            Text(
                                text = windUnit ?: "null",
                                color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f),
                                modifier = Modifier.clickable { expanded = true })
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.wrapContentSize(),
                                offset = DpOffset(x = 25.dp, y = 4.dp),
                            ) {
                                DropdownMenuItem(
                                    onClick = { setWindSpeed(WindSpeedUnits.KM) },
                                    enabled = true
                                ) {
                                    Text(text = "km/h")
                                }
                                DropdownMenuItem(
                                    onClick = { setWindSpeed(WindSpeedUnits.MS) },
                                    enabled = true
                                ) {
                                    Text(text = "m/s")
                                }
                                Divider(color = MaterialTheme.colors.onSurface)
                                DropdownMenuItem(
                                    onClick = { setWindSpeed(WindSpeedUnits.MPH) },
                                    enabled = true
                                ) {
                                    Text(text = "mph")
                                }
                            }
                        }
                    }
                }
                Text(
                    text = "About",
                    modifier = Modifier.padding(top = 16.dp),
                    color = MaterialTheme.colors.onBackground.copy(alpha = .5f),
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "A Weather Demo App Developed By Arash Ebrahimzade.\n" +
                            "This app is is a work in progress 🚧.",
                    color = MaterialTheme.colors.onBackground.copy(alpha = .5f),
                    fontSize = 12.sp,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Weather Data from Openweathermap.org",
                    color = MaterialTheme.colors.onBackground.copy(alpha = .5f),
                    fontSize = 12.sp,
                )
            }
        }
    }
}

@Composable
fun SettingsTopBar(onBackPressed: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(onClick = { onBackPressed() }) {
            Icon(
                modifier = Modifier,
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back Icon"
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Settings",
            fontSize = 18.sp
        )
    }
}

@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
private fun SettingsPreview() {
    WeatherTheme() {
        val temp = TemperatureUnits.F
        val wind = WindSpeedUnits.KM
        Settings(
            SettingsUIState.Success(settingsData = SettingsData(wind, temp)),
            onBackPressed = {}, setTemperature = {}, setWindSpeed = {})
    }
}

@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
private fun MenuPreview() {
    WeatherTheme() {
        var expanded by remember {
            mutableStateOf(true)
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            DropdownMenu(
                expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier
                    .background(Color.Black)
                    .border(width = 1.dp, color = Color.Black)
            ) {
                DropdownMenuItem(onClick = {}, enabled = true) {
                    Text(text = "Item111111111")
                }
                Divider(color = Color.Blue)
                DropdownMenuItem(onClick = { }, enabled = false) {
                    Text(text = "Item2")
                }
            }

        }
    }
}
