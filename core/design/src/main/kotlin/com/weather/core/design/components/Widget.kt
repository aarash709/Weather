package com.weather.core.design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    title: String,
    surfaceColor: Color,
    infoText: String = "",
    content: @Composable BoxScope.() -> Unit,
) {
    Surface(
        modifier = Modifier.aspectRatio(1.1f) then modifier,
        shape = RoundedCornerShape(32.dp),
        color = surfaceColor
    ) {
        val paleOnSurfaceColor = LocalContentColor.current.copy(alpha = 0.7f)
        Column(
            Modifier
                .padding(16.dp)
        ) {
            Column(
                Modifier
                    .weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    color = paleOnSurfaceColor
                )
                Text(text = infoText, fontSize = 20.sp)
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1.5f),
                contentAlignment = Alignment.CenterEnd
            ) {
                content()
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
private fun WidgetPrev() {

    WeatherTheme {
//        Color.Blue.copy(green = 0.5f)
        val isDay = true
        val darkBG = Color(0xFF11284D)
        val lightBG = Color(0xFF4876BE)
        val widgetSurfaceColor =
            if (isDay) Color.Black.copy(alpha = 0.1f) else Color.White.copy(alpha = 0.1f)
        FlowRow(
            Modifier
                .background(if (isDay) lightBG else darkBG),
            maxItemsInEachRow = 2,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            WeatherSquareWidget(
                modifier = Modifier.weight(1f),
                title = "Title",
                surfaceColor = widgetSurfaceColor,
                infoText = "info"
            ) {
                Text(text = "Content", fontSize = 32.sp)
            }
            WeatherSquareWidget(
                modifier = Modifier.weight(1f),
                title = "Title",
                surfaceColor = widgetSurfaceColor,
                infoText = "info"
            ) {
                Text(text = "Content", fontSize = 32.sp)
            }
        }

    }
}
