package com.weather.feature.forecast

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material.icons.outlined.North
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.weather.core.design.components.weatherPlaceholder
import com.weather.core.design.theme.WeatherTheme
import com.weather.feature.forecast.components.Daily
import com.weather.feature.forecast.components.DailyStaticData
import com.weather.feature.forecast.components.ForecastTopBar
import com.weather.feature.forecast.components.HourlyGraph
import com.weather.feature.forecast.components.HourlyWidgetWithGraph
import com.weather.feature.forecast.components.WindDetails
import com.weather.feature.forecast.components.hourlydata.HourlyStaticData
import com.weather.model.Coordinate
import com.weather.model.Current
import com.weather.model.OneCallCoordinates
import com.weather.model.SavableForecastData
import com.weather.model.SettingsData
import com.weather.model.TemperatureUnits
import com.weather.model.Weather
import com.weather.model.WeatherData
import com.weather.model.WindSpeedUnits
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@ExperimentalCoroutinesApi
@Composable
fun WeatherForecastScreen(
    viewModel: ForecastViewModel = hiltViewModel(),
    navigateToManageLocations: () -> Unit,
    onNavigateToSettings: () -> Unit,
) {
    //stateful
    val weatherUIState by viewModel
        .weatherUIState.collectAsStateWithLifecycle()
    val syncing by viewModel.isSyncing.collectAsStateWithLifecycle()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        WeatherForecastScreen(
            weatherUIState = weatherUIState,
            isSyncing = syncing,
            onNavigateToManageLocations = { navigateToManageLocations() },
            onNavigateToSettings = { onNavigateToSettings() },
            onRefresh = viewModel::sync
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WeatherForecastScreen(
    modifier: Modifier = Modifier,
    weatherUIState: SavableForecastData,
    isSyncing: Boolean,
    onNavigateToManageLocations: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onRefresh: (Coordinate) -> Unit,
) {
    val lazyListState = rememberLazyListState()
    // stateless
    val speedUnit by remember(weatherUIState) {
        val state = when (weatherUIState.userSettings.windSpeedUnits) {
            WindSpeedUnits.KM -> "km/h"
            WindSpeedUnits.MS -> "m/s"
            WindSpeedUnits.MPH -> "mph"
            null -> "null"
        }
        mutableStateOf(state)
    }
    val temperatureUnit by remember(weatherUIState) {
        val state = when (weatherUIState.userSettings.temperatureUnits) {
            TemperatureUnits.C -> "C"
            TemperatureUnits.F -> "F"
            null -> "null"
        }
        mutableStateOf(state)
    }
    val refreshState =
        rememberPullRefreshState(refreshing = isSyncing, onRefresh = {
            onRefresh(
                weatherUIState.weather.coordinates.let {
                    Coordinate(it.name, it.lat.toString(), it.lon.toString())
                }
            )
        })
    Column(
        modifier = Modifier
            .pullRefresh(refreshState)
    )
    {
        ForecastTopBar(
            cityName = weatherUIState.weather.coordinates.name,
            showPlaceholder = weatherUIState.showPlaceHolder,
            onNavigateToManageLocations = { onNavigateToManageLocations() },
            onNavigateToSettings = { onNavigateToSettings() })
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            PullRefreshIndicator(refreshing = isSyncing, state = refreshState)
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = lazyListState,
                contentPadding = WindowInsets.navigationBars.asPaddingValues(),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                item {
                    ConditionAndDetails(
                        modifier = Modifier.fillMaxSize(),
                        weatherData = weatherUIState.weather,
                        showPlaceholder = weatherUIState.showPlaceHolder,
                        speedUnit = speedUnit,
                        temperatureUnit = temperatureUnit,
                    )
                }
            }
        }
    }
}

@Composable
fun ConditionAndDetails(
    modifier: Modifier = Modifier,
    weatherData: WeatherData,
    showPlaceholder: Boolean,
    speedUnit: String,
    temperatureUnit: String,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onBackground) {
            CurrentWeather(
                modifier = Modifier.padding(vertical = 60.dp),
                weatherData = weatherData.current,
                showPlaceholder = showPlaceholder,
                temperatureUnit = temperatureUnit
            )
            Spacer(modifier = Modifier.height(100.dp))
            CurrentWeatherDetails(
                modifier = Modifier
                    .padding(horizontal = 1.dp)
                    .fillMaxWidth()
                    .weatherPlaceholder(
                        visible = showPlaceholder,
                    ),
                weatherData = weatherData.current,
                speedUnit = speedUnit,
            )
        }
        Daily(
            modifier = Modifier
                .fillMaxWidth()
                .weatherPlaceholder(
                    visible = showPlaceholder,
                ),
            dailyList = weatherData.daily.map { it.toDailyPreview() })
        HourlyWidgetWithGraph(
            modifier = Modifier.weatherPlaceholder(
                visible = showPlaceholder),
            hourly = weatherData.hourly
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
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
    ) {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onBackground) {
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
    showPlaceholder: Boolean,
    temperatureUnit: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CurrentTempAndCondition(
            modifier = Modifier
                .fillMaxWidth()
                .weatherPlaceholder(
                    visible = showPlaceholder,
                ),
            temp = weatherData.temp.roundToInt().toString(),
            temperatureUnit = temperatureUnit,
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
) {
    val visibility = when {
        weatherData.visibility < 1000 -> {
            "${weatherData.visibility}m"
        }

        weatherData.visibility > 1000 -> {
            "${weatherData.visibility.div(1000)}km"
        }

        else -> {
            "${weatherData.visibility}"
        }
    }
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
            value = visibility,
            itemName = "Visibility"
        )
        WinDirectionDetail(
            image = Icons.Outlined.North,
            value = weatherData.wind_deg,
            itemName = "Wind Direction"
        )
    }
}

@Composable
fun WinDirectionDetail(
    image: ImageVector,
    value: Int,
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
                modifier = Modifier
                    .size(16.dp)
                    .graphicsLayer {
                        rotationZ = value.minus(180f)
                    },
            )
        }
        Text(
            text = itemName,
            fontSize = 10.sp,
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
                modifier = Modifier
                    .size(16.dp),
            )
            Text(
                text = value,
                fontSize = 10.sp,
            )
        }
        Text(
            text = itemName,
            fontSize = 10.sp,
        )
    }
}


@Composable
private fun CurrentTempAndCondition(
    modifier: Modifier = Modifier,
    temp: String,
    temperatureUnit: String,
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
            )
            Text(
                text = "$temp°$temperatureUnit",
                fontSize = 70.sp,
            )
            Text(
                text = "Feels like $feelsLikeTemp°$temperatureUnit",
                fontSize = 14.sp,
                color = LocalContentColor.current.copy(alpha = 0.5f)
            )
        }
    }
}

@ExperimentalMaterialApi
@Preview(name = "night", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(name = "day", showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun MainPagePreview() {
    var placeholder by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = Unit, block = {
        delay(1000)
        placeholder = false
    })
    WeatherTheme {
        val data = SavableForecastData(
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
                    wind_speed = 2.64,
                    weather = listOf(
                        Weather("", "", 0, "Snow")
                    )
                ),
                daily = DailyStaticData,
                hourly = HourlyStaticData,
            ),
            userSettings = SettingsData(WindSpeedUnits.KM, TemperatureUnits.C),
            showPlaceHolder = placeholder
        )
        Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
            WeatherForecastScreen(weatherUIState = data,
                isSyncing = false,
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
        CurrentTempAndCondition(
            temp = "5",
            temperatureUnit = "C",
            feelsLikeTemp = "3",
            condition = "Snow"
        )
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
private fun CurrentDetailsPreview() {
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