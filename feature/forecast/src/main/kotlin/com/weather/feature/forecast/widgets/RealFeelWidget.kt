package com.weather.feature.forecast.widgets

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.experiment.weather.core.common.R.*
import com.weather.core.design.components.WeatherSquareWidget
import com.weather.core.design.theme.WeatherTheme
import kotlin.math.cos
import kotlin.math.sin


@Composable
internal fun RealFeelWidget(
    realFeel: Int,
    modifier: Modifier = Modifier,
    surfaceColor: Color,
) {
    WeatherSquareWidget(
        modifier,
        title = stringResource(id = string.real_feel),
        surfaceColor = surfaceColor,
        infoText = "$realFeel°"
    ) {
        RealFeelGraph(realFeel)
    }
}

@Composable
private fun RealFeelGraph(realFeel: Int) {
    val indicatorColor = LocalContentColor.current
    Spacer(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(12.dp)
            .drawWithCache {
                val width = size.width
                val height = size.height
                val circleSize = (width * 0.06f)
                val indicatorStrokeWidth = (width * 0.03f)
                val archThickness = (width / 12f)
                val progress =
                    realFeel
                        .coerceIn(minimumValue = 0, maximumValue = 40)
                        .toDouble()
                        .div(40)
                val endOffset = size.width / 3f
                val angle = (progress * 270) + 45
                val x = -(circleSize * sin(Math.toRadians(angle)).toFloat()) + size.width / 2
                val y = (circleSize * cos(Math.toRadians(angle)).toFloat()) + size.height / 2
                val xEnd = -(endOffset * sin(Math.toRadians(angle)).toFloat()) + size.width / 2
                val yEnd = (endOffset * cos(Math.toRadians(angle)).toFloat()) + size.height / 2
                onDrawBehind {
                    // 5 degrees added for a gap between archs and is subtracted
                    // from the sweep angle of the last arch
                    drawArc(
                        color = Color.Blue.copy(green = 0.5f),
                        startAngle = 135f,
                        sweepAngle = 90f,
                        useCenter = false,
                        topLeft = Offset(0f, 0f),
                        style = Stroke(width = archThickness, cap = StrokeCap.Round),
                    )
                    drawArc(
                        color = Color.Green,
                        startAngle = 225f,
                        sweepAngle = 90f,
                        useCenter = false,
                        topLeft = Offset(0f, 0f),
                        style = Stroke(width = archThickness, cap = StrokeCap.Round),
                    )
                    drawArc(
                        color = Color.Red.copy(green = 0.5f),
                        startAngle = 315f,
                        sweepAngle = 90f,
                        useCenter = false,
                        topLeft = Offset(0f, 0f),
                        style = Stroke(width = archThickness, cap = StrokeCap.Round),
                    )
                    //indicator
                    drawCircle(
                        color = indicatorColor,
                        radius = circleSize,
                        center = Offset(x = width / 2, y = height / 2),
                        style = Stroke(width = indicatorStrokeWidth)
                    )
                    drawLine(
                        color = indicatorColor,
                        start = Offset(x, y),
                        end = Offset(xEnd, yEnd),
                        strokeWidth = indicatorStrokeWidth,
                        cap = StrokeCap.Round

                    )
                }
            }
    )
}


@OptIn(ExperimentalLayoutApi::class)
@Preview(backgroundColor = 0xFF255BFF, showBackground = false)
@Composable
private fun RealFeelPrev() {
    WeatherTheme {
        val color = MaterialTheme.colorScheme.background
        FlowRow(maxItemsInEachRow = 2, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            RealFeelWidget(modifier = Modifier.weight(1f), realFeel = -10, surfaceColor = color)
            RealFeelWidget(modifier = Modifier.weight(1f), realFeel = -10, surfaceColor = color)
        }
    }
}