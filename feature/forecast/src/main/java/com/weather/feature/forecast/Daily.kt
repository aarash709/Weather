package com.weather.feature.forecast

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tsunami
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weather.model.DailyPreview

@Composable
fun Daily(
    modifier: Modifier = Modifier,
    dailyList: List<DailyPreview>,
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items(dailyList) { daily ->
            DailyItem(modifier = Modifier, daily)
        }
    }
}

@Composable
fun DailyItem(modifier: Modifier = Modifier, daily: DailyPreview) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = daily.time)
            Text(
                text = daily.time, fontSize = 10.sp,
                color = Color.Gray
            )
        }
        Icon(imageVector = Icons.Default.WbSunny, contentDescription = "Weather Icon")
        Text(
            text = daily.temp,
            color = Color.Gray
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DailyItemPreview() {
    DailyItem(daily = DailyData[0])
}

@Preview(showBackground = true)
@Composable
private fun DailyListPreview() {
    Daily(dailyList = DailyData)
}

val DailyData = listOf(
    DailyPreview(
        temp = "10",
        time = "Today",
        icon = ""
    ),
    DailyPreview(
        temp = "8",
        time = "Tomorrow",
        icon = ""
    ),
    DailyPreview(
        temp = "11",
        time = "Wed",
        icon = ""
    ),
    DailyPreview(
        temp = "6",
        time = "Thur",
        icon = ""
    ),
    DailyPreview(
        temp = "8",
        time = "Fri",
        icon = ""
    )
)