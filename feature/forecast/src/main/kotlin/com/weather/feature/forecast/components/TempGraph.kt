package com.weather.feature.forecast.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.weather.core.design.theme.WeatherTheme
import com.weather.feature.forecast.components.hourlydata.HourlyStaticData
import com.weather.model.Hourly
import kotlin.math.roundToInt

@Composable
fun TempPoints(modifier: Modifier = Modifier) {
    val pointsColor = MaterialTheme.colorScheme.onSurface
    Spacer(modifier = modifier
        .fillMaxWidth()
        .drawWithCache {
            val center = Offset(x = 0.0f, y = 0.0f)
            onDrawBehind {
                drawCircle(color = pointsColor, radius = 10f, center = center)
            }
        })
}

@Composable
fun HourlyGraph(modifier: Modifier = Modifier, data: List<Hourly>) {
    val textColor = MaterialTheme.colorScheme.onSurface
    val textMeasurer = rememberTextMeasurer()
//    val a = rememberVectorPainter(image = Icons.Outlined.Radio) //test
    Spacer(modifier = modifier then Modifier
        .padding(start = 0.dp, bottom = 0.dp)
        .fillMaxWidth()
        .drawWithCache {
            val width = size.width
            val height = size.height
            val dataSize = data.size
            val minTemp = data.minBy { it.temp }.temp
            val maxTemp = data.maxBy { it.temp }.temp
            val tempRange = (maxTemp - minTemp).toFloat()
            val topOffset = 20.dp.toPx()
            val path = Path()
            var previousTemp = height
            onDrawBehind {
//                with(a){
//                    draw(a.intrinsicSize) //test
//                }
                //horizontal line
                drawLine(
                    color = textColor,
                    start = Offset(x = 0f, topOffset),
                    end = Offset(x = width, y = topOffset),
                    pathEffect = PathEffect.dashPathEffect(
                        floatArrayOf(10f, 10f),
                        phase = 0f
                    ),
                )
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
                        color = textColor,
                        topLeft = Offset(xPerIndex - 15, y - 70)
                    )
                    drawCircle(Color.Black, radius = 10f, center = Offset(xPerIndex, y))
                    drawLine(
                        color = Color.Red.copy(alpha = 0.5f),
                        start = Offset(x = xPerIndex, y),
                        end = Offset(x = xPerIndex, y = y),
                        strokeWidth = 3f,
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(10f, 10f),
                            phase = 0f
                        ),
                    )
                    if (index == 0) {
//                        path.reset()
                        path.moveTo(0f, y)
                        //draw vertical on first point
                        drawLine(
                            color = Color.Red.copy(alpha = 0.5f),
                            start = Offset(x = 0f, y),
                            end = Offset(x = 0f, y = y + 50.dp.toPx()),
                            strokeWidth = 3f,
                            pathEffect = PathEffect.dashPathEffect(
                                floatArrayOf(10f, 10f),
                                phase = 0f
                            ),
                        )
                    } else {
//                        path.cubicTo(
//                            x1 = controlPoints1.x,
//                            y1 = controlPoints1.y,
//                            x2 = controlPoints2.x,
//                            y2 = controlPoints2.y,
//                            x3 = xPerIndex,
//                            y3 = y
//                        )
                        path.lineTo(
                            x = xPerIndex,
                            y = y
                        )
                        //vertical lines
//                        drawLine(
//                            color = Color.Red.copy(alpha = 0.5f),
//                            start = Offset(x = xPerIndex, y),
//                            end = Offset(x = xPerIndex, y = y + height),
//                            strokeWidth = 3f,
//                            pathEffect = PathEffect.dashPathEffect(
//                                floatArrayOf(10f, 10f),
//                                phase = 0f
//                            ),
//                        )
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

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
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
        LineChart(modifier = Modifier.aspectRatio(16 / 9f), lineChartData = linechart)
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
