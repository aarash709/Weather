package com.weather.core.design.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessLow
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Use this composable for small square shape widgets in forecast screen
 */
@Composable
fun WeatherSquareWidget(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    surfaceColor: Color = Color.White.copy(alpha = 0.15f),
    content: @Composable BoxScope.() -> Unit,
) {
    Surface(
        modifier = Modifier.aspectRatio(1f) then modifier,
        shape = RoundedCornerShape(16.dp),
        color = surfaceColor
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    modifier = Modifier.padding(3.dp),
                    tint = Color.White.copy(alpha = 0.5f),
                    contentDescription = null
                )
                Text(
                    text = title,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                content()
            }
        }
    }
}

@Preview
@Composable
private fun WidgetPrev() {
    WeatherSquareWidget(icon = Icons.Default.BrightnessLow, title = "Title") {
        Text(text = "Content", fontSize = 32.sp)
    }
}
