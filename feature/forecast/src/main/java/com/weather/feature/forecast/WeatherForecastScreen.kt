package com.weather.feature.forecast

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.weather.core.design.components.ShowLoadingText
import com.weather.core.design.theme.WeatherTheme
import com.weather.feature.forecast.components.*
import com.weather.feature.forecast.components.Daily
import com.weather.feature.forecast.components.HourlyForecast
import com.weather.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.math.roundToInt

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@Composable
fun WeatherForecastScreen(
    viewModel: ForecastViewModel = hiltViewModel(),
    navigateToManageLocations: () -> Unit,
    onNavigateToSettings: () -> Unit,
    navigateToOnboard: () -> Unit,
) {
    //stateful
    val databaseIsEmpty by viewModel.dataBaseOrCityIsEmpty.collectAsStateWithLifecycle()
    val weatherUIState by viewModel
        .weatherUIState.collectAsStateWithLifecycle()
    val syncing by viewModel.isWorkRunning.collectAsStateWithLifecycle()

    if (databaseIsEmpty) {
        LaunchedEffect(key1 = Unit) {
            navigateToOnboard()
        }
    } else {
        Box(modifier = Modifier.background(color = MaterialTheme.colors.background)) {
            WeatherForecastScreen(
                weatherUIState = weatherUIState,
                isSyncing = syncing,
//                speedUnit = "",
//                temperatureUnit = "",
//                distanceUnit = "",
                onNavigateToManageLocations = { navigateToManageLocations() },
                onNavigateToSettings = { onNavigateToSettings() },
                onRefresh = viewModel::sync
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun WeatherForecastScreen(
    weatherUIState: WeatherUIState,
    isSyncing: Boolean,
//    speedUnit: String,
//    temperatureUnit: String,
//    distanceUnit: String,
    onNavigateToManageLocations: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onRefresh: (Coordinate) -> Unit,
) {
    val lazyListState = rememberLazyListState()
    // stateless
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        when (weatherUIState) {
            WeatherUIState.Loading -> ShowLoadingText()
            is WeatherUIState.Success -> {
                val pullRefreshState = rememberPullRefreshState(
                    refreshing = isSyncing,
                    onRefresh = {
                        onRefresh(weatherUIState.data.weather.coordinates.let {
                            Coordinate(it.name, it.lat.toString(), it.lon.toString())
                        })
                    },
                )
                val speedUnit by remember {
                    val state = when (weatherUIState.data.userSettings.windSpeedUnits) {
                        WindSpeedUnits.KM -> "km/h"
                        WindSpeedUnits.MS -> "m/s"
                        WindSpeedUnits.MPH -> "mph"
                        null -> "null"
                    }
                    mutableStateOf(state)
                }
                val temperatureUnit by remember {
                    val state = when (weatherUIState.data.userSettings.temperatureUnits) {
                        TemperatureUnits.C -> "C"
                        TemperatureUnits.F -> "F"
                        null -> "null"
                    }
                    mutableStateOf(state)
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .pullRefresh(pullRefreshState),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    ForecastTopBar(
                        cityName = weatherUIState.data.weather.coordinates.name.toString(),
                        onNavigateToManageLocations = { onNavigateToManageLocations() },
                        onNavigateToSettings = { onNavigateToSettings() })
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = lazyListState,
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        item {
                            ConditionAndDetails(
                                weatherUIState.data.weather,
                                speedUnit = speedUnit,
                                temperatureUnit = temperatureUnit,
                                distanceUnit = "m"
                            )
                        }
                    }
                }
                PullRefreshIndicator(
                    refreshing = isSyncing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}

@Composable
fun ConditionAndDetails(
    weatherData: WeatherData,
    speedUnit: String,
    temperatureUnit: String,
    distanceUnit: String,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        CurrentWeather(
            modifier = Modifier.padding(vertical = 60.dp),
            weatherData = weatherData.current
        )
        Spacer(modifier = Modifier.height(100.dp))
        CurrentWeatherDetails(
            modifier = Modifier
                .padding(horizontal = 1.dp)
                .fillMaxWidth(),
            weatherData = weatherData.current,
            speedUnit = speedUnit,
            distanceUnit = distanceUnit
        )
        Daily(
            modifier = Modifier.fillMaxWidth(),
            dailyList = weatherData.daily.map { it.toDailyPreview() })
        HourlyForecast(
            modifier = Modifier
                .padding(bottom = 16.dp),
            data = weatherData.hourly
        )
    }
}

@Composable
fun SunMoonPosition() {
    Card(
        modifier = Modifier,
        shape = RoundedCornerShape(8.dp)
    ) {
        Canvas(modifier = Modifier) {
            drawRect(color = Color.Red)
            drawArc(
                brush = Brush.linearGradient(listOf(Color.Red, Color.Blue)),
                startAngle = 40f, sweepAngle = 50f, useCenter = false, topLeft = Offset(80f, 20f)
            )
        }
    }
}


@Composable
fun CurrentDetails(
    modifier: Modifier = Modifier,
    visibility: String, humidity: String, airPressure: String,
) {
    val textSize = 14.sp
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.surface,
    ) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Visibility",
                        fontSize = textSize,
                    )
                    Text(
                        text = "$visibility km/h",
                        fontSize = textSize
                    )
                }
                Divider(
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Humidity",
                        fontSize = textSize
                    )
                    Text(
                        text = "$humidity%",
                        fontSize = textSize
                    )
                }
                Divider(
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Air Pressure",
                        fontSize = textSize
                    )
                    Text(
                        text = "$airPressure mb",
                        fontSize = textSize
                    )
                }
            }

        }
    }
}


@Composable
private fun CurrentWeather(
    modifier: Modifier = Modifier,
    weatherData: Current,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CurrentTempAndCondition(
            modifier = Modifier
                .fillMaxWidth(),
            icon = weatherData.weather[0].icon,
            temp = weatherData.temp.roundToInt().toString(),
            feelsLikeTemp = weatherData.feels_like.roundToInt().toString(),
            condition = weatherData.weather.first().main
        )
    }
}

@Composable
fun CurrentWeatherDetails(
    modifier: Modifier = Modifier,
    weatherData: Current,
    speedUnit: String,
    distanceUnit: String,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        WeatherDetailItem(
            image = Icons.Outlined.Air,
            value = "${weatherData.wind_speed}$speedUnit",
            itemName = "Wind Speed"
        )
        WeatherDetailItem(
            image = Icons.Outlined.WaterDrop,
            value = "${weatherData.humidity}%",
            itemName = "Humidity"
        )
        WeatherDetailItem(
            image = Icons.Outlined.Visibility,
            value = "${weatherData.visibility}$distanceUnit",
            itemName = "Visibility"
        )
        WeatherDetailItem(
            image = Icons.Outlined.Directions,
            value = "${weatherData.wind_deg}",
            itemName = "Wind Direction"
        )
    }
}

@Composable
private fun WeatherDetailItem(
    image: ImageVector,
    value: String,
    itemName: String,
) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = image,
                contentDescription = itemName,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colors.onBackground
            )
            Text(
                text = value,
                fontSize = 10.sp,
                color = MaterialTheme.colors.onBackground
            )
        }
        Text(
            text = itemName,
            fontSize = 10.sp,
            color = MaterialTheme.colors.onBackground
        )
    }
}


@Composable
private fun CurrentTempAndCondition(
    modifier: Modifier = Modifier,
    icon: String,
    temp: String,
    feelsLikeTemp: String,
    condition: String,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = condition,
                fontSize = 24.sp,
                color = MaterialTheme.colors.onBackground
            )
            Text(
                text = "$temp°",
                fontSize = 70.sp,
                color = MaterialTheme.colors.onBackground
            )
            Text(
                text = "Feels like $feelsLikeTemp°",
                fontSize = 14.sp,
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f)
            )
        }
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun MainPagePreview() {
    WeatherTheme {
        val data = WeatherUIState.Success(
            data = SavableForecastData(
                weather = WeatherData(
                    coordinates = OneCallCoordinates(
                        name = "Tehran",
                        lat = 0.0,
                        lon = 0.0,
                        timezone = "tehran",
                        timezone_offset = 0
                    ),
                    current = Current(
                        clouds = 27,
                        dew_point = 273.46,
                        dt = 1674649142,
                        feels_like = 286.08,
                        humidity = 38,
                        pressure = 1017,
                        sunrise = 1674617749,
                        sunset = 1674655697,
                        temp = 287.59,
                        uvi = 0.91,
                        visibility = 10000,
                        wind_deg = 246,
                        wind_gust = 1.71,
                        wind_speed = 2.64,
                        weather = listOf(
                            Weather("", "", 0, "Snow")
                        )
                    ),
                    daily = DailyStaticData,
                    hourly = HourlyStaticData,
                ),
                userSettings = SettingsData(WindSpeedUnits.KM, TemperatureUnits.C)
            )
        )
        Box(modifier = Modifier.background(color = MaterialTheme.colors.background)) {
            WeatherForecastScreen(weatherUIState = data,
                isSyncing = false,
//                speedUnit = "",
//                temperatureUnit = "",
//                distanceUnit = "",
                onNavigateToManageLocations = {},
                onNavigateToSettings = {},
                onRefresh = {})
        }
    }
}

@Composable
@Preview(showBackground = true)
fun CurrentWeatherPreview() {
    WeatherTheme {
        CurrentTempAndCondition(temp = "5", feelsLikeTemp = "3", icon = "02d", condition = "Snow")
    }
}

@Preview(showBackground = false, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun WindPreview() {
    WeatherTheme {
        val animateDegree = remember {
            Animatable(25f)
        }
        LaunchedEffect(key1 = Unit) {
            animateDegree.animateTo(113f, tween(1000, 100, easing = EaseOutCubic))
        }
        WindDetails(Modifier, animateDegree.value, 3.52f)
    }
}

@Preview(showBackground = false, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun CurrentDetails() {
    WeatherTheme {
        CurrentDetails(
            Modifier,
            "300", "60", "1005"
        )
    }
}

@Preview(showBackground = false, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun SunPositionPreview() {
    WeatherTheme {
        SunMoonPosition()
    }
}
