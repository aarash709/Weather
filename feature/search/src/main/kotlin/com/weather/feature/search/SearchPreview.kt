package com.weather.feature.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.weather.core.design.theme.WeatherTheme
import com.weather.model.DailyPreview

@Composable
fun FiveDaySearchPreview(weatherPreview: List<DailyPreview>) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            weatherPreview.forEach {
                SearchDailyItem(daily = it,Modifier.weight(1f))
            }
        }

    }
}

@Composable
private fun SearchDailyItem(daily: DailyPreview,modifier: Modifier= Modifier) {
    Column(
        modifier = Modifier.padding(vertical = 0.dp) then modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = daily.time, fontSize = 14.sp)
        AsyncImage(
            model = "https://openweathermap.org/img/wn/${daily.icon}@2x.png",
            contentDescription = "Weather Icon"
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = "${daily.tempDay}°",fontSize = 16.sp)
        Text(text = "${daily.tempNight}°",fontSize = 16.sp)
    }
}

@PreviewLightDark
@Composable
private fun SearchFiveDayPrev() {
    WeatherTheme {
        FiveDaySearchPreview(dailyDummyData)
    }
}

val dailyDummyData = listOf(
    DailyPreview(
        tempDay = 20,
        tempNight = 11,
        time = "Today",
        icon = "",
        condition = ""
    ),
    DailyPreview(
        tempDay = 21,
        tempNight = 12,
        time = "Tomorrow",
        icon = "",
        condition = ""
    ), DailyPreview(
        tempDay = 18,
        tempNight = 10,
        time = "Tue",
        icon = "",
        condition = ""
    ), DailyPreview(
        tempDay = 15,
        tempNight = 8,
        time = "Wed",
        icon = "",
        condition = ""
    ), DailyPreview(
        tempDay = 14,
        tempNight = 6,
        time = "Thur",
        icon = "",
        condition = ""
    )
)