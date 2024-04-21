package com.weather.feature.forecast.widgets

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowRight
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.experiment.weather.core.common.R.*
import com.weather.core.design.modifiers.bouncyTapEffect
import com.weather.core.design.theme.WeatherTheme
import com.weather.feature.forecast.R
import com.weather.feature.forecast.components.TempBar
import com.weather.feature.forecast.components.TempData
import com.weather.feature.forecast.components.hourlydata.DailyPreviewStaticData
import com.weather.model.DailyPreview
import timber.log.Timber
import kotlin.math.roundToInt

@Composable
internal fun DailyWidget(
    modifier: Modifier = Modifier,
    dailyList: List<DailyPreview>,
    currentTemp: Int,
    surfaceColor: Color = Color.Black.copy(alpha = 0.10f),
) {
    Surface(
        modifier = Modifier.bouncyTapEffect() then modifier,
        shape = RoundedCornerShape(16.dp),
        color = surfaceColor,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
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
                        text = stringResource(id = string.five_day_forecast),
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(id = string.more_details),
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
            HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
            val minTemp by remember(dailyList) {
                mutableIntStateOf(dailyList.minOf { it.tempNight })
            }
            val maxTemp by remember(dailyList) {
                mutableIntStateOf(dailyList.maxOf { it.tempDay })
            }
            dailyList.forEachIndexed { index, daily ->
                val currentLow = daily.tempNight
                val currentHigh = daily.tempDay
                DailyItem(
                    modifier = Modifier
                        .fillMaxWidth(),
                    daily = daily,
                    tempData = TempData(
                        minTemp = minTemp,
                        maxTemp = maxTemp,
                        currentLow = currentLow,
                        currentHigh = currentHigh,
                        shouldShowCurrentTemp = index == 0,
                        currentTemp = currentTemp
                    )
                )
                if (index != dailyList.lastIndex) {
                    HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                }
            }
        }
    }
}

@Composable
internal fun DailyItem(modifier: Modifier = Modifier, daily: DailyPreview, tempData: TempData) {
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
                .padding(start = 8.dp)
                .weight(1.5f),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${daily.tempNight.toFloat().roundToInt()}°",
                modifier = Modifier.weight(1.0f),
                textAlign = TextAlign.Start,
                fontSize = 14.sp,
            )
            TempBar(tempData = tempData)
            Text(
                text = "${daily.tempDay.toFloat().roundToInt()}°",
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
        DailyWidget(
            dailyList = DailyPreviewStaticData,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            currentTemp = 0
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DailyItemPreview() {
    WeatherTheme {
        DailyItem(
            daily = DailyPreviewStaticData[0],
            tempData = TempData(
                minTemp = 0,
                maxTemp = 0,
                currentLow = 0,
                currentHigh = 0,
                shouldShowCurrentTemp = false,
                currentTemp = 0
            )
        )
    }
}