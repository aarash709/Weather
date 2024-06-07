package com.weather.feature.forecast.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.experiment.weather.core.common.R.string
import com.weather.core.design.components.WeatherSquareWidget
import com.weather.core.design.theme.WeatherTheme

@Composable
fun SunWidget(
    sunrise: Int,
    sunset: Int,
    currentTime: Int,
    modifier: Modifier = Modifier,
    surfaceColor: Color,
) {
    WeatherSquareWidget(
        modifier = modifier,
        title = stringResource(id = string.sunrise),
        surfaceColor = surfaceColor
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
    val paleOnSurfaceColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    Spacer(modifier = modifier
        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .aspectRatio(1f)
        .padding(16.dp)
        .drawWithCache {
            val width = size.width
            val height = size.height
            val circleSize = 5.dp.toPx()

            val sunsetColor = Color(0xFFf4a169)
            val darkBlue = Color(5, 20, 50)
            val daylightColor = Color(0xFFfaca6b)

            val timeRange = sunset.minus(sunrise)

            val progress = currentTime
                .minus(sunrise)
                .toFloat()
                .div(timeRange)

            onDrawBehind {
                val path = calculatePath()
                val pathPosition = FloatArray(2)
                val pathTangent = FloatArray(2)
                val measure = android.graphics.PathMeasure(path.asAndroidPath(), false)
                val length = measure.length
                measure.getPosTan(length * progress, pathPosition, pathTangent)
                val brush = Brush.verticalGradient(
                    0.0f to daylightColor,
                    0.65f to daylightColor,
                    0.90f to sunsetColor,
                    1.0f to darkBlue,
                    startY = -height * 0.3f, // magic number
                    endY = height - 10f
                )
                val indicatorBoarderOffset = circleSize / 32
                drawLine(
                    paleOnSurfaceColor,
                    start = Offset(
                        width - (width.times(1.25f)),
                        height / 1.20f
                    ),
                    end = Offset(width.times(1.25f), height / 1.20f)
                )
                drawSundialPath(
                    path = path,
                    brush = brush
                )
                drawCircleIndicator(
                    brush = brush,
                    radius = circleSize,
                    boarderRadius = circleSize * indicatorBoarderOffset,
                    position = pathPosition,
                    shouldShowBorder = true
                )
            }
        })
}

fun DrawScope.calculatePath(): Path {
    val width = size.width
    val height = size.height
    val centerX = size.width / 2
    val centerY = size.height / 2
    return Path().apply {
        moveTo(-width / 6, height)
        //start
        quadraticBezierTo(
            x1 = width / 32,
            y1 = centerY * 2,
            x2 = width / 6,
            y2 = centerY
        )
        //center
        quadraticBezierTo(
            x1 = centerX,
            y1 = -centerY,
            x2 = width - width / 6,
            y2 = centerY
        )
        //end
        quadraticBezierTo(
            x1 = width - width / 32,
            y1 = centerY * 2,
            x2 = width + width / 6,
            y2 = height
        )
    }
}

private fun DrawScope.drawSundialPath(path: Path, brush: Brush) {
    drawPath(path, brush, style = Stroke(size.width / 12, cap = StrokeCap.Round))
}


private fun DrawScope.drawCircleIndicator(
    brush: Brush,
    radius: Float,
    boarderRadius: Float,
    position: FloatArray = floatArrayOf(0f, 0f),
    shouldShowBorder: Boolean = true,
) {
    if (shouldShowBorder) {
        drawCircle(
            Color.Transparent,
            radius = radius + boarderRadius,
            center = Offset(position[0], position[1]),
            blendMode = BlendMode.Clear
        )
    }
    drawCircle(
        brush = brush,
        radius = radius,
        center = Offset(position[0], position[1])
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
private fun UVPreview() {
    val position = 55
    WeatherTheme {
        val color = MaterialTheme.colorScheme.background
        FlowRow(maxItemsInEachRow = 2, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SunWidget(
                modifier = Modifier.weight(1f),
                sunrise = 20,
                sunset = 90,
                currentTime = position,
                surfaceColor = color
            )
            SunWidget(
                modifier = Modifier.weight(1f),
                sunrise = 20,
                sunset = 90,
                currentTime = position,
                surfaceColor = color
            )
        }
    }
}