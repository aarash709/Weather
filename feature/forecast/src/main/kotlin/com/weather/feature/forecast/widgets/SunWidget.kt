package com.weather.feature.forecast.widgets

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weather.core.design.components.WeatherSquareWidget
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sin

@Composable
fun SunWidget(sunrise: Int, sunset: Int, currentTime: Int, modifier: Modifier = Modifier) {
    WeatherSquareWidget(
        modifier = modifier.graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen),
        icon = Icons.Outlined.WbSunny,
        title = "Sunrise"
    ) {
        SunGraph(
            modifier = Modifier,
            sunrise = sunrise,
            sunset = sunset,
            currentTime = currentTime
        )
    }
}

@Composable
private fun SunGraph(
    modifier: Modifier = Modifier,
    sunrise: Int,
    sunset: Int,
    currentTime: Int,
) {
    Spacer(modifier = modifier
        .aspectRatio(1f)
        .padding(16.dp)
        .drawWithCache {
            val width = size.width
            val height = size.height
            val centerX = width / 2
            val centerY = height / 2

            val circleSize = 8.dp.toPx()

            val sunsetColor = Color.Red.copy(green = 0.3f)
            val darkBlue = Color(5, 20, 50)
            val daylightColor = Color(244, 250, 127)
            val daylightBrush = Brush.horizontalGradient(
                0.0f to sunsetColor,
                0.05f to daylightColor,
                0.95f to daylightColor,
                1.0f to sunsetColor
            )
            val nightColor = Brush.horizontalGradient(listOf(darkBlue, darkBlue))

            val archThickness = 6.dp.toPx()
            val timeRange = sunset.minus(sunrise)

            val progress = currentTime
                .minus(sunrise)
                .toFloat()
                .div(timeRange)
            val radius = size.width / 2
            //added 180 offset as starting point if progress is 0.0f
            val angle = (progress.times(180)) + 180.0

            val x = (radius * cos(Math.toRadians(angle)).toFloat()) + size.width / 2
            val y = (radius * sin(Math.toRadians(angle)).toFloat()) + size.height / 2
            onDrawBehind {
                drawLine(
                    Color.White.copy(alpha = 0.5f),
                    start = Offset(
                        width - (width.times(1.25f)),
                        height / 2
                    ),
                    end = Offset(width.times(1.25f), height / 2)
                )
//                drawSundial(dayBrush = daylightBrush, archThickness = archThickness)
//                val circlePosition = if (x
//                        .isNaN()
//                        .not() && y
//                        .isNaN()
//                        .not()
//                ) {
//                    Offset(x = x, y = y)
//                } else Offset.Zero
//                drawCircleIndicator(
//                    brush = if (currentTime > sunset)
//                        nightColor
//                    else daylightBrush,
//                    circleSize = circleSize,
//                    position = circlePosition,
//                    shouldShowBorder = true
//                )
                //test
                val path = Path().apply {
                    moveTo(-50.0f, centerY + 100f)
                    //start
                    quadraticBezierTo(
                        x1 = 10f,
                        y1 = centerY + 100f,
                        x2 = 50.0f,
                        y2 = centerY
                    )
                    //center
                    quadraticBezierTo(
                        x1 = centerX,
                        y1 = -centerY,
                        x2 = width - 50f,
                        y2 = centerY
                    )
                    //end
                    quadraticBezierTo(
                        x1 = width - 10,
                        y1 = centerY + 100f,
                        x2 = width + 50f,
                        y2 = centerY + 100f
                    )
                }
                val position = FloatArray(2)
                val tan = FloatArray(2)
                val measure = android.graphics.PathMeasure(path.asAndroidPath(), false)
                val length = measure.length
                measure.getPosTan(length * progress, position, tan)
                clipRect {

                }
                val brush = Brush.horizontalGradient(
                    0.05f to darkBlue,
                    0.15f to sunsetColor,
                    0.2f to daylightColor,
                    0.8f to daylightColor,
                    0.85f to sunsetColor,
                    0.95f to darkBlue
                )
                testPath(
                    path = path,
                    brush = brush
                )
                drawCircle(
                    Color.Black,
                    radius = 20f,
                    center = Offset(position[0], position[1]),
                    blendMode = BlendMode.Clear
                )
                drawCircle(
                    brush = brush,
                    radius = 15f,
                    center = Offset(position[0], position[1])
                )
                drawLine(
                    Color.Cyan,
                    strokeWidth = 10f,
                    start = Offset(x = position[0], y = position[1]),
                    end = Offset(
                        x = position[0] + tan[0] * 50f,
                        y = position[1] + tan[1] * 50f
                    ),
                    cap = StrokeCap.Round
                )
            }
        })
}

private fun DrawScope.testPath(path: Path, brush: Brush) {
    val width = size.width
    val height = size.height
    fun calculate(x: Float) = (height - (height - x.pow(2))).times(
        exp(
            -height / 2 * x.pow(2)
        )
    )
    drawPath(path, brush, style = Stroke(10f, cap = StrokeCap.Round))
}

private fun DrawScope.drawSundial(dayBrush: Brush, archThickness: Float) {
    drawArc(
        color = Color.White.copy(alpha = 0.5f),
        topLeft = Offset.Zero,
        startAngle = 0f,
        sweepAngle = 180f,
        useCenter = false,
        style = Stroke(
            width = archThickness,
            cap = StrokeCap.Butt,
            pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(5f, 20f),
                phase = 15f
            )
        )
    )
    drawArc(
        brush = dayBrush,
        topLeft = Offset.Zero,
        startAngle = 180f,
        sweepAngle = 180f,
        useCenter = false,
        style = Stroke(width = archThickness),
    )
}

private fun DrawScope.drawCircleIndicator(
    brush: Brush,
    circleSize: Float,
    position: Offset = Offset.Zero,
    shouldShowBorder: Boolean = true,
) {
    if (shouldShowBorder) {
        drawCircle(
            color = Color.Black,
            radius = circleSize.times(1.3f),
            center = position,
            blendMode = BlendMode.Clear

        )
    }
    drawCircle(
        brush = brush,
        radius = circleSize,
        center = position
    )
}

@Preview
@Composable
private fun UVPreview() {
    SunWidget(sunrise = 0, sunset = 100, currentTime = 25)
}