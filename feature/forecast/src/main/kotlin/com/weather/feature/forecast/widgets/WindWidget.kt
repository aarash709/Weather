package com.weather.feature.forecast.widgets

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun WindWidget() {
    Surface(modifier = Modifier){
        Text(text = "Wind")
    }
}