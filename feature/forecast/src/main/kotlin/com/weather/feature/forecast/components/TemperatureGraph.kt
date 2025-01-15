package com.weather.feature.forecast.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weather.feature.forecast.hourlydata.HourlyStaticData
import com.weather.feature.forecast.tempColor
import com.weather.model.Hourly
import com.weather.model.TemperatureUnits
import kotlin.math.roundToInt

private fun calculateGraphBrush(
    tempUnit: TemperatureUnits,
    minTemp: Int,
    maxTemp: Int,
    startYOffset: Float = 0f,
): Brush {
    val minTempColor = tempColor(temp = minTemp, tempUnit = tempUnit)
    val maxTempColor = tempColor(temp = maxTemp, tempUnit = tempUnit)
    return Brush.verticalGradient(
        colors = listOf(maxTempColor, minTempColor),
        startY = startYOffset
    )
}

@Composable
internal fun HourlyTemperatureGraph(
    modifier: Modifier = Modifier,
    data: List<Hourly>,
    temperatureUnits: TemperatureUnits,
) {
    val contentColor = LocalContentColor.current
    val textMeasurer = rememberTextMeasurer()

    Spacer(modifier = Modifier
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

            onDrawBehind {
                var previousTemp = height
                var firstIndexOffset = Offset.Zero
                data.forEachIndexed { index, hourly ->
                    val temp = hourly.temp.toFloat()

                    //normalize temp then calculate the height and subtract the offset
                    //for text elements
                    val y = height - ((temp - minTemp) / tempRange)
                        .times(height.minus(topOffset))
                        .toFloat()
                    val x = width / (dataSize - 1)
                    val xPerIndex = x * index
                    val controlPoints1 = Offset(xPerIndex.minus(x / 2), previousTemp)
                    val controlPoints2 = Offset(xPerIndex.minus(x / 2), y)

                    //temp text
                    val textLayoutResult = textMeasurer.measure(
                        "${temp.roundToInt()}°",
                        style = TextStyle(fontSize = 14.sp)
                    )
                    val textYOffset = 5.dp.toPx()
                    drawText(
                        textLayoutResult = textLayoutResult,
                        color = contentColor,
                        topLeft = Offset(
                            x = xPerIndex - textLayoutResult.size.width / 2f,
                            y = (y - textLayoutResult.size.height).minus(textYOffset)
                        )
                    )

                    //first temp vertical dashed white line
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
                        previousTemp = y //fixes controlPoint1.y skip first index set
                        path.moveTo(0f, y)
                        path.lineTo(-130f, y = y) //start infinite line
                        path.moveTo(0f, y)
                        //draw vertical on first point
                        drawLine(
                            color = contentColor,
                            start = Offset(x = 0f, y),
                            end = Offset(x = 0f, y = y + (height - y).plus(15f)),
                            strokeWidth = 3f,
                            pathEffect = PathEffect.dashPathEffect(
                                floatArrayOf(6f, 7f),
                                phase = 0f
                            ),
                        )
                        firstIndexOffset = Offset(0f, y)
                    } else {
                        previousTemp = y
                        path.cubicTo(
                            x1 = controlPoints1.x,
                            y1 = controlPoints1.y,
                            x2 = controlPoints2.x,
                            y2 = controlPoints2.y,
                            x3 = xPerIndex,
                            y3 = y
                        )
                    }
                    if (index == data.lastIndex) {
                        path.lineTo(xPerIndex + 130, y = y) //continue the path line at the end
                    }
                }
                val graphBrush = calculateGraphBrush(
                    tempUnit = temperatureUnits,
                    minTemp = data
                        .minOf { it.temp }
                        .toInt(),
                    maxTemp = data
                        .maxOf { it.temp }
                        .toInt(),
                    startYOffset = topOffset
                )
                drawPath(
                    path = path,
                    brush = graphBrush,
                    style = Stroke(width = 5f),
                )
                //first index circle
                drawCircle(
                    color = contentColor,
                    radius = 10f,
                    center = firstIndexOffset,
                )
            }
        } then modifier)
}

@Preview
@Composable
private fun HourlyGraphPreview() {
    HourlyTemperatureGraph(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
            .aspectRatio(16 / 9f),
        data = HourlyStaticData,
        temperatureUnits = TemperatureUnits.C
    )
}
