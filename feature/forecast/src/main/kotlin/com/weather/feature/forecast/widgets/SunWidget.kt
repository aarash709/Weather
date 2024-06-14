package com.weather.feature.forecast.widgets

import android.graphics.PathMeasure
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ClipOp
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.experiment.weather.core.common.R.string
import com.weather.core.design.components.WeatherSquareWidget
import com.weather.core.design.theme.WeatherTheme
import timber.log.Timber

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
    val paleOnSurfaceColor = LocalContentColor.current.copy(alpha = 0.6f)
    Spacer(modifier = modifier
        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .aspectRatio(1f)
        .padding(16.dp)
        .drawWithCache {
            val width = size.width
            val height = size.height
            val circleSize = 5.dp.toPx()

            val sunsetColor = Color(0xFFf4a169)
            val lightPurple = Color(0xFF7C78E9)
            val darkPurple = Color(0xFF544FE6)
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
                val measure = PathMeasure(path.asAndroidPath(), false)
                val length = measure.length
                measure.getPosTan(length * progress, pathPosition, pathTangent)
                Timber.e("path lenght:${progress}")
                val dayBrush = Brush.verticalGradient(
                    0.0f to daylightColor,
                    0.65f to daylightColor,
                    1.0f to sunsetColor,
                    startY = -height * 0.3f, // magic number
                    endY = height / 1.4f
                )
                val nightBrush = Brush.verticalGradient(
                    0.0f to lightPurple,
                    1.0f to darkPurple,
                    startY = height / 1.4f, // magic number
                    endY = height * 0.8f
                )
                val indicatorBoarderOffset = circleSize / 32
                drawLine(
                    paleOnSurfaceColor,
                    start = Offset(
                        width - (width.times(1.25f)),
                        height / 1.4f
                    ),
                    end = Offset(width.times(1.25f), height / 1.4f),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f))
                )
                clipRect(
                    left = width - (width.times(1.25f)),
                    top = height / 1.4f,
                    right = width.times(1.25f),
                    bottom = height * 1.1f,
                    clipOp = ClipOp.Difference
                ) {
                    drawSundialPath(
                        path = path,
                        brush = dayBrush
                    )
                }
                clipRect(
                    left = width - (width.times(1.25f)),
                    top = height / 1.4f,
                    right = width.times(1.25f),
                    bottom = height * 1.1f,
                    clipOp = ClipOp.Intersect
                ) {
                    drawSundialPath(
                        path = path,
                        brush = nightBrush
                    )
                }
                drawCircleIndicator(
                    brush = if (progress in 0.18..0.85) dayBrush else nightBrush,
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
        //start point
        moveTo(-width * 0.15f, height)
        //center point
        //control points 1 and 2
        cubicTo(
            x1 = width * 0.25f,
            y1 = height,
            x2 = width * 0.1f,
            y2 = height * 0.1f,
            x3 = centerX,
            y3 = height * 0.1f
        )
        //end pint
        //control points 3 and 4
        cubicTo(
            x1 = width * 0.9f,
            y1 = height * 0.1f,
            x2 = width * 0.75f,
            y2 = height,
            x3 = width * 1.15f,
            y3 = height
        )
    }
}

private fun DrawScope.drawSundialPath(path: Path, brush: Brush) {
    drawPath(
        path = path, brush = brush,
        style = Stroke(
            size.width / 12,
            cap = StrokeCap.Round
        )
    )
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

//@Preview
//@Composable
//private fun GraphPreview() {
//    WeatherTheme {
//        Surface(modifier = Modifier.aspectRatio(0.9f), color = Color.Blue) {
//            SunGraph(Modifier.padding(16.dp), sunrise = 1, sunset = 100, currentTime = 20)
//        }
//    }
//}

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
private fun UVPreview() {
    val position = 80
    WeatherTheme {
        val color = MaterialTheme.colorScheme.background
        FlowRow(maxItemsInEachRow = 2, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SunWidget(
                modifier = Modifier.weight(1f),
                sunrise = 0,
                sunset = 100,
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
