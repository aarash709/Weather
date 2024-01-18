package com.weather.feature.forecast.components

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.weather.core.design.modifiers.bouncyTapEffect
import com.weather.core.design.theme.WeatherTheme
import com.weather.model.Daily
import com.weather.model.DailyPreview
import kotlin.math.roundToInt

@Composable
fun Daily(
    modifier: Modifier = Modifier,
    dailyList: List<DailyPreview>,
    surfaceColor: Color = Color.White.copy(alpha = 0.15f),
) {
    Surface(
        modifier = Modifier.bouncyTapEffect() then modifier,
        shape = RoundedCornerShape(16.dp),
        color = surfaceColor,
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            dailyList.forEach { daily ->
                DailyItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp), daily
                )
            }
        }
    }
}

@Composable
fun DailyItem(modifier: Modifier = Modifier, daily: DailyPreview) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = daily.time, modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(2f)
        )
        AsyncImage(
            model = "https://openweathermap.org/img/wn/${daily.icon}@2x.png",
            contentDescription = "WeatherIcon",
            modifier = Modifier
        )
        Row(
            modifier = Modifier
                .weight(2f),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${daily.tempDay.toFloat().roundToInt()}",
                fontSize = 14.sp,
            )
            Text(
                text = " / ",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.25f)
            )
            Text(
                text = "${daily.tempNight.toFloat().roundToInt()}°",
                fontSize = 14.sp,
            )
        }
    }
}

@Preview(
    name = "night", showBackground = true, uiMode = UI_MODE_NIGHT_YES,
    backgroundColor = 0xFF5366C5
)
@Preview(
    name = "light", showBackground = true, uiMode = UI_MODE_NIGHT_NO,
    backgroundColor = 0xFF5366C5
)
@Composable
private fun DailyListPreview() {
    WeatherTheme {
        Daily(
            dailyList = DailyPreviewStaticData,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DailyItemPreview() {
    WeatherTheme {
        DailyItem(daily = DailyPreviewStaticData[0])
    }
}


val DailyStaticData = listOf(
    Daily(
        0,
        0.0,
        "Tomorrow",
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
        "Friday",
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
        "Saturday",
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
        "Sunday",
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
        time = "Friday",
        icon = "",
        condition = "Rain"
    ),
    DailyPreview(
        tempDay = "281",
        tempNight = "269",
        time = "Saturday",
        icon = "",
        condition = "Snow"
    )
)