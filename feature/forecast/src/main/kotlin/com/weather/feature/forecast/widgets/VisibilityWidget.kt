package com.weather.feature.forecast.widgets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun VisibilityWidget(visibility: Int, modifier: Modifier = Modifier) {
    WeatherSquareWidget(
        modifier = modifier,
        icon = Icons.Outlined.RemoveRedEye,
        title = "Visibility"
    ) {
        Text(text = "$visibility", fontSize = 32.sp)
    }
}

@Preview
@Composable
private fun VisibilityPreview() {
    VisibilityWidget(1000)
}