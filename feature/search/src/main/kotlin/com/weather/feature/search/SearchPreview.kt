package com.weather.feature.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.weather.core.design.theme.WeatherTheme
import com.weather.model.DailyPreview

@Composable
fun FiveDaySearchPreview(dailyPreview: List<DailyPreview>) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            dailyPreview.forEach {
                SearchDailyItem(daily = it)
            }
        }

    }
}

@Composable
private fun SearchDailyItem(daily: DailyPreview) {
    Column(
        modifier = Modifier.padding(vertical = 0.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = daily.time)
        Icon(imageVector = Icons.Outlined.WbSunny, contentDescription = "Condition Icon")
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = "${daily.tempDay}")
        Text(text = "${daily.tempNight}")
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