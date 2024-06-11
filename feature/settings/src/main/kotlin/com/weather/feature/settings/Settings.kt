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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import com.experiment.weather.core.common.R

@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
) {
    val settingsUIState by viewModel.settingsUIState.collectAsStateWithLifecycle()
    var tempUnit by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    var windUnit by remember {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = settingsUIState) {
        when (settingsUIState) {
            is SettingsUIState.Success -> {
                tempUnit =
                    when ((settingsUIState as SettingsUIState.Success).settingsData.temperatureUnits) {
                        TemperatureUnits.C -> context.getString(R.string.celsius_symbol)
                        TemperatureUnits.F -> context.getString(R.string.fahrenheit_symbol)
                        else -> ""
                    }
                windUnit =
                    when ((settingsUIState as SettingsUIState.Success).settingsData.windSpeedUnits) {
                        WindSpeedUnits.KM -> context.getString(R.string.kilometer_per_hour)
                        WindSpeedUnits.MS -> context.getString(R.string.meters_per_second)
                        WindSpeedUnits.MPH -> context.getString(R.string.miles_per_hour)
                        else -> ""
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
                        text = stringResource(id = R.string.settings),
                        scrollBehavior = scrollBehavior,
                        onBackPressed = { onBackPressed() })
                    SettingGroup(
                        modifier = Modifier,
                        groupName = stringResource(id = R.string.units)
                    ) {
                        TemperatureSection(
                            title = stringResource(id = R.string.Temperature),
                            tempUnitName = tempUnit,
                            setTemperature = setTemperature
                        )
                        WindSpeedSection(
                            title = stringResource(id = R.string.wind_speed),
                            windSpeedUnitName = windUnit,
                            setWindSpeed = setWindSpeed
                        )
                    }
                    SettingsHorizontalDivider()
                    SettingGroup(
                        modifier = Modifier.padding(vertical = 16.dp),
                        groupName = stringResource(id = R.string.about)
                    ) {
                        About(modifier = Modifier.padding(horizontal = 16.dp))
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
            text = stringResource(id = R.string.about_description),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = .5f),
            fontSize = 14.sp,
        )
        Text(
            text = stringResource(id = R.string.about_data_source),
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