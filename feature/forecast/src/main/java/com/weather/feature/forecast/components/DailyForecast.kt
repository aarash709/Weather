package com.weather.feature.forecast.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.weather.core.design.theme.Gray
import com.weather.model.DailyPreview
import kotlin.math.roundToInt

@Composable
fun Daily(
    modifier: Modifier = Modifier,
    dailyList: List<DailyPreview>,
) {
    Box(
        modifier = Modifier.drawBehind {
            drawRoundRect(color=  Color.LightGray, cornerRadius = CornerRadius(x = 30f))
        },
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            dailyList.forEach { daily ->
                DailyItem(modifier = Modifier, daily)
            }
        }
    }
}

@Composable
fun DailyItem(modifier: Modifier = Modifier, daily: DailyPreview) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
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
//            Icon(imageVector = Icons.Default.WbSunny, contentDescription = "Weather Icon")
            Row(

            ) {
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
                text = "${daily.tempDay.toFloat().minus(273.15).roundToInt()}",
                fontSize = 14.sp,
            )
            Text(
                text = " / ",
                fontSize = 14.sp,
            color = Color.White.copy(alpha = .7f))
            Text(
                text = "${daily.tempNight.toFloat().minus(273.15).roundToInt()}°",
                fontSize = 14.sp,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DailyItemPreview() {
    DailyItem(daily = DailyStaticData[0])
}

@Preview(showBackground = true)
@Composable
private fun DailyListPreview() {
    Daily(dailyList = DailyStaticData)
}

val DailyStaticData = listOf(
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