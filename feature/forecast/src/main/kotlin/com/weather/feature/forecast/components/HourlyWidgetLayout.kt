package com.weather.feature.forecast.components

import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

@Composable
fun HourlyWidgetWithGraph(
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
                            minWidth = width+25,
                            maxWidth = width+25,
                            minHeight = height,
                            maxHeight = height,
                        )
                )
                graphPlaceable
            }

        val height = dailyGraphPlaceable.first().height + timestampPlaceable.maxOf { it.height }
        layout(width = totalWidth + 200, height = height) {
            val horizontalOffset = 16.dp.toPx().toInt()

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