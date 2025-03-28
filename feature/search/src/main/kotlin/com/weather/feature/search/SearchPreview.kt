package com.weather.feature.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.weather.core.design.theme.WeatherTheme
import com.weather.model.DailyPreview

@Composable
fun FiveDaySearchPreview(weatherPreview: List<DailyPreview>) {
	Card {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(bottom = 16.dp),
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			weatherPreview.forEach {
				SearchDailyItem(dailyPreview = it, Modifier.weight(1f))
			}
		}

	}
}

@Composable
private fun SearchDailyItem(dailyPreview: DailyPreview, modifier: Modifier = Modifier) {
	Column(
		modifier = Modifier then modifier,
		verticalArrangement = Arrangement.spacedBy(12.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(text = dailyPreview.time, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
		AsyncImage(
			model = dailyPreview.iconUrl,
			contentDescription = "Weather Icon"
		)
		Spacer(modifier = Modifier.height(2.dp))
		Text(text = "${dailyPreview.tempDay}°", fontSize = 16.sp)
		Text(text = "${dailyPreview.tempNight}°", fontSize = 16.sp)
	}
}

@PreviewLightDark
@Composable
private fun SearchFiveDayPrev() {
	WeatherTheme {
		FiveDaySearchPreview(dailyPreviewDummyData)
	}
}

val dailyPreviewDummyData = listOf(
	DailyPreview(
		tempDay = 20,
		tempNight = 11,
		time = "Today",
		weatherCode = 0,
		iconUrl = ""
	),
	DailyPreview(
		tempDay = 21,
		tempNight = 12,
		time = "Tomorrow",
		weatherCode = 0,
		iconUrl = "",
	), DailyPreview(
		tempDay = 18,
		tempNight = 10,
		time = "Tue",
		weatherCode = 0,
		iconUrl = ""
	), DailyPreview(
		tempDay = 15,
		tempNight = 8,
		time = "Wed",
		weatherCode = 0,
		iconUrl = ""
	), DailyPreview(
		tempDay = 14,
		tempNight = 6,
		time = "Thur",
		weatherCode = 0,
		iconUrl = "",
	)
)