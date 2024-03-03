package com.weather.feature.forecast.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun WindWidget(
    modifier: Modifier = Modifier,
    surfaceColor: Color = Color.White.copy(alpha = 0.15f),
) {
    Surface(
        modifier = Modifier.aspectRatio(1f) then modifier,
        shape = RoundedCornerShape(16.dp),
        color = surfaceColor
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
//                Box(
//                    modifier = Modifier
//                        .clip(CircleShape)
//                        .size(15.dp)
//                        .background(Color.White.copy(alpha = 0.5f)),
//                    contentAlignment = Alignment.Center
//                ) {
                    Icon(
                        imageVector = Icons.Outlined.Air,
                        modifier = Modifier.padding(3.dp),
                        tint = Color.Blue.copy(alpha = 0.4f),
                        contentDescription = null
                    )
//                }
                Text(
                    text = "Wind",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }
//            Text(text = "test", fontSize = 32.sp)
            WindDirection(modifier = Modifier.padding(32.dp), windDirection = 90)
        }
    }
}

@Composable
fun WindDirection(modifier: Modifier = Modifier, windDirection: Int) {
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color.White,
                radius = size.width / 2,
                style = Stroke(width = size.width.times(0.01f))
            )
        }
        Icon(
            imageVector = Icons.Outlined.ArrowUpward,
            contentDescription = "wind direction arrow icon",
            modifier = Modifier
                .align(Alignment.Center)
                .size(64.dp)
                .graphicsLayer {
                    rotationZ = windDirection.minus(180f)
                },
        )
    }
}

@Preview(backgroundColor = 0xFF255BFF, showBackground = false)
@Composable
private fun WidPreview() {
    WindWidget()
}