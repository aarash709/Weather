package com.weather.feature.forecast.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Thermostat
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weather.core.design.theme.WeatherTheme

@Composable
fun RealFeelWidget(
    realFeel: Int,
    surfaceColor: Color = Color.White.copy(alpha = 0.15f),
) {
    Surface(
        modifier = Modifier
            .aspectRatio(1f),
        shape = RoundedCornerShape(16.dp),
        color = surfaceColor
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(15.dp)
                        .background(Color.White.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Thermostat,
                        modifier = Modifier.padding(3.dp),
                        tint = Color.Blue.copy(alpha = 0.4f),
                        contentDescription = null
                    )
                }
                Text(
                    text = "Real Feel",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }
            Text(text = "$realFeel°", modifier = Modifier.padding(32.dp), fontSize = 64.sp)
        }
    }
}

@Preview
@Composable
private fun RealFeelPrev() {
    WeatherTheme {
        RealFeelWidget(19)
    }
}