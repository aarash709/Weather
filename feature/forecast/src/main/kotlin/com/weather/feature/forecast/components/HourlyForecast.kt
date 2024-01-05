package com.weather.feature.forecast.components

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import coil.compose.AsyncImage
import com.weather.core.design.theme.WeatherTheme
import com.weather.model.Hourly
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
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
//            HourlyGraph(data = data)
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
        var timestampTotalWidth = 0

        val timestampPlaceable = timestampsMeasurable.map { measurable ->
            val timeStampPlaceable = measurable.measure(constraints)
            timestampTotalWidth += timeStampPlaceable.width
            timeStampPlaceable
        }

        val dailyGraphPlaceable = tempGraphMeasurable
            .map { measurable ->
                val height = graphHeight.toPx().toInt()
                val graphPlaceable = measurable.measure(
                    constraints
                        .copy(
                            minWidth = timestampTotalWidth,
                            maxWidth = timestampTotalWidth,
                            minHeight = height,
                            maxHeight = height,
                        )
                )
                graphPlaceable
            }

        val height = dailyGraphPlaceable.first().height + timestampPlaceable.maxOf { it.height }
        layout(width = timestampTotalWidth + 100, height = height) {
            val horizontalOffset = 16.dp.toPx().toInt()

            dailyGraphPlaceable.first().place(x = 0, y = 0)

            timestampPlaceable.forEachIndexed { index, placeable ->
                if (index == 0) {
                    placeable.place(
                        x = placeable.width.times(index),
                        y = dailyGraphPlaceable.maxOf { it.height })
                } else {
                    placeable.place(
                        x = placeable.width.times(index),
                        y = dailyGraphPlaceable.maxOf { it.height }
                    )
                }
            }
        }
    }
}

@Composable
fun HourlyGraph(modifier: Modifier = Modifier, data: List<Hourly>) {
    val textColor = MaterialTheme.colorScheme.background
    val textMeasurer = rememberTextMeasurer()
    Spacer(modifier = modifier then Modifier
//        .background(color = MaterialTheme.colorScheme.background)
        .padding(start = 0.dp, bottom = 0.dp)
//        .height(50.dp)
        .fillMaxWidth()
//        .aspectRatio(16 / 9f)
        .drawWithCache {
            val width = size.width
            val height = size.height
            val dataSize = data.size
            val minTemp = data.minBy { it.temp }.temp
            val maxTemp = data.maxBy { it.temp }.temp
            val tempRange = (maxTemp - minTemp).toFloat()
            val topOffset = 16.dp.toPx()
            val path = Path()
            var previousTemp = height
            onDrawBehind {
                data.forEachIndexed { index, hourly ->
                    val temp = hourly.temp.toFloat()
                    val y = height - ((temp - minTemp) / tempRange)
                        .times(height.minus(topOffset))
                        .toFloat()
                    val x = width / (dataSize - 1)
                    val xPerIndex = x * (index)
                    val controlPoints1 = Offset(xPerIndex.minus(x / 2), previousTemp)
                    val controlPoints2 = Offset(xPerIndex.minus(x / 2), y)

                    drawText(
                        textMeasurer.measure("${temp.roundToInt()}"),
                        topLeft = Offset(xPerIndex - 15, y - 70)
                    )
                    if (index == 0) {
//                        path.reset()
                        path.moveTo(0f, y)
                    } else {
                        path.cubicTo(
                            x1 = controlPoints1.x,
                            y1 = controlPoints1.y,
                            x2 = controlPoints2.x,
                            y2 = controlPoints2.y,
                            x3 = xPerIndex,
                            y3 = y
                        )
//                        path.lineTo(
//                            x = xPerIndex,
//                            y = y
//                        )
                        drawLine(
                            color = Color.White,
                            start = Offset(x = 0f, topOffset),
                            end = Offset(x = width, y = topOffset),
                            pathEffect = PathEffect.dashPathEffect(
                                floatArrayOf(10f, 10f),
                                phase = 0f
                            ),
                        )
                        drawCircle(Color.Black, radius = 10f, center = Offset(xPerIndex, y))
                        drawPath(
                            path = path,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Red,
                                    Color.Yellow,
                                    Color.Green,
                                )
                            ),
                            style = Stroke(width = 5f)
                        )
                        previousTemp = y
                    }
                }
            }
        })
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

@Preview(uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun HourlyCustomLayoutPreview() {
    WeatherTheme {
        val scrollState = rememberScrollState()
        HourlyWidgetWithGraph(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(21 / 9f)
                .border(width = 2.dp, Color.Red)
                .background(MaterialTheme.colorScheme.surface)
                .horizontalScroll(scrollState),
            itemCount = HourlyStaticData.size,
            graphHeight = 50.dp,
            hourlyGraph = { HourlyGraph(data = HourlyStaticData) },
            hourlyTimeStamps = { index ->
                val timeStamp = HourlyStaticData[index].dt
                Text(text = timeStamp)
            },
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun Ychart() {
    val sampleData = List(6) { it }
    val points = HourlyStaticData.mapIndexed { index, hourly ->
        Point(index.toFloat(), hourly.temp.toFloat())
    }
//    val points = sampleData.mapIndexed { index, number ->
//        Point(number.toFloat(), index.toFloat())
//    }
    val steps = HourlyStaticData.size
    WeatherTheme {
        val xAxisData = AxisData.Builder()
            .axisStepSize(100.dp)
            .steps(HourlyStaticData.size - 1)
            .labelData { i -> i.toString() }
            .labelAndAxisLinePadding(15.dp)
            .backgroundColor(MaterialTheme.colorScheme.surface)
            .axisLabelColor(MaterialTheme.colorScheme.onSurface)
            .build()

        val yAxisData = AxisData.Builder()
            .steps(HourlyStaticData.maxOf { it.temp }.toInt())
            .labelAndAxisLinePadding(20.dp)
            .axisLineColor(MaterialTheme.colorScheme.onSurface)
            .axisLabelColor(MaterialTheme.colorScheme.onSurface)
            .backgroundColor(MaterialTheme.colorScheme.surface)
//            .labelData { i ->
//                (i).toString()
//            }
            .build()
        val linechart = LineChartData(
            linePlotData = LinePlotData(
                lines = listOf(
                    Line(
                        dataPoints = points,
                        lineStyle = LineStyle(color = MaterialTheme.colorScheme.onSurface),
                        intersectionPoint = IntersectionPoint(color = MaterialTheme.colorScheme.onSurface),
                        selectionHighlightPoint = SelectionHighlightPoint(),
                        shadowUnderLine = ShadowUnderLine(),
                        selectionHighlightPopUp = SelectionHighlightPopUp()
                    )
                ),
            ),
            xAxisData = xAxisData,
            yAxisData = yAxisData,
//        gridLines = GridLines(),
            backgroundColor = MaterialTheme.colorScheme.surface
        )
//        LineChart(modifier = Modifier.aspectRatio(16 / 9f), lineChartData = linechart)
    }
}

@Preview
@Composable
private fun HourlyGraphPreview() {
    HourlyGraph(
        modifier = Modifier,
        data = HourlyStaticData
    )
}

@Preview
@Composable
private fun HourlyWithGraphTestPreview() {
    WeatherTheme {
        HourlyForecast(data = HourlyStaticData)
    }
}

@Preview(showBackground = false, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = false, uiMode = UI_MODE_NIGHT_NO)
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
//            HourlyItem(item = HourlyStaticData[0])
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
        temp = 9.0,
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
        dt = "16:00",
        feels_like = 0.0,
        humidity = 1,
        pop = 0.0,
        pressure = 0,
        temp = 12.0,
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
        temp = 15.0,
        uvi = 0.0,
        visibility = 100,
        description = "description",
        icon = "icon",
        id = 100,
        main = "main",
        wind_deg = 10,
        wind_gust = 0.0,
        wind_speed = 0.0
    ), Hourly(
        clouds = 0,
        dew_point = 0.0,
        dt = "18:00",
        feels_like = 0.0,
        humidity = 1,
        pop = 0.0,
        pressure = 0,
        temp = 15.0,
        uvi = 0.0,
        visibility = 100,
        description = "description",
        icon = "icon",
        id = 100,
        main = "main",
        wind_deg = 10,
        wind_gust = 0.0,
        wind_speed = 0.0
    ), Hourly(
        clouds = 0,
        dew_point = 0.0,
        dt = "19:00",
        feels_like = 0.0,
        humidity = 1,
        pop = 0.0,
        pressure = 0,
        temp = 16.0,
        uvi = 0.0,
        visibility = 100,
        description = "description",
        icon = "icon",
        id = 100,
        main = "main",
        wind_deg = 10,
        wind_gust = 0.0,
        wind_speed = 0.0
    ), Hourly(
        clouds = 0,
        dew_point = 0.0,
        dt = "20:00",
        feels_like = 0.0,
        humidity = 1,
        pop = 0.0,
        pressure = 0,
        temp = 15.0,
        uvi = 0.0,
        visibility = 100,
        description = "description",
        icon = "icon",
        id = 100,
        main = "main",
        wind_deg = 10,
        wind_gust = 0.0,
        wind_speed = 0.0
    ), Hourly(
        clouds = 0,
        dew_point = 0.0,
        dt = "21:00",
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
    ), Hourly(
        clouds = 0,
        dew_point = 0.0,
        dt = "22:00",
        feels_like = 0.0,
        humidity = 1,
        pop = 0.0,
        pressure = 0,
        temp = 12.0,
        uvi = 0.0,
        visibility = 100,
        description = "description",
        icon = "icon",
        id = 100,
        main = "main",
        wind_deg = 10,
        wind_gust = 0.0,
        wind_speed = 0.0
    ), Hourly(
        clouds = 0,
        dew_point = 0.0,
        dt = "23:00",
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
    )
)