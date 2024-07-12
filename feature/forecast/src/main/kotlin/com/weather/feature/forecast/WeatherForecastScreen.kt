package com.weather.feature.forecast

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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
    WeatherForecastScreen(
        weatherUIState = weatherUIState,
        timeOfDay = timeOfDay,
        isSyncing = syncing,
        onNavigateToManageLocations = { onNavigateToManageLocations() },
        onNavigateToSettings = { onNavigateToSettings() },
        onRefresh = viewModel::sync
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun WeatherForecastScreen(
    modifier: Modifier = Modifier,
    weatherUIState: SavableForecastData,
    timeOfDay: TimeOfDay,
    isSyncing: Boolean,
    onNavigateToManageLocations: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onRefresh: (Coordinate) -> Unit,
) {
    val resource = LocalContext.current.resources
    val density = LocalDensity.current
    val config = LocalConfiguration.current
    val scope = rememberCoroutineScope()
    var firstScrollableItemHeight by rememberSaveable {
        mutableIntStateOf(0)
    }
    val conditionID = weatherUIState.weather.current.weather[0].id
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
    var isScrollEnabled by rememberSaveable {
        mutableStateOf(false)
    }
    val decay = rememberSplineBasedDecay<Float>()
    val draggableState = remember {
        AnchoredDraggableState(
            initialValue = Anchors.Closed,
            positionalThreshold = { totalDistance -> totalDistance * 0.5f },
            snapAnimationSpec = spring(),
            decayAnimationSpec = decay,
            velocityThreshold = { with(density) { 100.dp.toPx() } }
        )
    }
    LaunchedEffect(key1 = draggableState.currentValue) {
        isScrollEnabled = draggableState.currentValue == Anchors.OPEN
    }
    val connection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                //disable scrolling if Anchor is Anchor.OPEN and
                //scroll position is 0(at the beginning)
                //and scrolling down
                //NOTE: if scrolling is disabled this connection and
                //all in child composable(s) scrollable wont work
                if (available.y > 0f && draggableState.currentValue == Anchors.OPEN && scrollState.value == 0) {
                    isScrollEnabled = false
                }
                return super.onPreScroll(available, source)
            }
        }
    }
    val hazeState = remember { HazeState() }
    Scaffold(
        topBar = {
            ForecastTopBar(
                onNavigateToManageLocations = { onNavigateToManageLocations() },
                onNavigateToSettings = { onNavigateToSettings() })
        }) { scaffoldPadding ->
        WeatherBackground(
            modifier = Modifier
//                .navigationBarsPadding()
                .padding(scaffoldPadding)
                .haze(hazeState),
            conditionID = conditionID,
            isDay = timeOfDay == TimeOfDay.Day,
            isDawn = timeOfDay == TimeOfDay.Dawn
        ) {
            Column(
                modifier = Modifier
                    .nestedScroll(connection)
                    .anchoredDraggable(draggableState, Orientation.Vertical)
                    .pullRefresh(refreshState) then modifier
            ) {
                CompositionLocalProvider(LocalContentColor provides Color.White) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        PullRefreshIndicator(refreshing = isSyncing, state = refreshState)
                        CurrentWeather(
                            modifier = Modifier
                                .padding(top = 60.dp, bottom = 100.dp)
                                .graphicsLayer {
                                    //can be enabled after implementing independent scrolling
                                    alpha =
                                        if (draggableState.currentValue == Anchors.OPEN) 0f else 1f
//                                if (draggableState.currentValue == Anchors.Closed)
//                                    draggableState.progress
//                                else 0.5f
                                },
                            location = weatherUIState.weather.coordinates.name,
                            weatherData = weatherUIState.weather.current,
                            today = weatherUIState.weather.daily[0],
                            showPlaceholder = false,
                        )
                        ConditionAndDetails(
                            hazeState = hazeState,
                            modifier = Modifier
                                .fillMaxSize()
                                .offset {
                                    IntOffset(
                                        x = 0,
                                        y = draggableState
                                            .requireOffset()
                                            .toInt()
                                    )
                                },
                            scrollState = scrollState,
                            isScrollEnabled = isScrollEnabled,
                            weatherData = weatherUIState.weather,
                            isDayTime = timeOfDay != TimeOfDay.Night,
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
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ConditionAndDetails(
    hazeState: HazeState,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    isScrollEnabled: Boolean = true,
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
            .verticalScroll(scrollState, isScrollEnabled),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        maxItemsInEachRow = 2
    ) {
        //widgets
        // TODO: Weather alert goes here
        DailyWidget(
            modifier = Modifier
                .fillMaxWidth()
                .hazeChild(hazeState, shape = RoundedCornerShape(16.dp))
                .onSizeChanged { firstItemHeight(it.height) },
            dailyList = weatherData.daily,
            currentTemp = weatherData.current.currentTemp.roundToInt(),
            surfaceColor = widgetColor
        )
        HourlyWidget(
            modifier = Modifier
                .fillMaxWidth()
                .hazeChild(hazeState, shape = RoundedCornerShape(16.dp)),
            hourly = weatherData.hourly,
            speedUnit = speedUnit,
            surfaceColor = widgetColor
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            WindWidget(
                modifier = Modifier
                    .weight(1f)
                    .hazeChild(hazeState, shape = RoundedCornerShape(16.dp)),
                windDirection = weatherData.current.wind_deg,
                windSpeed = weatherData.current.wind_speed.roundToInt(),
                speedUnits = speedUnit,
                surfaceColor = widgetColor
            )
            SunWidget(
                modifier = Modifier
                    .weight(1f)
                    .hazeChild(hazeState, shape = RoundedCornerShape(16.dp)),
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
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            RealFeelWidget(
                modifier = Modifier
                    .weight(1f)
                    .hazeChild(hazeState, shape = RoundedCornerShape(16.dp)),
                realFeel = weatherData.current.feels_like.roundToInt(),
                surfaceColor = widgetColor
            )
            HumidityWidget(
                modifier = Modifier
                    .weight(1f)
                    .hazeChild(hazeState, shape = RoundedCornerShape(16.dp)),
                humidity = weatherData.current.humidity,
                surfaceColor = widgetColor
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            UVWidget(
                modifier = Modifier
                    .weight(1f)
                    .hazeChild(hazeState, shape = RoundedCornerShape(16.dp)),
                uvIndex = weatherData.current.uvi.toInt(),
                surfaceColor = widgetColor
            )
            PressureWidget(
                modifier = Modifier
                    .weight(1f)
                    .hazeChild(hazeState, shape = RoundedCornerShape(16.dp)),
                pressure = weatherData.current.pressure,
                surfaceColor = widgetColor
            )
        }
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
                timeOfDay = TimeOfDay.Day,
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