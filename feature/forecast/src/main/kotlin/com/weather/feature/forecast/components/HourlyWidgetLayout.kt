package com.weather.feature.forecast.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.weather.core.design.theme.WeatherTheme
import com.weather.feature.forecast.components.hourlydata.HourlyStaticData
import com.weather.model.Hourly
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

@Composable
fun HourlyWidgetWithGraph(
    modifier: Modifier = Modifier,
    hourly: List<Hourly>,
    speedUnit : String,
    surfaceColor: Color = Color.White.copy(alpha = 0.15f)) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = surfaceColor,
    ) {
        Column(modifier = Modifier.padding(vertical = 16.dp)) {
//            Text(
//                text = "Hourly",
//                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
//                color = LocalContentColor.current.copy(alpha = 0.5f)
//            )
            HourlyGraphLayout(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState()),
                itemCount = hourly.size,
                graphHeight = 50.dp,
                hourlyGraph = {
                    HourlyTemperatureGraph(data = hourly)
                },
                hourlyTimeStamps = {
                    val timeStamp = hourly[it].time
                    val icon = hourly[it].icon
                    val windSpeed = hourly[it].wind_speed.toString()
                    Column(
                        modifier = Modifier
                            .width(80.dp)
                            .padding(horizontal = 0.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        AsyncImage(
                            model = "https://openweathermap.org/img/wn/${icon}@2x.png",
                            contentDescription = ""
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "$windSpeed$speedUnit", fontSize = 12.sp, color = Color.White.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = timeStamp, fontSize = 12.sp, color = Color.White.copy(alpha = 0.5f))
                    }
                },
            )
        }
    }
}


@Composable
fun HourlyGraphLayout(
    modifier: Modifier = Modifier,
    itemCount: Int,
    graphHeight: Dp,
    hourlyGraph: @Composable HourlyGraphScope.() -> Unit,
    hourlyTimeStamps: @Composable (index: Int) -> Unit,
) {
    val timeStamps = @Composable { repeat(itemCount) { hourlyTimeStamps(it) } }
    val tempGraph = @Composable { HourlyGraphScope.hourlyGraph() }
    Layout(
        contents = listOf(tempGraph, timeStamps),
        modifier = modifier
    ) {
            (tempGraphMeasurable, timestampsMeasurable),
            constraints,
        ->
        val topOffset = 16.dp.toPx().roundToInt()
        var totalWidth = 0
        var graphStartXOffset = 0 //x axis start offset placing
        var firstTimeStampHalfWidth = 0
        var lastTimeStampHalfWidth = 0
        val timestampPlaceable = timestampsMeasurable.mapIndexed() { index, measurable ->
            val timePlaceable = measurable.measure(constraints)
            if (index == 0) {
                firstTimeStampHalfWidth = timePlaceable.width / 2
                graphStartXOffset = firstTimeStampHalfWidth
            }
            if (index == timestampsMeasurable.lastIndex) {
                lastTimeStampHalfWidth = timePlaceable.width / 2
            }
            totalWidth += timePlaceable.width
            timePlaceable
        }

        val dailyGraphPlaceable = tempGraphMeasurable
            .map { measurable ->
                val height = graphHeight.toPx().toInt()
                val width = totalWidth.minus(firstTimeStampHalfWidth + lastTimeStampHalfWidth)
                val graphPlaceable = measurable.measure(
                    constraints
                        .copy(
                            minWidth = width,
                            maxWidth = width,
                            minHeight = height,
                            maxHeight = height,
                        )
                )
                graphPlaceable
            }

        val height = dailyGraphPlaceable.first().height + timestampPlaceable.maxOf { it.height }
        layout(width = totalWidth, height = height) {
            timestampPlaceable.forEachIndexed { index, placeable ->
                if (index == 0) {
                    graphStartXOffset = placeable.width / 2
                }
                placeable.place(
                    x = placeable.width.times(index),
                    y = dailyGraphPlaceable.maxOf { it.height })
            }
            dailyGraphPlaceable.first().place(x = graphStartXOffset, y = 0)
        }
    }
}


//pass required data to the parent modifier to calculate graphs and drawScope
@LayoutScopeMarker
@Immutable
object HourlyGraphScope {
    @Stable
    fun Modifier.timeGraphBar(
        start: LocalDateTime,
        end: LocalDateTime,
        hours: List<Int>,
    ): Modifier {
        val earliestTime = LocalTime.of(hours.first(), 0)
        val durationInHours = ChronoUnit.MINUTES.between(start, end) / 60f
        val durationFromEarliestToStartInHours =
            ChronoUnit.MINUTES.between(earliestTime, start.toLocalTime()) / 60f
        // we add extra half of an hour as hour label text is visually centered in its slot
        val offsetInHours = durationFromEarliestToStartInHours + 0.5f
        return then(
            HourlyGraphParentData(
                duration = durationInHours / hours.size,
                offset = offsetInHours / hours.size
            )
        )
    }
}

class HourlyGraphParentData(
    val duration: Float,
    val offset: Float,
) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) = this@HourlyGraphParentData
}
//

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun HourlyWidgetPreview() {
    WeatherTheme {
//        preview wont size correctly when using 'Asyncimage' composable
        val scrollState = rememberScrollState()
        HourlyWidgetWithGraph(
            modifier = Modifier,
            HourlyStaticData,
            "km/h"
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun HourlyCustomLayoutPreview() {
    WeatherTheme {
        val scrollState = rememberScrollState()
        HourlyGraphLayout(
            modifier = Modifier
//                .fillMaxWidth()
//                .border(width = 2.dp, Color.Red)
                .background(MaterialTheme.colorScheme.surface),
            itemCount = HourlyStaticData.size,
            graphHeight = 50.dp,
            hourlyGraph = { HourlyTemperatureGraph(data = HourlyStaticData) },
            hourlyTimeStamps = { index ->
                val timeStamp = HourlyStaticData[index].time
                Column {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = ""
                    )
                    Text(text = timeStamp)
                }
            },
        )
    }
}