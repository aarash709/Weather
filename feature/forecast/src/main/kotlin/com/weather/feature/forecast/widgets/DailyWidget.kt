package com.weather.feature.forecast.widgets

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.experiment.weather.core.common.R.string
import com.weather.core.design.modifiers.bouncyTapEffect
import com.weather.core.design.theme.WeatherTheme
import com.weather.feature.forecast.components.TempBar
import com.weather.feature.forecast.components.TempData
import com.weather.feature.forecast.hourlydata.DailyPreviewStaticData
import com.weather.model.Daily
import com.weather.model.TemperatureUnits
import kotlin.math.roundToInt

@Composable
internal fun DailyWidget(
    modifier: Modifier = Modifier,
    dailyList: List<Daily>,
    currentTemp: Int,
    tempUnit: TemperatureUnits,
    surfaceColor: Color,
) {
    Surface(
        modifier = Modifier.bouncyTapEffect() then modifier,
        shape = RoundedCornerShape(16.dp),
        color = surfaceColor,
    ) {
        val paleOnSurfaceColor = LocalContentColor.current
        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DailyTitle(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                contentColor = paleOnSurfaceColor
            )
            val minTemp by remember(dailyList) {
                mutableIntStateOf(dailyList.minOf { it.nightTemp.roundToInt() })
            }
            val maxTemp by remember(dailyList) {
                mutableIntStateOf(dailyList.maxOf { it.dayTemp.roundToInt() })
            }
            dailyList.forEachIndexed { index, daily ->
                val currentLow = daily.dayTemp.roundToInt()
                val currentHigh = daily.nightTemp.roundToInt()
                DailyItem(
                    modifier = Modifier
                        .fillMaxWidth(),
                    daily = daily,
                    tempData = TempData(
                        tempUnit = tempUnit,
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
private fun DailyTitle(modifier: Modifier = Modifier, contentColor: Color) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.CalendarMonth,
                tint = contentColor,
                contentDescription = null
            )
            Text(
                text = stringResource(id = string.five_day_forecast),
                fontSize = 14.sp,
                color = contentColor
            )
        }
    }
}

@Composable
private fun DailyItem(modifier: Modifier = Modifier, daily: Daily, tempData: TempData) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = daily.time, modifier = Modifier
                    .align(Alignment.CenterVertically)
            )
            AsyncImage(
                model = daily.iconUrl,
                modifier = Modifier.padding(end = 16.dp),
                contentDescription = "WeatherIcon",
            )
        }
        Row(
            modifier = Modifier
                .padding(start = 0.dp)
                .weight(1.25f),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${daily.nightTemp.roundToInt()}°",
                modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
            )
            TempBar(tempData = tempData, modifier = Modifier.weight(1.5f))
            Text(
                text = "${daily.dayTemp.roundToInt()}°",
                modifier = Modifier.weight(1f).padding(start = 16.dp),
                textAlign = TextAlign.Center,
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
        val color = MaterialTheme.colorScheme.background
        DailyWidget(
            dailyList = DailyPreviewStaticData,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            currentTemp = 0,
            surfaceColor = color,
            tempUnit = TemperatureUnits.C
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
                tempUnit = TemperatureUnits.C,
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