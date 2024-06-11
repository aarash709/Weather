package com.weather.feature.forecast.widgets

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.experiment.weather.core.common.R.*
import com.weather.core.design.theme.WeatherTheme
import com.weather.feature.forecast.R
import com.weather.feature.forecast.components.HourlyTemperatureGraph
import com.weather.feature.forecast.components.hourlydata.HourlyStaticData
import com.weather.model.Hourly

@Composable
internal fun HourlyWidget(
    modifier: Modifier = Modifier,
    hourly: List<Hourly>,
    speedUnit: String,
    surfaceColor: Color = Color.Black.copy(alpha = 0.10f),
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = surfaceColor,
    ) {
        val paleOnSurfaceColor = LocalContentColor.current.copy(alpha = 0.6f)
        Column(modifier = Modifier.padding(vertical = 16.dp)) {
            Row(
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(15.dp)
                        .background(paleOnSurfaceColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccessTime,
                        modifier = Modifier.padding(3.dp),
                        tint = paleOnSurfaceColor,
                        contentDescription = null
                    )
                }
                Text(
                    text = stringResource(id = string.hourly_forecast),
                    modifier = Modifier,
                    fontSize = 14.sp,
                    color = paleOnSurfaceColor
                )
            }
            HourlyGraphLayout(
                modifier = Modifier
                    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                    .horizontalScroll(rememberScrollState()),
                itemCount = hourly.size,
                graphHeight = 50.dp,
                hourlyGraph = {
                    HourlyTemperatureGraph(data = hourly)
                },
                hourlyTimeStamps = {
                    val time = hourly[it].time
                    val icon = hourly[it].icon
                    val windSpeed = hourly[it].wind_speed.toString()
                    Column(
                        modifier = Modifier
                            .width(80.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        AsyncImage(
                            model = "https://openweathermap.org/img/wn/${icon}@2x.png",
                            contentDescription = ""
                        )
                        if (hourly[it].sunriseSunset.isNotEmpty()) {
                            Text(
                                text = hourly[it].sunriseSunset,
                                fontSize = 12.sp,
                            )
                        } else {
                            Text(
                                text = "$windSpeed$speedUnit",
                                fontSize = 12.sp,
                                color = paleOnSurfaceColor
                            )
                        }
                        Text(
                            text = time,
                            fontSize = 12.sp,
                            color = paleOnSurfaceColor
                        )
                    }
                })
        }
    }
}


@Composable
internal fun HourlyGraphLayout(
    modifier: Modifier = Modifier,
    itemCount: Int,
    graphHeight: Dp,
    hourlyGraph: @Composable () -> Unit,
    hourlyTimeStamps: @Composable (index: Int) -> Unit,
) {
    val timeStamps = @Composable { repeat(itemCount) { hourlyTimeStamps(it) } }
    val tempGraph = @Composable { hourlyGraph() }
    Layout(
        contents = listOf(tempGraph, timeStamps),
        modifier = modifier
    ) {
            (tempGraphMeasurable, timestampsMeasurable),
            constraints,
        ->
        var totalWidth = 0
        var graphStartXOffset = 0 //x axis start offset placing
        var firstTimeStampHalfWidth = 0
        var lastTimeStampHalfWidth = 0
        val timestampPlaceable = timestampsMeasurable.mapIndexed { index, measurable ->
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
//@LayoutScopeMarker
//@Immutable
//object HourlyGraphScope {
//    @Stable
//    fun Modifier.timeGraphBar(
//        start: LocalDateTime,
//        end: LocalDateTime,
//        hours: List<Int>,
//    ): Modifier {
//        val earliestTime = LocalTime.of(hours.first(), 0)
//        val durationInHours = ChronoUnit.MINUTES.between(start, end) / 60f
//        val durationFromEarliestToStartInHours =
//            ChronoUnit.MINUTES.between(earliestTime, start.toLocalTime()) / 60f
//        // we add extra half of an hour as hour label text is visually centered in its slot
//        val offsetInHours = durationFromEarliestToStartInHours + 0.5f
//        return then(
//            HourlyGraphParentData(
//                duration = durationInHours / hours.size,
//                offset = offsetInHours / hours.size
//            )
//        )
//    }
//}
//
//class HourlyGraphParentData(
//    val duration: Float,
//    val offset: Float,
//) : ParentDataModifier {
//    override fun Density.modifyParentData(parentData: Any?) = this@HourlyGraphParentData
//}
//

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun HourlyWidgetPreview() {
    WeatherTheme {
//        preview wont size correctly when using 'Asyncimage' composable
        HourlyWidget(
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