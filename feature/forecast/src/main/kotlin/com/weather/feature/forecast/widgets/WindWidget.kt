package com.weather.feature.forecast.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material.icons.outlined.ArrowDropUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.experiment.weather.core.common.R.array
import com.experiment.weather.core.common.R.string
import com.weather.core.design.components.WeatherSquareWidget
import kotlin.math.cos
import kotlin.math.sin

@Composable
internal fun WindWidget(
    modifier: Modifier = Modifier,
    windDirection: Int,
    windSpeed: Int,
    speedUnits: String,
) {
    val context = LocalContext.current
    val direction by remember(windDirection) {
        val directions = context.resources.getStringArray(array.wind_directions)
        val value = when (windDirection) {
            in 341..360 -> directions[0]
            in 0..20 -> directions[0]
            in 21..70 -> directions[1]
            in 71..110 -> directions[2]
            in 111..160 -> directions[3]
            in 161..200 -> directions[4]
            in 201..240 -> directions[5]
            in 241..290 -> directions[6]
            in 291..340 -> directions[7]
            else -> directions[8]
        }
        mutableStateOf(value)
    }
    WeatherSquareWidget(
        modifier,
        icon = Icons.Outlined.Air,
        title = stringResource(id = string.wind_from),
        infoText = direction
    ) {
        WindDirectionGraph(
            windDirection = windDirection,
            windSpeed = windSpeed,
            speedUnits = speedUnits
        )
    }
}

@Composable
internal fun WindDirectionGraph(
    modifier: Modifier = Modifier,
    windDirection: Int,
    windSpeed: Int,
    speedUnits: String,
) {
    val textMeasurer = rememberTextMeasurer()
    val arrowPainter = rememberVectorPainter(image = Icons.Outlined.ArrowDropUp)
    Spacer(
        modifier = modifier
            .aspectRatio(1f)
            .padding(16.dp)
            .drawWithCache {
                val width = size.width
                val halfWidth = size.center.x
                val innerRadius = halfWidth.times(0.9f)
                val outerRadius = halfWidth.times(1.0f)
                val letters = listOf("E", "N", "W", "S")
                onDrawBehind {
                    drawInfoText(halfWidth, textMeasurer, windSpeed, speedUnits)
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.05f),
                                Color.White.copy(alpha = 0.02f),
                            )
                        ), radius = halfWidth / 2
                    )
                    drawArrow(windDirection, halfWidth, arrowPainter)
                    drawLines(innerRadius, outerRadius, width)
                    drawLetters(letters, textMeasurer, innerRadius, width)

                }
            }
    )
}

private fun DrawScope.drawInfoText(
    halfWidth: Float,
    textMeasurer: TextMeasurer,
    windSpeed: Int,
    speedUnits: String,
) {
    val textSize = (size.width * 0.14f).toSp()
    val infoText = textMeasurer.measure(
        text = "$windSpeed\n $speedUnits",
        style = TextStyle(fontSize = textSize, textAlign = TextAlign.Center)
    )
    drawText(
        textLayoutResult = infoText,
        topLeft = Offset(
            halfWidth - infoText.size.width.div(2),
            halfWidth - infoText.size.height.div(2)
        ),
        color = Color.White
    )
}

private fun DrawScope.drawArrow(
    windDirection: Int,
    halfWidth: Float,
    arrow: VectorPainter,
) {
    val painterSize = Size(size.width / 3, size.height / 3)
    val lineStroke = (size.width / 35f)
    rotate((windDirection - 180f), pivot = size.center) {
        with(arrow) {
            translate(
                left = halfWidth - painterSize.width / 2,
                top = -painterSize.height / 2f
            ) {
                draw(
                    Size(painterSize.width, painterSize.height * 1.5f),
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
        }
        drawLine(
            color = Color.White,
            start = Offset(halfWidth, size.height / 10),
            end = Offset(halfWidth, halfWidth.div(2.1f)),
            strokeWidth = lineStroke
        )
    }
    rotate(windDirection.toFloat(), pivot = size.center) {
        drawLine(
            color = Color.White,
            start = Offset(halfWidth, size.height / 25),
            end = Offset(halfWidth, halfWidth.div(2.1f)),
            strokeWidth = lineStroke
        )
    }
}

private fun DrawScope.drawLines(
    innerRadius: Float,
    outerRadius: Float,
    width: Float,
    lineCount: Int = 72,
    offsetDeg: Int = 5,
) {
    val halfWidth = width / 2
    val strokeWidth = (width * 0.01f)
    (0..<lineCount).forEach { index ->
        val rad = (index.toDouble() * offsetDeg)
        val lineRad = Math.toRadians(rad)
        val lineColor =
            if (index % 18 == 0) Color.White.copy(alpha = 0.5f) else Color.White.copy(alpha = 0.1f)
        val startLinesX = (innerRadius * cos(lineRad)).plus(halfWidth).toFloat()
        val endLinesX = (outerRadius * cos(lineRad)).plus(halfWidth).toFloat()

        val startLinesY = (innerRadius * -sin(lineRad)).plus(halfWidth).toFloat()
        val endLinesY = (outerRadius * -sin(lineRad)).plus(halfWidth).toFloat()
        drawLine(
            color = lineColor,
            start = Offset(startLinesX, startLinesY),
            end = Offset(endLinesX, endLinesY),
            strokeWidth = strokeWidth
        )
    }
}

private fun DrawScope.drawLetters(
    letters: List<String>,
    textMeasurer: TextMeasurer,
    inderRadius: Float,
    width: Float,
) {
    val textColor = Color.White.copy(alpha = 0.5f)
    letters.forEachIndexed { index, letter ->
        val deg = index * 90.0
        val rad = Math.toRadians(deg)
        val halfWidth = width / 2

        val startX = (inderRadius * cos(rad)).plus(halfWidth).toFloat()
        val startY = (inderRadius * -sin(rad)).plus(halfWidth).toFloat()
        val textSize = (width * 0.12f).toSp()
        val textLayoutResult = textMeasurer.measure(
            text = letter,
            style = TextStyle(fontSize = textSize)
        )

        val textX = when (letter) {
            "E" -> startX - textLayoutResult.size.width.div(0.75f)
            "W" -> startX + textLayoutResult.size.width.div(3)
            else -> startX - textLayoutResult.size.width.div(2)

        }
        val textY = when (letter) {
            "E" -> startY - textLayoutResult.size.height.div(2)
            "N" -> startY
            "S" -> startY - textLayoutResult.size.height
            else -> startY - textLayoutResult.size.height.div(2)

        }

        drawText(
            textLayoutResult,
            color = textColor,
            topLeft = Offset(
                x = textX,
                y = textY
            )
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Preview(backgroundColor = 0xFF255BFF, showBackground = false)
@Composable
private fun WidPreview() {
    FlowRow(
        Modifier
            .background(Color.Blue.copy(green = 0.35f)),
        maxItemsInEachRow = 2,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        WindWidget(Modifier.weight(1f), windDirection = 0, windSpeed = 12, "km/h")
        WindWidget(Modifier.weight(1f), windDirection = 0, windSpeed = 12, "mph")
    }
}