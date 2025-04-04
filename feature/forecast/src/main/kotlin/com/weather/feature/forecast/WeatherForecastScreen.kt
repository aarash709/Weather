package com.weather.feature.forecast

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.experiment.weather.core.common.R
import com.weather.core.design.theme.ForecastTheme
import com.weather.feature.forecast.components.CurrentWeather
import com.weather.feature.forecast.components.ForecastTopBar
import com.weather.feature.forecast.components.PagerIndicators
import com.weather.feature.forecast.components.WeatherBackground
import com.weather.feature.forecast.hourlydata.DailyDummyData
import com.weather.feature.forecast.hourlydata.HourlyStaticData
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
import com.weather.model.SavableForecastData
import com.weather.model.SettingsData
import com.weather.model.TemperatureUnits
import com.weather.model.WeatherCoordinates
import com.weather.model.WeatherData
import com.weather.model.WindSpeedUnits
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToInt

@ExperimentalCoroutinesApi
@Composable
fun WeatherForecastRoute(
	viewModel: ForecastViewModel = hiltViewModel(),
	onNavigateToManageLocations: () -> Unit,
	onNavigateToSettings: () -> Unit,
) {
	//stateful
	val forecastData by viewModel
		.weatherUIState.collectAsStateWithLifecycle()
	val favoriteCity by viewModel.favoriteCity.collectAsStateWithLifecycle()
	val settings by viewModel
		.userSettings.collectAsStateWithLifecycle()
	val syncing by viewModel.isSyncing.collectAsStateWithLifecycle()
	val timeOfDay by viewModel.timeOfDay.collectAsStateWithLifecycle()
	WeatherForecastScreen(
		forecastData = forecastData,
		favoriteCity = favoriteCity,
		settingsData = settings,
		timeOfDay = timeOfDay,
		isSyncing = syncing,
		onNavigateToManageLocations = { onNavigateToManageLocations() },
		onNavigateToSettings = { onNavigateToSettings() },
		onRefresh = viewModel::sync
	)
}

@OptIn(
	ExperimentalMaterial3Api::class
)
@Composable
fun WeatherForecastScreen(
	modifier: Modifier = Modifier,
	forecastData: List<SavableForecastData>,
	favoriteCity: String?,
	settingsData: SettingsData,
	timeOfDay: TimeOfDay,
	isSyncing: Boolean,
	onNavigateToManageLocations: () -> Unit,
	onNavigateToSettings: () -> Unit,
	onRefresh: (Coordinate) -> Unit,
) {
	val resource = LocalContext.current.resources
	val speedUnit by remember(forecastData) {
		val value = when (settingsData.windSpeedUnits) {
			WindSpeedUnits.KM -> resource.getString(R.string.kilometer_per_hour_symbol)
			WindSpeedUnits.MS -> resource.getString(R.string.meters_per_second_symbol)
			WindSpeedUnits.MPH -> resource.getString(R.string.miles_per_hour_symbol)
		}
		mutableStateOf(value)
	}
	val temperatureUnit = settingsData.temperatureUnits
	Scaffold(
		modifier = Modifier
			.fillMaxSize(),
		contentWindowInsets = WindowInsets(0, 0, 0, 0)
	) { padding ->
		WeatherBackground(
			modifier = modifier
				.padding(padding),
			showBackground = false
		) {
			Column(
				modifier = Modifier
			) {
				CompositionLocalProvider(LocalContentColor provides Color.White) {
					var topAppBarSize by remember {
						mutableIntStateOf(0)
					}
					val density = LocalDensity.current
					ForecastTopBar(
						modifier = Modifier
							.statusBarsPadding()
							.onGloballyPositioned {
								topAppBarSize =
									with(density) { it.size.height.toDp().value.toInt() }
							},
						onNavigateToManageLocations = { onNavigateToManageLocations() },
						onNavigateToSettings = { onNavigateToSettings() })
					val pagerState = rememberPagerState(
						pageCount = { forecastData.size }
					)
					PullToRefreshBox(
						modifier = Modifier.padding(horizontal = 16.dp),
						isRefreshing = isSyncing,
						onRefresh = {
							onRefresh(
								forecastData[pagerState.currentPage].weather.coordinates.let {
									Coordinate(it.name, it.lat.toString(), it.lon.toString())
								}
							)
						},
						contentAlignment = Alignment.TopCenter,
						content = {
														LaunchedEffect(key1 = favoriteCity) {
								val page = forecastData.indexOfFirst {
									it.weather.coordinates.name == (favoriteCity
										?: it.weather.coordinates.name)
								}
								pagerState.scrollToPage(page)
							}
							var currentWeatherSize by remember {
								mutableIntStateOf(0)
							}
							val currentPageIndex = pagerState.currentPage
							val scrollState = rememberScrollState()
							CurrentWeather(
								modifier = Modifier
									.padding(top = 50.dp)
									.graphicsLayer {
										val scrollProgress =
											(scrollState.value.toFloat() / scrollState.maxValue.toFloat())
										val newAlpha = 1 - scrollProgress
											.times(5)
										val scale = 1 - scrollProgress
										scaleX = scale
										scaleY = scale
										alpha = newAlpha
										translationY = scrollState.value.toFloat() / 2 * -1
									}
									.onGloballyPositioned {
										currentWeatherSize =
											with(density) { it.size.height.toDp().value.toInt() }
									},
								weatherData = forecastData[currentPageIndex].weather,
								indicator = {
									PagerIndicators(
										modifier = Modifier,
										pagerState = pagerState,
										currentPage = currentPageIndex,
										height = 6.dp
									)
								}
							)
							HorizontalPager(state = pagerState, pageSpacing = 16.dp) { index ->
								WeatherDetails(
									modifier = Modifier
										.padding(top = 0.dp),
									scrollState = scrollState,
									weatherData = forecastData[index].weather,
									isDayTime = timeOfDay != TimeOfDay.Night,
									showPlaceholder = forecastData[index].showPlaceHolder,
									speedUnit = speedUnit,
									tempUnit = temperatureUnit,
									shouldChangeColor = /*scrollProgress > 10*/ false,
									firstItemHeight = currentWeatherSize + topAppBarSize
								)
							}
						})
				}
			}
		}
	}
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun WeatherDetails(
	modifier: Modifier = Modifier,
	scrollState: ScrollState = rememberScrollState(),
	isScrollEnabled: Boolean = true,
	weatherData: WeatherData,
	isDayTime: Boolean,
	showPlaceholder: Boolean,
	speedUnit: String,
	tempUnit: TemperatureUnits,
	shouldChangeColor: Boolean,
	firstItemHeight: Int,
) {
	//these colors are used if a static or animated background is present
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
	//if background is disabled use this color for widgets surface color
	val surfaceColor = ForecastTheme.colorScheme.surface
	val rowPadding = 8.dp
	val topPadding = firstItemHeight.plus(50).dp
	FlowRow(
		modifier
			.verticalScroll(scrollState, isScrollEnabled)
			.navigationBarsPadding(),
		horizontalArrangement = Arrangement.spacedBy(1.dp), //buggy so there has to be at least 1.dp
		verticalArrangement = Arrangement.spacedBy(rowPadding),
		maxItemsInEachRow = 2
	) {
		Spacer(modifier = Modifier.height(topPadding))
		// TODO: Weather alert goes here
		DailyWidget(
			modifier = Modifier
				.fillMaxWidth(),
			dailyList = weatherData.daily,
			currentTemp = weatherData.current.currentTemp.roundToInt(),
			tempUnit = tempUnit,
			surfaceColor = surfaceColor
		)
		HourlyWidget(
			modifier = Modifier
				.fillMaxWidth(),
			hourly = weatherData.hourly,
			speedUnit = speedUnit,
			tempUnit = tempUnit,
			surfaceColor = surfaceColor
		)
		Row(horizontalArrangement = Arrangement.spacedBy(rowPadding)) {
			WindWidget(
				modifier = Modifier
					.weight(1f),
				weatherData = weatherData,
				speedUnits = speedUnit,
				surfaceColor = surfaceColor
			)
			var formattedSunrise by remember {
				mutableStateOf("00:00")
			}
			var formattedSunset by remember {
				mutableStateOf("00:00")
			}
			LaunchedEffect(key1 = weatherData) {
				val sdf = SimpleDateFormat(
					"HH:mm",
					Locale.getDefault()
				)
				val sunrise = weatherData.current.sunrise.toLong()
				val sunset = weatherData.current.sunset.toLong()
				sdf.timeZone = TimeZone.getTimeZone("UTC")
				formattedSunrise = sdf.format(Date(sunrise * 1000))
				formattedSunset = sdf.format(Date(sunset * 1000))
			}
			val offset = weatherData.coordinates.timezoneOffset.toLong()
			val currentTimeSeconds by remember(weatherData) {
				mutableIntStateOf(Instant.now().plusSeconds(offset).epochSecond.toInt())
			}
			SunWidget(
				modifier = Modifier
					.weight(1f),
				formattedSunrise = formattedSunrise,
				formattedSunset = formattedSunset,
				weatherData = weatherData,
				currentTimeSeconds = currentTimeSeconds,
				surfaceColor = surfaceColor
			)
		}
		Row(horizontalArrangement = Arrangement.spacedBy(rowPadding)) {
			RealFeelWidget(
				modifier = Modifier
					.weight(1f),
				realFeel = weatherData.current.feelsLike.roundToInt(),
				surfaceColor = surfaceColor
			)
			HumidityWidget(
				modifier = Modifier
					.weight(1f),
				humidity = weatherData.current.humidity,
				surfaceColor = surfaceColor
			)
		}
		Row(horizontalArrangement = Arrangement.spacedBy(rowPadding)) {
			UVWidget(
				modifier = Modifier
					.weight(1f),
				uvIndex = weatherData.current.uvi.toInt(),
				surfaceColor = surfaceColor
			)
			PressureWidget(
				modifier = Modifier
					.weight(1f),
				pressure = weatherData.current.pressure.toInt(),
				surfaceColor = surfaceColor
			)
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
		val data = listOf(
			SavableForecastData(
				weather = WeatherData(
					coordinates = WeatherCoordinates(
						name = "Tehran",
						lat = 0.0,
						lon = 0.0,
						timezone = "tehran",
						timezoneOffset = 0
					),
					current = Current(
						time = "time",
						feelsLike = 286.08,
						humidity = 38,
						pressure = 1017.0,
						sunrise = 1674617749,
						sunset = 1674655697,
						currentTemp = 287.59,
						uvi = 0.91,
						visibility = 10000,
						windDirection = 246,
						windSpeed = 2.64,
						condition = ""
					),
					daily = DailyDummyData,
					hourly = HourlyStaticData,
				),
				showPlaceHolder = placeholder
			)
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
				forecastData = data,
				favoriteCity = "",
				settingsData = SettingsData(),
				timeOfDay = TimeOfDay.Day,
				isSyncing = false,
				onNavigateToManageLocations = {},
				onNavigateToSettings = {},
				onRefresh = {}
			)
		}
	}
}