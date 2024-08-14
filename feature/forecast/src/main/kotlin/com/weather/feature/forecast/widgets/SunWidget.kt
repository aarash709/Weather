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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.experiment.weather.core.common.R.string
import com.weather.core.design.components.WeatherSquareWidget
import com.weather.core.design.theme.WeatherTheme
import com.weather.core.repository.fake.data.listOfWeatherDataTest
import com.weather.model.WeatherData

@Composable
fun SunWidget(
    formattedSunrise: String,
    formattedSunset: String,
    weatherData: WeatherData,
    currentTimeSeconds: Int,
    modifier: Modifier = Modifier,
    surfaceColor: Color,
) {
    val sunrise = weatherData.current.sunrise
    val sunset = weatherData.current.sunset
    val title = when {
        currentTimeSeconds < sunrise -> stringResource(id = string.sunrise)
        currentTimeSeconds in (sunrise + 1)..<sunset -> stringResource(id = string.sunset)
        //default title because sometimes open weather servers not updating sunrise and set on time
        else -> stringResource(id = string.sunrise)
    }
    val infoText = when {
        currentTimeSeconds < sunrise -> formattedSunrise
        currentTimeSeconds in (sunrise + 1)..<sunset -> formattedSunset
        //default sun time because sometimes open weather servers not updating sunrise and set on time
        else -> formattedSunrise
    }
    WeatherSquareWidget(
        modifier = modifier,
        title = title,
        infoText = infoText,
        surfaceColor = surfaceColor
    ) {
        SunGraph(
            modifier = Modifier,
            formattedSunrise = formattedSunrise,
            formattedSunset = formattedSunset,
            sunrise = sunrise,
            sunset = sunset,
            currentTime = currentTimeSeconds
        )
    }
}

@Composable
private fun SunGraph(
    modifier: Modifier = Modifier,
    formattedSunrise: String = "00:00",
    formattedSunset: String = "00:00",
    sunrise: Int,
    sunset: Int,
    currentTime: Int,
) {
    val paleOnSurfaceColor = LocalContentColor.current.copy(alpha = 0.6f)
    val textMeasure = rememberTextMeasurer()
    Spacer(modifier = modifier
        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .aspectRatio(1.25f)
        .padding(10.dp)
        .drawWithCache {
            val width = size.width
            val height = size.height

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
                val strokeWidth = height / 14
                val indicatorRadius = (height / 17f)
                val pathPosition = FloatArray(2)
                val pathTangent = FloatArray(2)
                val maskingHeight = height / 1.5f
                val path = calculatePath(strokeWidth = strokeWidth)
                val measure = PathMeasure(path.asAndroidPath(), false)
                val length = measure.length
                measure.getPosTan(length * progress, pathPosition, pathTangent)

                val dayBrush = Brush.verticalGradient(
                    0.0f to daylightColor,
                    0.65f to daylightColor,
                    1.0f to sunsetColor,
                    startY = -height * 0.3f, // magic number
                    endY = maskingHeight
                )
                val nightBrush = Brush.verticalGradient(
                    0.0f to lightPurple,
                    1.0f to darkPurple,
                    startY = maskingHeight, // magic number
                    endY = height * 0.8f
                )
                drawLine(
                    paleOnSurfaceColor,
                    start = Offset(
                        x = 0f,
                        y = maskingHeight
                    ),
                    end = Offset(width, height / 1.5f),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f))
                )
                //sunrise and sunset times under graph
                val fontSize = (height / 20).sp
                val sunriseText =
                    textMeasure.measure(
                        formattedSunrise,
                        style = TextStyle(fontSize = fontSize)
                    )
                val sunsetText =
                    textMeasure.measure(formattedSunset, style = TextStyle(fontSize = fontSize))
                drawText(
                    textLayoutResult = sunriseText,
                    color = paleOnSurfaceColor,
                    topLeft = Offset(x = 0f, height - sunriseText.size.height * 0.8f),
                )
                drawText(
                    textLayoutResult = sunsetText,
                    color = paleOnSurfaceColor,
                    topLeft = Offset(
                        x = width - sunsetText.size.width,
                        y = height - sunsetText.size.height * 0.8f
                    )
                )
                //one path clipped to apply two colors for day and night
                clipRect(
                    left = width - (width.times(1.25f)),
                    top = maskingHeight,
                    right = width.times(1.25f),
                    bottom = height * 1.1f,
                    clipOp = ClipOp.Difference
                ) {
                    drawSundialPath(
                        path = path,
                        brush = dayBrush,
                        strokeWidth = strokeWidth
                    )
                }
                clipRect(
                    left = width - (width.times(1.25f)),
                    top = maskingHeight,
                    right = width.times(1.25f),
                    bottom = height * 1.1f,
                    clipOp = ClipOp.Intersect
                ) {
                    drawSundialPath(
                        path = path,
                        brush = nightBrush,
                        strokeWidth = strokeWidth
                    )
                }
                drawCircleIndicator(
                    brush = if (progress in 0.18..0.85) dayBrush else nightBrush,
                    radius = indicatorRadius,
                    position = pathPosition,
                    shouldShowBorder = true
                )

            }
        })
}

fun DrawScope.calculatePath(strokeWidth: Float): Path {
    val width = size.width
    val height = size.height
    val bottomHeight = height * 0.8f
    val topHeight = height * .1f
    val centerX = size.width / 2
    val centerY = size.height / 2
    return Path().apply {
        //start point
        moveTo(strokeWidth, bottomHeight)
        //center point
        //control points 1 and 2
        cubicTo(
            x1 = width * 0.35f,
            y1 = bottomHeight,
            x2 = width * 0.2f,
            y2 = topHeight,
            x3 = width * 0.5f,
            y3 = topHeight
        )
        //end pint
        //control points 3 and 4
        cubicTo(
            x1 = width * 0.8f,
            y1 = topHeight,
            x2 = width * 0.65f,
            y2 = bottomHeight,
            x3 = width - strokeWidth,
            y3 = bottomHeight
        )
    }
}

private fun DrawScope.drawSundialPath(path: Path, brush: Brush, strokeWidth: Float) {
    drawPath(
        path = path, brush = brush,
        style = Stroke(
            strokeWidth,
            cap = StrokeCap.Round
        )
    )
}


private fun DrawScope.drawCircleIndicator(
    brush: Brush,
    radius: Float,
    position: FloatArray = floatArrayOf(0f, 0f),
    shouldShowBorder: Boolean = true,
) {
    if (shouldShowBorder) {
        drawCircle(
            Color.Transparent,
            radius = radius,
            center = Offset(position[0], position[1]),
            style = Stroke(radius),
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
@PreviewLightDark
@Composable
private fun UVPreview() {
    val position = 50
    WeatherTheme {
        val color = MaterialTheme.colorScheme.background
        FlowRow(maxItemsInEachRow = 2, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SunWidget(
                modifier = Modifier.weight(1f),
                weatherData = listOfWeatherDataTest.first(),
                currentTimeSeconds = position,
                surfaceColor = color,
                formattedSunrise = "06:10",
                formattedSunset = "18:30",
            )
            SunWidget(
                modifier = Modifier.weight(1f),
                weatherData = listOfWeatherDataTest.first(),
                currentTimeSeconds = position,
                surfaceColor = color,
                formattedSunrise = "06:10",
                formattedSunset = "18:30",
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@PreviewLightDark
@Composable
private fun UVSinglePreview() {
    val position = 50
    WeatherTheme {
        val color = MaterialTheme.colorScheme.background
        FlowRow(maxItemsInEachRow = 2, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SunWidget(
                modifier = Modifier.weight(1f),
                weatherData = listOfWeatherDataTest.first(),
                currentTimeSeconds = position,
                surfaceColor = color,
                formattedSunrise = "06:10",
                formattedSunset = "18:30",
            )
        }
    }
}
