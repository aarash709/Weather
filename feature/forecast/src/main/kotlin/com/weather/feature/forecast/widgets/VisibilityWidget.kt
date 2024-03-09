package com.weather.feature.forecast.widgets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.weather.core.design.components.WeatherSquareWidget


@Composable
fun VisibilityWidget(visibility: Int, modifier: Modifier = Modifier) {
    val visibilityValue = when {
        visibility < 1000 -> {
            "${visibility}m"
        }

        visibility >= 1000 -> {
            "${visibility.div(1000)}km"
        }

        else -> {
            "$visibility"
        }
    }

    WeatherSquareWidget(
        modifier = modifier,
        icon = Icons.Outlined.RemoveRedEye,
        title = "Visibility"
    ) {
        Text(text = visibilityValue, fontSize = 32.sp)
    }
}

@Preview
@Composable
private fun VisibilityPreview() {
    VisibilityWidget(1000)
}