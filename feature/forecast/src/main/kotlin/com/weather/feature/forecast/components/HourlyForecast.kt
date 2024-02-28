package com.weather.feature.forecast.components

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.weather.core.design.theme.WeatherTheme
import com.weather.feature.forecast.components.hourlydata.HourlyStaticData
import com.weather.model.Hourly
import kotlin.math.roundToInt


@Composable
internal fun HourlyForecast(
    modifier: Modifier = Modifier,
    data: List<Hourly>,
    surfaceColor: Color = Color.White.copy(alpha = 0.15f),
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = surfaceColor,
    ) {
        Column {
            Text(
                text = "Today",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                color = LocalContentColor.current.copy(alpha = 0.5f)
            )
            LazyRow(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                items(data) { hourly ->
                    HourlyItem(modifier = Modifier, hourly)
                }
            }
        }
    }
}

@Composable
internal fun HourlyItem(
    modifier: Modifier = Modifier,
    item: Hourly,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = item.time)
        }
        AsyncImage(
            model = "https://openweathermap.org/img/wn/${item.icon}@2x.png",
            contentDescription = "Weather Icon",
            modifier = Modifier
        )
        Text(
            text = "${item.temp.toFloat().roundToInt()}°",
            color = LocalContentColor.current.copy(alpha = 0.5f)
        )
    }
}

@Preview(showBackground = false, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = false, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun HourlyPreview() {
    WeatherTheme {
        HourlyForecast(data = HourlyStaticData)
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun HourlyItemPreview() {
    WeatherTheme {
        Surface {
//            HourlyItem(item = HourlyStaticData[0])
        }
    }
}