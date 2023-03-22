package com.weather.feature.forecast

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.weather.core.design.theme.WeatherTheme
import com.weather.core.design.theme.White
import com.weather.feature.forecast.components.*
import com.weather.model.Current
import com.weather.model.OneCallCoordinates
import com.weather.model.Weather
import com.weather.model.WeatherData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.math.roundToInt

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@Composable
fun WeatherForecastScreen(
    viewModel: ForecastViewModel = hiltViewModel(),
    navigateToManageLocations: () -> Unit,
    navigateToOnboard: () -> Unit,
) {
    //stateful
    val databaseIsEmpty by viewModel.dataBaseOrCityIsEmpty.collectAsStateWithLifecycle()
    val weatherUIState by viewModel
        .weatherUIState.collectAsStateWithLifecycle()
    val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()

    if (databaseIsEmpty) {
        LaunchedEffect(key1 = Unit) {
            navigateToOnboard()
        }
    } else {
        Box(modifier = Modifier.background(color = MaterialTheme.colors.background)) {
            WeatherForecastScreen(
                weatherUIState = weatherUIState,
                isSyncing = isSyncing,
                onNavigateToManageLocations = { navigateToManageLocations() },
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
    onNavigateToManageLocations: () -> Unit,
    onRefresh: () -> Unit,
) {
    val lazyListState = rememberLazyListState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isSyncing,
        onRefresh = onRefresh,
    )
    // stateless
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .pullRefresh(pullRefreshState)
    ) {
        when (weatherUIState) {
            WeatherUIState.Loading -> ShowLoading()
            is WeatherUIState.Success -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
//                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    TopAppBar(
                        modifier = Modifier,
                        backgroundColor = MaterialTheme.colors.background,
                        elevation = 0.dp
                    ) {
                        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                            TopBar(
                                cityName = weatherUIState.data.coordinates.name.toString(),
                                onNavigateToManageLocations = { onNavigateToManageLocations() }
                            )
                        }
                    }
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = lazyListState,
                    ) {
                        item {
                            ConditionAndDetails(weatherUIState.data)
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
fun ConditionAndDetails(weatherData: WeatherData) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CurrentWeather(
            modifier = Modifier.padding(vertical = 64.dp),
            weatherData = weatherData.current
        )
        Spacer(modifier = Modifier.height(64.dp))
//        CurrentWeatherDetails(
//            weatherData = weatherData.current
//        )
//        Row(
//            modifier = Modifier
//                .height(IntrinsicSize.Max)
//                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            WindDetails(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .weight(1f),
//                weatherData.current.wind_deg.toFloat(),
//                weatherData.current.wind_speed.toFloat()
//            )
//            CurrentDetails(
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .weight(1f),
//                weatherData.current.visibility.toString(),
//                weatherData.current.humidity.toString(),
//                weatherData.current.pressure.toString(),
//            )
//        }
        CurrentWeatherDetails(
            modifier = Modifier.padding(horizontal = 1.dp).fillMaxWidth(),
            weatherData = weatherData.current)
//        SunMoonPosition()
        Daily(
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
fun WindDetails(
    modifier: Modifier = Modifier,
    windDirection: Float,
    windSpeed: Float,
) {
    val animatedDegree = remember {
        Animatable(0f)
    }
    LaunchedEffect(key1 = Unit) {
        animatedDegree.animateTo(
            windDirection,
            tween(
                1000,
                100,
                easing = EaseOutCubic
            )
        )
    }
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(modifier = Modifier
            .padding(8.dp)
            .height(IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically) {
            BoxedWindIndicator(
                modifier = Modifier.weight(2f),
                windDirection = animatedDegree.value
            )
            Column(
                modifier = Modifier.weight(2f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "${windSpeed}km/h")
            }
        }
    }
}

@Composable
fun BoxedWindIndicator(modifier: Modifier = Modifier, windDirection: Float) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .border(width = 2.dp, color = White, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.North,
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer {
                    rotationZ = windDirection.times(-1)
                },
            contentDescription = "Wind direction arrow"
        )
        Box(modifier = Modifier
            .padding(4.dp)
            .fillMaxSize()) {
            Text(text = "N", modifier = Modifier.align(Alignment.TopCenter))
            Text(text = "E", modifier = Modifier.align(Alignment.CenterEnd))
            Text(text = "S", modifier = Modifier.align(Alignment.BottomCenter))
            Text(text = "W", modifier = Modifier.align(Alignment.CenterStart))
        }

    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun WindDirectionIndicator(
    modifier: Modifier = Modifier,
    animatedDegree: Float,
) {
    val textMeasurer = rememberTextMeasurer()
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
//                        .padding(8.dp)
                .aspectRatio(1f)
                .fillMaxSize()
        ) {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val lineWidth = 1.dp.toPx()
            drawText(
                textMeasurer = textMeasurer,
                text = "N",
                style = TextStyle(color = Color.White),
                topLeft = Offset(
                    x = centerX - 15,
                    y = 10f
                )
            )
            drawText(
                textMeasurer = textMeasurer,
                text = "E",
                style = TextStyle(color = Color.White),
                topLeft = Offset(
                    x = size.width - 40,
                    y = centerY - 25
                )
            )
            drawText(
                textMeasurer = textMeasurer,
                text = "S",
                style = TextStyle(color = Color.White),
                topLeft = Offset(
                    x = centerX - 15,
                    y = size.height - 60
                )
            )
            drawText(
                textMeasurer = textMeasurer,
                text = "W",
                style = TextStyle(color = Color.White),
                topLeft = Offset(
                    x = 10f,
                    y = centerY - 25
                ),

                )
            //debug line
//            drawLine(
//                color = White,
//                start = Offset(0f, centerY),
//                end = Offset(size.width, centerY),
//                strokeWidth = lineWidth
//            )
            //**
            withTransform({
                rotate(degrees = animatedDegree)
            }) {
                drawLine(
                    Color.White,
                    start = Offset(centerX, centerY.minus(centerY.div(2))),
                    end = Offset(centerX, centerY.plus(centerY.div(2))),
                    strokeWidth = lineWidth,
                    cap = StrokeCap.Round,
                )
                rotate(40f, pivot = Offset(centerX, centerY.minus(centerY.div(2)))) {
                    drawLine(
                        Color.Red,
                        start = Offset(centerX, centerY.minus(centerY.div(2))),
                        end = Offset(centerX, centerY),
                        strokeWidth = lineWidth,
                        cap = StrokeCap.Round
                    )
                }
                rotate(-40f, pivot = Offset(centerX, centerY.minus(centerY.div(2)))) {
                    drawLine(
                        Color.Red,
                        start = Offset(centerX, centerY.minus(centerY.div(2))),
                        end = Offset(centerX, centerY),
                        strokeWidth = lineWidth,
                        cap = StrokeCap.Round
                    )
                }
            }
            drawCircle(
                color = Color.White.copy(alpha = 0.5f),
                center = Offset(centerX, y = centerY),
                radius = centerX,
                style = Stroke(width = lineWidth)
            )
//                drawRect(Color.White.copy(alpha = 0.5f), style = Stroke(lineWidth))
//                val vertcalSize = size.width / (3 + 1)
//                repeat(3) {
//                    val startx = vertcalSize * (it + 1)
//                    drawLine(
//                        Color.White,
//                        start = Offset(startx, 0f),
//                        end = Offset(startx, size.height)
//                    )
//                }
//                repeat(4) {
//                    val startx = (size.height / (4 + 1)) * (it + 1)
//                    drawLine(
//                        Color.White,
//                        start = Offset(0f, startx),
//                        end = Offset(size.width, startx)
//                    )
//                }
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
private fun TopBar(
    cityName: String,
    onNavigateToManageLocations: () -> Unit,
) {
    // TODO:  //handled in the gps handler later on
    val locationBased by remember {
        mutableStateOf<Boolean>(value = false)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onNavigateToManageLocations() }) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        }
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            if (locationBased) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Location Icon"
                )
            }
            Text(
                text = cityName,
                fontSize = 20.sp
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Location Picker Icon"
            )
        }
//        Switch(checked = false, onCheckedChange = {/*todo change theme*/ })
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Location Pick Icon"
        )
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
            temp = weatherData.temp.minus(273.15).roundToInt().toString(),
            feelsLikeTemp = weatherData.feels_like.minus(273.15).roundToInt().toString(),
            condition = weatherData.weather.first().main
        )
    }
}

@Composable
fun CurrentWeatherDetails(
    modifier: Modifier = Modifier,
    weatherData: Current,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        WeatherDetailItem(
            image = Icons.Outlined.Air,
            value = "${weatherData.wind_speed}km/h",
            itemName = "Wind Speed"
        )
        WeatherDetailItem(
            image = Icons.Outlined.WaterDrop,
            value = "${weatherData.humidity}%",
            itemName = "Humidity"
        )
        WeatherDetailItem(
            image = Icons.Outlined.Visibility,
            value = "${weatherData.visibility}m",
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
//        AsyncImage(
//            model = "https://openweathermap.org/img/wn/$icon@2x.png",
//            contentDescription = "WeatherIcon",
//            modifier = Modifier.size(120.dp)
//        )
//        Image(
//            imageVector = Icons.Outlined.WbCloudy,
//            contentDescription = "Current Weather",
//            modifier = Modifier.size(128.dp)
//        )
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
                fontSize = 60.sp,
                color = MaterialTheme.colors.onBackground
            )
            Text(
                text = "Feels like $feelsLikeTemp°",
                fontSize = 12.sp,
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
            data = WeatherData(
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
                        Weather("", "", 0, "")
                    )
                ),
                daily = DailyStaticData,
                hourly = HourlyStaticData
            )
        )
        Box(modifier = Modifier.background(color = MaterialTheme.colors.background)) {
            WeatherForecastScreen(weatherUIState = data,
                false,
                onNavigateToManageLocations = {}, onRefresh = {})
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

@Preview
@Composable
private fun WindIndicatorPreview() {
    WeatherTheme {
        WindDirectionIndicator(animatedDegree = 0f)
    }
}

@Preview
@Composable
private fun BoxedWindIndicatorPreview() {
    WeatherTheme {
        BoxedWindIndicator(windDirection = 0f)
    }
}



