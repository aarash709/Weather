package com.weather.core.design.modifiers

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView

fun Modifier.bouncyTapEffect(targetScale:Float = 0.95f) = composed {
    var itemPressed by remember {
        mutableStateOf(false)
    }
    val itemScale by animateFloatAsState(
        targetValue = if (itemPressed) targetScale else 1f,
        animationSpec = tween(100),
        label = "bouncy scale"
    )
    val haptic = LocalHapticFeedback.current
    LaunchedEffect(key1 = itemPressed){
        if (itemPressed)
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
    }
    graphicsLayer {
        scaleY = itemScale
        scaleX = itemScale
    }.pointerInput(itemPressed) {
        awaitPointerEventScope {
            itemPressed = if (itemPressed) {
                waitForUpOrCancellation()
                !itemPressed
            } else {
                awaitFirstDown(false)
                !itemPressed
            }
        }
    }
}