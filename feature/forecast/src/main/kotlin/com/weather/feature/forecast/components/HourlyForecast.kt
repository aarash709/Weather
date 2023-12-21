package com.weather.feature.forecast.components

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.weather.core.design.theme.WeatherTheme
import com.weather.model.Hourly
import kotlin.math.roundToInt


@Composable
fun HourlyForecast(
    modifier: Modifier = Modifier,
    data: List<Hourly>,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
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
fun HourlyItem(
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
            Text(text = item.dt)
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


@Composable
fun HourlyGraph() {
    Spacer(modifier = Modifier
        .aspectRatio(16 / 9f)
        .drawWithCache {
            val width = size.width
            val height = size.height
            val data = HourlyStaticData
            val dataSize = data.size
            val min = data.minBy { it.temp }.temp
            val max = data.maxBy { it.temp }.temp
            val rangeSteps = (max - min).toFloat()
            val maxRelativeHeight = size.height.div(data.size)
            val path = Path()
            onDrawBehind {
                drawRect(color = Color.Gray, style = Stroke(width = 5f))
                data.forEachIndexed { index, hourly ->
                    val temp = hourly.temp.toFloat()
                    val y = height - ((temp - min) / rangeSteps)
                        .times(height)
                        .toFloat()
                    val x = (size.width / data.size)
                    if (index == 0) {
                        path.moveTo(
                            x = 0f,
                            y = y
                        )
                    }
                    path.lineTo(x = x * (index), y = y)
                    drawCircle(
                        color = Color.Red   ,
                        radius = 15f,
                        center = Offset(x = x * index, y = y)
                    )
                    this.drawContext.canvas.nativeCanvas.drawText(
                        "$index",
                        x * index,
                        height - 10,
                        android.graphics
                            .Paint()
                            .apply {
                                color = android.graphics.Color.WHITE
                            }
                    )
                    this.drawContext.canvas.nativeCanvas.drawText(
                        "$temp",
                        5f,
                        height - (height / dataSize)
                            .times(index + 1)
                            .minus(11f),
                        android.graphics
                            .Paint()
                            .apply {
                                color = android.graphics.Color.WHITE
                            }
                    )
                }

                drawPath(
                    path = path,
                    color = Color.White,
                    style = Stroke(width = 10f)
                )
            }
        })
}

@Preview
@Composable
private fun HourlyGraphPreview() {
    HourlyGraph()
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun HourlyPreview() {
    WeatherTheme {
        HourlyForecast(data = HourlyStaticData)
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun HourlyItemPreview() {
    WeatherTheme {
        Surface {
            HourlyItem(item = HourlyStaticData[0])
        }
    }
}

val HourlyStaticData = listOf(
    Hourly(
        clouds = 0,
        dew_point = 0.0,
        dt = "13:00",
        feels_like = 0.0,
        humidity = 1,
        pop = 0.0,
        pressure = 0,
        temp = 8.0,
        uvi = 0.0,
        visibility = 100,
        description = "description",
        icon = "icon",
        id = 100,
        main = "main",
        wind_deg = 10,
        wind_gust = 0.0,
        wind_speed = 0.0
    ),
    Hourly(
        clouds = 0,
        dew_point = 0.0,
        dt = "14:00",
        feels_like = 0.0,
        humidity = 1,
        pop = 0.0,
        pressure = 0,
        temp = 11.0,
        uvi = 0.0,
        visibility = 100,
        description = "description",
        icon = "icon",
        id = 100,
        main = "main",
        wind_deg = 10,
        wind_gust = 0.0,
        wind_speed = 0.0
    ),
    Hourly(
        clouds = 0,
        dew_point = 0.0,
        dt = "15:00",
        feels_like = 0.0,
        humidity = 1,
        pop = 0.0,
        pressure = 0,
        temp = 10.0,
        uvi = 0.0,
        visibility = 100,
        description = "description",
        icon = "icon",
        id = 100,
        main = "main",
        wind_deg = 10,
        wind_gust = 0.0,
        wind_speed = 0.0
    ),
    Hourly(
        clouds = 0,
        dew_point = 0.0,
        dt = "16:00",
        feels_like = 0.0,
        humidity = 1,
        pop = 0.0,
        pressure = 0,
        temp = 14.0,
        uvi = 0.0,
        visibility = 100,
        description = "description",
        icon = "icon",
        id = 100,
        main = "main",
        wind_deg = 10,
        wind_gust = 0.0,
        wind_speed = 0.0
    ),
    Hourly(
        clouds = 0,
        dew_point = 0.0,
        dt = "17:00",
        feels_like = 0.0,
        humidity = 1,
        pop = 0.0,
        pressure = 0,
        temp = 17.0,
        uvi = 0.0,
        visibility = 100,
        description = "description",
        icon = "icon",
        id = 100,
        main = "main",
        wind_deg = 10,
        wind_gust = 0.0,
        wind_speed = 0.0
    )
)