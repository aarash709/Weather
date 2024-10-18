package com.weather.feature.forecast.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExposureNeg1
import androidx.compose.material.icons.filled.ExposurePlus1
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weather.core.design.components.weatherPlaceholder
import com.weather.core.design.theme.ForecastTheme
import com.weather.model.WeatherData
import kotlin.math.roundToInt

@Composable
internal fun CurrentWeather(
	modifier: Modifier = Modifier,
	weatherData: WeatherData,
	location: String = weatherData.coordinates.name,
	showPlaceholder: Boolean = false,
	indicator: @Composable () -> Unit,
) {
	val today = weatherData.daily.first()
	val highTemp = today.dayTemp.roundToInt().toString()
	val lowTemp = today.nightTemp.roundToInt().toString()
	val condition = weatherData.current.weather.first().description
	Row(
		modifier = modifier
			.fillMaxWidth()
			.weatherPlaceholder(
				visible = showPlaceholder,
			),
		horizontalArrangement = Arrangement.Center,
		verticalAlignment = Alignment.CenterVertically
	) {
		Column(
			modifier = Modifier,
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.spacedBy(8.dp)
		) {
			Column(horizontalAlignment = Alignment.CenterHorizontally) {
				Text(
					text = location,
					fontSize = 18.sp,
				)
				indicator()
				Text(
					text = condition,
					fontSize = 14.sp,
					color = LocalContentColor.current.copy(alpha = 0.75f)
				)
			}
			val currentTemp = weatherData.current.currentTemp.roundToInt()
			Row(
				modifier = Modifier.animateContentSize(),
				horizontalArrangement = Arrangement.Center,
				verticalAlignment = Alignment.CenterVertically
			) {
				currentTemp.toString().forEach { char ->
					AnimatedContent(targetState = char, transitionSpec = {
						if (targetState > initialState) {
							slideInVertically { -it } togetherWith slideOutVertically { it }
						} else {
							slideInVertically { it } togetherWith slideOutVertically { -it }
						}
					}, label = "current temp") { temp ->
						Text(
							text = "$temp",
							fontSize = 120.sp,
						)
					}
				}
			}
			Text(
				text = "High $highTemp° • Low $lowTemp°",
				fontSize = 14.sp,
				color = LocalContentColor.current.copy(alpha = 0.75f)
			)
		}
	}
}

@Composable
internal fun PagerIndicators(
	modifier: Modifier = Modifier,
	height: Dp = 8.dp,
	pagerState: PagerState,
	currentPage: Int,
) {
	val count = pagerState.pageCount
	Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
		repeat(count) { iteration ->
			val selected = currentPage == iteration
			val color = if (selected) Color.White else Color.DarkGray
			val width by animateDpAsState(if (selected) 16.dp else 8.dp, label = "")
			Box(
				modifier = Modifier
					.padding(5.dp)
					.clip(CircleShape)
					.size(width = width, height = height)
					.background(color)
			)
		}
	}
}

@Preview
@Composable
private fun IndicatorPreview() {
	ForecastTheme {
		var currentPage by remember {
			mutableIntStateOf(0)
		}
		Column(horizontalAlignment = Alignment.CenterHorizontally) {
			val pagerState = rememberPagerState(
				pageCount = { 5 }
			)
			PagerIndicators(pagerState = pagerState, currentPage = currentPage)
			Button(onClick = { currentPage += 1 }) {
				Icon(
					Icons.Default.ExposurePlus1,
					contentDescription = null
				)
			}
			Button(onClick = { currentPage -= 1 }) {
				Icon(
					Icons.Default.ExposureNeg1,
					contentDescription = null
				)
			}
		}
	}
}