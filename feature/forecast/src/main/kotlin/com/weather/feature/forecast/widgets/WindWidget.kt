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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val text = rememberTextMeasurer()
    Canvas(
        modifier = modifier
            .aspectRatio(1f)
            .padding(16.dp)
    ) {
        val width = size.width
        val height = size.height
        val halfWidth = width / 2
        val inderRadius = halfWidth.times(0.85f)
        val outerRadius = halfWidth.times(1.0f)
        val letters = listOf("E", "N", "W", "S")
        letters.forEachIndexed { index, letter ->
            val deg = index * 90.0
            val rad = Math.toRadians(deg)

            val startX = (inderRadius * cos(rad)).plus(halfWidth).toFloat()
            val endX = (outerRadius * cos(rad)).plus(halfWidth).toFloat()

            val startY = (inderRadius * -sin(rad)).plus(halfWidth).toFloat()
            val endY = (outerRadius * -sin(rad)).plus(halfWidth).toFloat()

            drawLine(
                color = Color.White,
                start = Offset(startX, startY),
                end = Offset(endX, endY),
                strokeWidth = 5f
            )

            val textLayoutResult = text.measure(
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
            val textColor = Color.White.copy(alpha = 0.75f)

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

@Preview(backgroundColor = 0xFF255BFF, showBackground = false)
@Composable
private fun WidPreview() {
    WindWidget()
}