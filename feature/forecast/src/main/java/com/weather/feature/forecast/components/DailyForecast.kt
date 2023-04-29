package com.weather.feature.forecast.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.weather.model.Daily
import com.weather.model.DailyPreview
import kotlin.math.roundToInt

@Composable
fun Daily(
    modifier: Modifier = Modifier,
    dailyList: List<DailyPreview>,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Daily",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
            )
            dailyList.forEach { daily ->
                DailyItem(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp), daily)
            }
        }
    }
}

@Composable
fun DailyItem(modifier: Modifier = Modifier, daily: DailyPreview) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(4f),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "https://openweathermap.org/img/wn/${daily.icon}@2x.png",
                contentDescription = "WeatherIcon",
                modifier = Modifier
            )
            Row {
                Text(text = daily.time)
                Text(text = " - ")
                Text(
                    text = daily.condition,
                    color = Color.Gray
                )
            }
        }
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${daily.tempDay.toFloat().roundToInt()}",
                fontSize = 14.sp,
            )
            Text(
                text = " / ",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = .7f)
            )
            Text(
                text = "${daily.tempNight.toFloat().roundToInt()}°",
                fontSize = 14.sp,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DailyItemPreview() {
    DailyItem(daily = DailyPreviewStaticData[0])
}

@Preview(showBackground = true)
@Composable
private fun DailyListPreview() {
    Daily(dailyList = DailyPreviewStaticData)
}

val DailyStaticData = listOf(
    Daily(
        0,
        0.0,
        "time",
        0,
        0.0,
        0,
        0,
        0.0,
        0,
        0,
        0,
        0.0,
        0.0,
        0.0,
        0,
        "Cloudy",
        "",
        "",
        0,
        0.0,
        0.0
    ),
    Daily(
        0,
        0.0,
        "time",
        0,
        0.0,
        0,
        0,
        0.0,
        0,
        0,
        0,
        0.0,
        0.0,
        0.0,
        0,
        "Rain",
        "",
        "",
        0,
        0.0,
        0.0
    ),
    Daily(
        0,
        0.0,
        "time",
        0,
        0.0,
        0,
        0,
        0.0,
        0,
        0,
        0,
        0.0,
        0.0,
        0.0,
        0,
        "Cloudy",
        "",
        "",
        0,
        0.0,
        0.0
    ),
    Daily(
        0,
        0.0,
        "",
        0,
        0.0,
        0,
        0,
        0.0,
        0,
        0,
        0,
        0.0,
        0.0,
        0.0,
        0,
        "Sunny",
        "",
        "",
        0,
        0.0,
        0.0
    )
)
val DailyPreviewStaticData = listOf(
    DailyPreview(
        tempDay = "283",
        tempNight = "275",
        time = "Tomorrow",
        icon = "",
        condition = "Clouds"
    ),
    DailyPreview(
        tempDay = "280",
        tempNight = "274",
        time = "Wed",
        icon = "",
        condition = "Snow"
    ),
    DailyPreview(
        tempDay = "284",
        tempNight = "276",
        time = "Thur",
        icon = "",
        condition = "Clouds"
    ),
    DailyPreview(
        tempDay = "278",
        tempNight = "270",
        time = "fri",
        icon = "",
        condition = "Rain"
    ),
    DailyPreview(
        tempDay = "281",
        tempNight = "269",
        time = "Fri",
        icon = "",
        condition = "Snow"
    )
)