package com.weather.feature.managelocations

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import com.weather.core.design.theme.Yellow

@ExperimentalFoundationApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomSwipeDismiss(
    modifier: Modifier = Modifier,
    dismissThreshold: Float,
    onDeleteItem: () -> Unit,
    onSetFavoriteItem: () -> Unit,
    content: @Composable () -> Unit,
) = BoxWithConstraints(modifier = modifier) {
    val height = constraints.maxWidth
    val width = constraints.maxWidth
    val hapticFeedback = LocalHapticFeedback.current
    val localView = LocalView.current
    var dismissDirection: DismissDirection? by remember {
        mutableStateOf(null)
    }
    val dismissState = rememberDismissState(
        confirmStateChange = { dismissValue ->
            when {
                dismissValue == DismissValue.DismissedToStart &&
                        dismissDirection == DismissDirection.EndToStart -> {
                    onDeleteItem()
                    true
                }

                dismissValue == DismissValue.DismissedToEnd &&
                        dismissDirection == DismissDirection.StartToEnd -> {
                    onSetFavoriteItem()
                    false
                }

                else -> false
            }
        }
    )
    LaunchedEffect(key1 = Unit) {
        snapshotFlow { dismissState.offset.value }
            .collect {
                dismissDirection = when {
                    it > (width * dismissThreshold) -> {
                        DismissDirection.StartToEnd
                    }

                    it < (width * -dismissThreshold) -> {
                        DismissDirection.EndToStart
                    }

                    else -> null
                }
            }
    }
    LaunchedEffect(key1 = dismissDirection) {
        dismissDirection?.let {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            //this is another option
            //localView.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
        }
    }
    SwipeToDismiss(
        state = dismissState,
        modifier = Modifier,
        directions = setOf(DismissDirection.EndToStart, DismissDirection.StartToEnd),
        dismissThresholds = { _ ->
            FractionalThreshold(
                dismissThreshold
            )
        },
        background = {
            DismissBackground(dismissState, dismissDirection)
        }
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun DismissBackground(
    state: DismissState,
    dismissDirection: DismissDirection?,
) = BoxWithConstraints(modifier = Modifier) {

    val dismissTransition =
        updateTransition(targetState = dismissDirection != null, label = "Dismiss Transition")
    val iconTargetScale by dismissTransition.animateFloat(
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessMedium
            )
        },
        label = "icon scale"
    ) { isDismissed ->
        if (isDismissed)
            1.3f
        else
            1.0f

    }
    val iconColor by animateColorAsState(
        targetValue = when (dismissDirection) {
            DismissDirection.StartToEnd,
            DismissDirection.EndToStart,
            -> Black

            null -> Transparent
        }
    )
    val backGroundColor by animateColorAsState(
        targetValue = when (dismissDirection) {
            DismissDirection.StartToEnd -> Yellow
            DismissDirection.EndToStart -> Red
            null -> MaterialTheme.colors.surface
        },
        animationSpec = tween(200)
    )
    AnimatedContent(
        targetState = Pair(state.dismissDirection, dismissDirection != null),
        transitionSpec = {
            fadeIn(
                tween(0)
            ) with fadeOut(
                tween(0)
            )
        }
    ) { (direction, allowedDismiss) ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    backGroundColor
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentAlignment = when (direction) {
                    DismissDirection.StartToEnd -> Alignment.CenterStart
                    else -> Alignment.CenterEnd
                }
            ) {
                when (direction) {
                    DismissDirection.EndToStart -> {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            modifier = Modifier.scale(
                                scale = iconTargetScale
                            ),
                            tint = if (allowedDismiss) iconColor else Red,
                            contentDescription = "delete Icon"
                        )
                    }

                    DismissDirection.StartToEnd -> {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            modifier = Modifier.scale(
                                scale = iconTargetScale
                            ),
                            tint = if (allowedDismiss) iconColor else Yellow,
                            contentDescription = "favorite Icon"
                        )
                    }

                    null -> Spacer(modifier = Modifier.width(0.dp))
                }
            }
        }
    }
}
