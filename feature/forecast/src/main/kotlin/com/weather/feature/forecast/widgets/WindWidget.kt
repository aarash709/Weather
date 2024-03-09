package com.weather.feature.forecast.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material.icons.outlined.ArrowDropUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weather.core.design.components.WeatherSquareWidget
import kotlin.math.cos
import kotlin.math.sin

@Composable
internal fun WindWidget(
    modifier: Modifier = Modifier,
    windDirection: Int,
    windSpeed: Int,
) {
    WeatherSquareWidget(modifier, icon = Icons.Outlined.Air, title = "Wind") {
        WindDirectionGraph(windDirection = windDirection, windSpeed = windSpeed)
    }
}

@Composable
internal fun WindDirectionGraph(modifier: Modifier = Modifier, windDirection: Int, windSpeed: Int) {
    val textMeasurer = rememberTextMeasurer()
    val painter = rememberVectorPainter(image = Icons.Outlined.ArrowDropUp)
    Canvas(
        modifier = modifier
            .aspectRatio(1f)
            .padding(16.dp)
    ) {
        val width = size.width
        val height = size.height
        val halfWidth = size.center.x
        val innerRadius = halfWidth.times(0.85f)
        val outerRadius = halfWidth.times(1.0f)
        val letters = listOf("E", "N", "W", "S")
        drawInfoText(halfWidth, textMeasurer, windSpeed)
        drawArrow(windDirection, halfWidth, painter)
        drawLines(innerRadius, outerRadius, width)
        drawLetters(letters, textMeasurer, innerRadius, width)
    }
}

private fun DrawScope.drawInfoText(halfWidth: Float, textMeasurer: TextMeasurer, windSpeed: Int) {
    val infoText = textMeasurer.measure("$windSpeed km/h")
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
    painter: VectorPainter,
) {
    rotate((windDirection - 180f), pivot = size.center) {
        with(painter) {
            translate(left = halfWidth - intrinsicSize.width / 2) {
                draw(
                    Size(intrinsicSize.width, intrinsicSize.height),
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
        }
        drawLine(
            color = Color.White,
            start = Offset(halfWidth, 30f),
            end = Offset(halfWidth, halfWidth.div(1.5f)),
            strokeWidth = 3.dp.toPx()
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
            strokeWidth = 5f
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

        val textLayoutResult = textMeasurer.measure(
            text = letter,
            style = TextStyle(fontSize = halfWidth.times(0.25f).toSp())
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

@Preview(backgroundColor = 0xFF255BFF, showBackground = false)
@Composable
private fun WidPreview() {
    WindWidget(windDirection = 0, windSpeed = 12)
}