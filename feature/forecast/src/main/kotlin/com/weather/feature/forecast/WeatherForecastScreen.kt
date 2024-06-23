package com.weather.feature.forecast

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.experiment.weather.core.common.R
import com.weather.core.design.components.weatherPlaceholder
import com.weather.core.design.theme.ForecastTheme
import com.weather.feature.forecast.components.ForecastTopBar
import com.weather.feature.forecast.components.WeatherBackground
import com.weather.feature.forecast.components.hourlydata.DailyStaticData
import com.weather.feature.forecast.components.hourlydata.HourlyStaticData
import com.weather.feature.forecast.widgets.DailyWidget
import com.weather.feature.forecast.widgets.HourlyWidget
import com.weather.feature.forecast.widgets.HumidityWidget
import com.weather.feature.forecast.widgets.PressureWidget
import com.weather.feature.forecast.widgets.RealFeelWidget
import com.weather.feature.forecast.widgets.SunWidget
import com.weather.feature.forecast.widgets.UVWidget
import com.weather.feature.forecast.widgets.WindWidget
import com.weather.model.Coordinate
import com.weather.model.Current
import com.weather.model.Daily
import com.weather.model.OneCallCoordinates
import com.weather.model.SavableForecastData
import com.weather.model.SettingsData
import com.weather.model.TemperatureUnits
import com.weather.model.Weather
import com.weather.model.WeatherData
import com.weather.model.WindSpeedUnits
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

enum class Anchors {
    OPEN, Closed
}

@ExperimentalCoroutinesApi
@Composable
fun WeatherForecastRoute(
    viewModel: ForecastViewModel = hiltViewModel(),
    onNavigateToManageLocations: () -> Unit,
    onNavigateToSettings: () -> Unit,
) {
    //stateful
    val weatherUIState by viewModel
        .weatherUIState.collectAsStateWithLifecycle()
    val syncing by viewModel.isSyncing.collectAsStateWithLifecycle()
    val timeOfDay by viewModel.timeOfDay.collectAsStateWithLifecycle()
    val conditionID = weatherUIState.weather.current.weather[0].id
    WeatherBackground(
        conditionID = conditionID,
        isDay = timeOfDay == TimeOfDay.Day,
        isDawn = timeOfDay == TimeOfDay.Dawn
    ) {
        WeatherForecastScreen(
            weatherUIState = weatherUIState,
            isDayTime = timeOfDay != TimeOfDay.Night,
            isSyncing = syncing,
            onNavigateToManageLocations = { onNavigateToManageLocations() },
            onNavigateToSettings = { onNavigateToSettings() },
            onRefresh = viewModel::sync
        )
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun WeatherForecastScreen(
    modifier: Modifier = Modifier,
    weatherUIState: SavableForecastData,
    isDayTime: Boolean,
    isSyncing: Boolean,
    onNavigateToManageLocations: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onRefresh: (Coordinate) -> Unit,
) {
    val resource = LocalContext.current.resources
    val density = LocalDensity.current
    val config = LocalConfiguration.current
    var height by remember {
        mutableFloatStateOf(0f)
    }
    var firstScrollableItemHeight by rememberSaveable {
        mutableIntStateOf(0)
    }

    val speedUnit by remember(weatherUIState) {
        val value = when (weatherUIState.userSettings.windSpeedUnits) {
            WindSpeedUnits.KM -> resource.getString(R.string.kilometer_per_hour_symbol)
            WindSpeedUnits.MS -> resource.getString(R.string.meters_per_second_symbol)
            WindSpeedUnits.MPH -> resource.getString(R.string.miles_per_hour_symbol)
            null -> "null"
        }
        mutableStateOf(value)
    }
    val refreshState =
        rememberPullRefreshState(refreshing = isSyncing, onRefresh = {
            onRefresh(
                weatherUIState.weather.coordinates.let {
                    Coordinate(it.name, it.lat.toString(), it.lon.toString())
                }
            )
        })
    val scrollState = rememberScrollState()
//    val scrollProgress by remember(scrollState.value) {
//        derivedStateOf {
//            (scrollState.value.toFloat() / scrollState.maxValue.toFloat()).times(100)
//        }
//    }

        val anchors = DraggableAnchors {
            Anchors.Closed at 0f
            Anchors.OPEN at 0f
        }
        val draggableState = remember {
            AnchoredDraggableState(
                initialValue = Anchors.Closed,
//                anchors = anchors,
                positionalThreshold = { totalDistance -> totalDistance * 0.1f },
                animationSpec = spring(),
                velocityThreshold = { with(density) { 50.dp.toPx() } }
            )
        }
        val connection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    Timber.e("avalible: ${available.y}")
                    Timber.e("draggg: ${draggableState.currentValue.name}")
                    if (available.y > 0f) {
                        draggableState.dispatchRawDelta(available.y)
                        return Offset.Zero
                    }
                    if (available.y < 0f && draggableState.currentValue == Anchors.Closed){
                        scrollState.dispatchRawDelta(0f)
                        draggableState.dispatchRawDelta(available.y)
                        return Offset.Zero
                    }
//                    scrollState.dispatchRawDelta(delta)
                    return super.onPreScroll(available, source)
                }
            }
        }
    Column(
        modifier = Modifier
            .nestedScroll(connection)
            .pullRefresh(refreshState) then modifier
    ) {
        // TODO: remove
        //expertiment
//        ModalBottomSheet(onDismissRequest = { /*TODO*/ }) {
//
//        }
        CompositionLocalProvider(LocalContentColor provides Color.White) {
            ForecastTopBar(
                onNavigateToManageLocations = { onNavigateToManageLocations() },
                onNavigateToSettings = { onNavigateToSettings() })
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
//                        height = size.height
                    }
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                PullRefreshIndicator(refreshing = isSyncing, state = refreshState)
                CurrentWeather(
                    modifier = Modifier
                        .padding(top = 60.dp, bottom = 100.dp)
                        .graphicsLayer {
                            //can be enabled after implementing independent scrolling
//                            alpha -= draggableState.progress
//                                .toFloat()
//                                .times(3f)
//                                .div(draggableState.targetValue)
                        },
                    location = weatherUIState.weather.coordinates.name,
                    weatherData = weatherUIState.weather.current,
                    today = weatherUIState.weather.daily[0],
                    showPlaceholder = false,
                )
//                LazyColumn(modifier = Modifier
//                    .fillMaxSize()
//                    .anchoredDraggable(draggableState, Orientation.Vertical)
//                    .offset {
//                        IntOffset(
//                            x = 0,
//                            y = draggableState
//                                .requireOffset()
//                                .toInt()
//                        )
//                    },) {
//                    items(100){
//                        Box(Modifier.fillMaxSize()) {
//                            Text(text = "text")
//                        }
//                    }
//                    item { Text(text = "last") }
//                }
//                Surface(
//                    onClick = { /*TODO*/ },
//                    color = Color.Transparent,
//                    modifier = Modifier
//
//                ) {
                ConditionAndDetails(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding()
                        .anchoredDraggable(draggableState, Orientation.Vertical)
                        .offset {
                            IntOffset(
                                x = 0,
                                y = draggableState
                                    .requireOffset()
                                    .toInt()
                            )
                        },
                    scrollState = scrollState,
                    weatherData = weatherUIState.weather,
                    isDayTime = isDayTime,
                    showPlaceholder = weatherUIState.showPlaceHolder,
                    speedUnit = speedUnit,
                    shouldChangeColor = /*scrollProgress > 10*/ false,
                    firstItemHeight = {
                        firstScrollableItemHeight = it
                        draggableState.updateAnchors(DraggableAnchors {
                            Anchors.Closed at with(density) { config.screenHeightDp.dp.toPx() - it.toFloat() * 2 }
                            Anchors.OPEN at 0f
                        })
                    }
                )
//                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ConditionAndDetails(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    weatherData: WeatherData,
    isDayTime: Boolean,
    showPlaceholder: Boolean,
    speedUnit: String,
    shouldChangeColor: Boolean,
    firstItemHeight: (Int) -> Unit,
) {
    val dayTimePrimaryColor = Color.Black.copy(
        alpha = 0.10f
    )
    val nightTimePrimaryColor = Color.White.copy(
        alpha = 0.10f
    )
    val primaryWidgetColor by
    animateColorAsState(
        targetValue = if (isDayTime) dayTimePrimaryColor else nightTimePrimaryColor,
        label = "primary widget background color"
    )
    val widgetColor by
    animateColorAsState(
        targetValue =
        if (shouldChangeColor && isDayTime) ForecastTheme.colorScheme.background
        else primaryWidgetColor,
        animationSpec = tween(durationMillis = 200),
        label = "scrolled widget background color"
    )
    FlowRow(
        modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        maxItemsInEachRow = 2
    ) {
//        CurrentWeather(
//            modifier = Modifier
//                .padding(top = 60.dp, bottom = 100.dp)
//                .graphicsLayer {
//                    //can be enabled after implementing independent scrolling
//                    //alpha -= scrollState.value.toFloat().times(3f).div(scrollState.maxValue)
//                },
//            location = weatherData.coordinates.name,
//            weatherData = weatherData.current,
//            today = weatherData.daily[0],
//            showPlaceholder = false,
//        )
//        Spacer(modifier = Modifier.height(65.dp))
        //widgets
        // TODO: Weather alert goes here
        DailyWidget(
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged { firstItemHeight(it.height) },
            dailyList = weatherData.daily,
            currentTemp = weatherData.current.currentTemp.roundToInt(),
            surfaceColor = widgetColor
        )
        HourlyWidget(
            modifier = Modifier
                .fillMaxWidth(),
            hourly = weatherData.hourly,
            speedUnit = speedUnit,
            surfaceColor = widgetColor
        )
        WindWidget(
            modifier = Modifier.weight(1f),
            windDirection = weatherData.current.wind_deg,
            windSpeed = weatherData.current.wind_speed.roundToInt(),
            speedUnits = speedUnit,
            surfaceColor = widgetColor
        )
        SunWidget(
            modifier = Modifier.weight(1f),
            formattedSunrise = SimpleDateFormat(
                "HH:mm",
                Locale.getDefault()
            ).format(Date(weatherData.current.sunrise.toLong() * 1000)),
            formattedSunset = SimpleDateFormat(
                "HH:mm",
                Locale.getDefault()
            ).format(Date(weatherData.current.sunset.toLong() * 1000)),
            sunriseSeconds = weatherData.current.sunrise,
            sunsetSeconds = weatherData.current.sunset,
            currentTimeSeconds = weatherData.current.dt,
            surfaceColor = widgetColor
        )
        RealFeelWidget(
            modifier = Modifier.weight(1f),
            realFeel = weatherData.current.feels_like.roundToInt(),
            surfaceColor = widgetColor
        )
        HumidityWidget(
            modifier = Modifier.weight(1f),
            humidity = weatherData.current.humidity,
            surfaceColor = widgetColor
        )
        UVWidget(
            modifier = Modifier.weight(1f),
            uvIndex = weatherData.current.uvi.toInt(),
            surfaceColor = widgetColor
        )
        PressureWidget(
            modifier = Modifier.weight(1f),
            pressure = weatherData.current.pressure,
            surfaceColor = widgetColor
        )
    }
}

@Composable
private fun CurrentWeather(
    modifier: Modifier = Modifier,
    location: String,
    weatherData: Current,
    today: Daily,
    showPlaceholder: Boolean,
) {
    val highTemp = today.dayTemp.roundToInt().toString()
    val lowTemp = today.nightTemp.roundToInt().toString()
    val condition = weatherData.weather.first().description
    Row(
        modifier = modifier
            .fillMaxWidth()
            .weatherPlaceholder(
                visible = showPlaceholder,
            ),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = location,
                fontSize = 16.sp,
            )
            Text(
                text = "${weatherData.currentTemp.roundToInt()}°",
                fontSize = 120.sp,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = condition,
                    fontSize = 16.sp,
                    color = LocalContentColor.current.copy(alpha = 0.75f)
                )
                Text(
                    text = "$highTemp°/$lowTemp°",
                    fontSize = 18.sp,
                    color = LocalContentColor.current.copy(alpha = 0.75f)
                )
            }
        }
    }
}

@ExperimentalMaterialApi
@Preview(name = "night", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(name = "day", showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun MainPagePreview() {
    var placeholder by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = Unit, block = {
        delay(1000)
        placeholder = false
    })
    ForecastTheme {
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
                    currentTemp = 287.59,
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
        Box(
            modifier = Modifier
//            .background(color = ForecastTheme.colorScheme.background)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Blue.copy(alpha = .5f),
                            Color.Blue
                        )
                    )
                )
        ) {
            WeatherForecastScreen(
                weatherUIState = data,
                isSyncing = false,
                isDayTime = false,
                onRefresh = {},
                onNavigateToManageLocations = {},
                onNavigateToSettings = {}
            )
        }
    }
}


@Preview(showBackground = false, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun WindPreview() {
    ForecastTheme {
        val animateDegree = remember {
            Animatable(25f)
        }
        LaunchedEffect(key1 = Unit) {
            animateDegree.animateTo(113f, tween(1000, 100, easing = EaseOutCubic))
        }
    }
}