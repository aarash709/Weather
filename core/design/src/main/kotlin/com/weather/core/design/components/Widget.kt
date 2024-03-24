package com.weather.core.design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.font.FontVariation.weight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weather.core.design.theme.WeatherTheme

/**
 * Use this composable for small square shape widgets in forecast screen
 */
@Composable
fun WeatherSquareWidget(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    surfaceColor: Color = Color.Black.copy(alpha = 0.10f),
    infoText: String,
    content: @Composable BoxScope.() -> Unit,
) {
    Surface(
        modifier = Modifier.aspectRatio(1f) then modifier,
        shape = RoundedCornerShape(16.dp),
        color = surfaceColor
    ) {
        Column(
            Modifier
                .padding(16.dp)
        ) {
            Column(
                Modifier
                    .weight(1f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.Top,
                ) {
                    Icon(
                        imageVector = icon,
                        modifier = Modifier.size(16.dp),
                        tint = Color.White.copy(alpha = 0.5f),
                        contentDescription = null
                    )
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }
                Text(text = infoText, fontSize = 26.sp)
            }
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    content()
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
private fun WidgetPrev() {
    WeatherTheme {
        FlowRow(
            Modifier
                .background(Color.Blue.copy(green = 0.5f)),
            maxItemsInEachRow = 2,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            WeatherSquareWidget(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.BrightnessLow,
                title = "Title",
                infoText = "info"
            ) {
                Text(text = "Content", fontSize = 32.sp)
            }
            WeatherSquareWidget(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.BrightnessLow,
                title = "Title",
                infoText = "info"
            ) {
                Text(text = "Content", fontSize = 32.sp)
            }
        }

    }
}
