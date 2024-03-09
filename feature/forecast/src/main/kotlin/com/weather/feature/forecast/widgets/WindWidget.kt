package com.weather.feature.forecast.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Air
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
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
) {
    WeatherSquareWidget(modifier, icon = Icons.Outlined.Air, title = "Wind") {
        WindDirectionGraph(windDirection = 90)
    }
}

@Composable
internal fun WindDirectionGraph(modifier: Modifier = Modifier, windDirection: Int) {
    val textMeasurer = rememberTextMeasurer()
    Canvas(
        modifier = modifier
            .aspectRatio(1f)
            .padding(16.dp)
    ) {
        val width = size.width
        val height = size.height
        val halfWidth = width / 2
        val innerRadius = halfWidth.times(0.85f)
        val outerRadius = halfWidth.times(1.0f)
        val letters = listOf("E", "N", "W", "S")
        drawLines(innerRadius, outerRadius, width)
        drawLetters(letters, textMeasurer, innerRadius, width)
    }
//    Icon(
//        imageVector = Icons.Outlined.ArrowUpward,
//        contentDescription = "wind direction arrow icon",
//        modifier = Modifier
//            .align(Alignment.Center)
//            .size(64.dp)
//            .graphicsLayer {
//                rotationZ = windDirection.minus(180f)
//            },
//    )
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
        val lineColor = if (index % 18 == 0) Color.White else Color.White.copy(alpha = 0.2f)
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
    val textColor = Color.White.copy(alpha = 0.75f)
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
    WindWidget()
}