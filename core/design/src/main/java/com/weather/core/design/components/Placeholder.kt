package com.weather.core.design.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape

fun Modifier.weatherPlaceholder(isLoading: Boolean, speed: Int): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "placeholderTransition")
    val xOffset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1_000),
            repeatMode = RepeatMode.Restart
        ),
        label = "placeholderFloat"
    )
    background(
        brush = Brush.linearGradient(listOf(
            Color(0xFFFFFFFF),
            Color(0xFF868181),
            Color(0xFFFFFFFF)
        ),
            start = Offset(xOffset,0f),
            end = Offset(xOffset,0f)
        ),
        shape = RectangleShape,
        alpha = 1f
    )
}