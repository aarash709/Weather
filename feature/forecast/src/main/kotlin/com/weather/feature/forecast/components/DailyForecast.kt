package com.weather.feature.forecast.components

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowRight
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
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
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(15.dp)
                            .background(Color.White.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CalendarMonth,
                            modifier = Modifier.padding(3.dp),
                            tint = Color.Blue.copy(alpha = 0.4f),
                            contentDescription = null
                        )
                    }
                    Text(
                        text = "5-Day forecast",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "More details",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowRight,
                        modifier = Modifier.padding(3.dp),
                        tint = Color.White.copy(alpha = 0.5f),
                        contentDescription = null
                    )
                }
            }
            dailyList.forEachIndexed { index, daily ->
                DailyItem(
                    modifier = Modifier
                        .fillMaxWidth(),
                    daily = daily
                )
                if (index != dailyList.lastIndex) {
                    HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                }
            }
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White.copy(alpha = 0.1f)
            ) {
                Text(
                    text = "5-day forecast",
                    modifier = Modifier
                        .padding(horizontal = 64.dp, vertical = 6.dp)
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
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
        AsyncImage(
            model = "https://openweathermap.org/img/wn/${daily.icon}@2x.png",
            contentDescription = "WeatherIcon",
            modifier = Modifier
        )
        Row(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${daily.tempNight.toFloat().roundToInt()}°",
                modifier = Modifier.weight(1.0f),
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
            )
            //            Text(
//                text = " / ",
//                fontSize = 14.sp,
//                color = Color.White.copy(alpha = 0.25f)
//            )
            //experimental temp bars for daily views
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black.copy(alpha = 0.2f)),
            ) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clip(RoundedCornerShape(16.dp))
                        .background(color = Color.Red)
                )
            }
            Text(
                text = "${daily.tempDay.toFloat().roundToInt()}",
                modifier = Modifier.weight(1.0f),
                textAlign = TextAlign.Right,
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