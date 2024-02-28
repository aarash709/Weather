package com.weather.feature.forecast.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.North
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weather.core.design.theme.WeatherTheme
import com.weather.core.design.theme.White

@Composable
internal fun WindDetails(
    modifier: Modifier = Modifier,
    windDirection: Float,
    windSpeed: Float,
) {
    val animatedDegree = remember {
        Animatable(0f)
    }
    LaunchedEffect(key1 = Unit) {
        animatedDegree.animateTo(
            windDirection,
            tween(
                1000,
                100,
                easing = EaseOutCubic
            )
        )
    }
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .height(IntrinsicSize.Max),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BoxedWindIndicator(
                modifier = Modifier.weight(2f),
                windDirection = animatedDegree.value
            )
            Column(
                modifier = Modifier.weight(2f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "${windSpeed}km/h")
            }
        }
    }
}

@Composable
internal fun BoxedWindIndicator(modifier: Modifier = Modifier, windDirection: Float) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .border(width = 2.dp, color = White, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.North,
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer {
                    rotationZ = windDirection.times(-1)
                },
            contentDescription = "Wind direction arrow"
        )
        Box(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxSize()
        ) {
            Text(text = "N", modifier = Modifier.align(Alignment.TopCenter))
            Text(text = "E", modifier = Modifier.align(Alignment.CenterEnd))
            Text(text = "S", modifier = Modifier.align(Alignment.BottomCenter))
            Text(text = "W", modifier = Modifier.align(Alignment.CenterStart))
        }

    }
}

@Composable
internal fun WindDirectionIndicator(
    modifier: Modifier = Modifier,
    animatedDegree: Float,
) {
    val textMeasurer = rememberTextMeasurer()
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
//                        .padding(8.dp)
                .aspectRatio(1f)
                .fillMaxSize()
        ) {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val lineWidth = 1.dp.toPx()
            drawText(
                textMeasurer = textMeasurer,
                text = "N",
                style = TextStyle(color = Color.White),
                topLeft = Offset(
                    x = centerX - 15,
                    y = 10f
                )
            )
            drawText(
                textMeasurer = textMeasurer,
                text = "E",
                style = TextStyle(color = Color.White),
                topLeft = Offset(
                    x = size.width - 40,
                    y = centerY - 25
                )
            )
            drawText(
                textMeasurer = textMeasurer,
                text = "S",
                style = TextStyle(color = Color.White),
                topLeft = Offset(
                    x = centerX - 15,
                    y = size.height - 60
                )
            )
            drawText(
                textMeasurer = textMeasurer,
                text = "W",
                style = TextStyle(color = Color.White),
                topLeft = Offset(
                    x = 10f,
                    y = centerY - 25
                ),

                )
            withTransform({
                rotate(degrees = animatedDegree)
            }) {
                drawLine(
                    Color.White,
                    start = Offset(centerX, centerY.minus(centerY.div(2))),
                    end = Offset(centerX, centerY.plus(centerY.div(2))),
                    strokeWidth = lineWidth,
                    cap = StrokeCap.Round,
                )
                rotate(40f, pivot = Offset(centerX, centerY.minus(centerY.div(2)))) {
                    drawLine(
                        Color.Red,
                        start = Offset(centerX, centerY.minus(centerY.div(2))),
                        end = Offset(centerX, centerY),
                        strokeWidth = lineWidth,
                        cap = StrokeCap.Round
                    )
                }
                rotate(-40f, pivot = Offset(centerX, centerY.minus(centerY.div(2)))) {
                    drawLine(
                        Color.Red,
                        start = Offset(centerX, centerY.minus(centerY.div(2))),
                        end = Offset(centerX, centerY),
                        strokeWidth = lineWidth,
                        cap = StrokeCap.Round
                    )
                }
            }
            drawCircle(
                color = Color.White.copy(alpha = 0.5f),
                center = Offset(centerX, y = centerY),
                radius = centerX,
                style = Stroke(width = lineWidth)
            )
        }
    }
}

@Preview
@Composable
private fun WindIndicatorPreview() {
    WeatherTheme {
        WindDirectionIndicator(animatedDegree = 0f)
    }
}

@Preview
@Composable
private fun BoxedWindIndicatorPreview() {
    WeatherTheme {
        BoxedWindIndicator(windDirection = 0f)
    }
}