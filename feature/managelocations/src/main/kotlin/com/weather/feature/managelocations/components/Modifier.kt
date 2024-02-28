package com.weather.feature.managelocations.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.ui.Modifier


@OptIn(ExperimentalFoundationApi::class)
internal fun Modifier.locationsClickable(
    inSelectionMode: Boolean,
    onSelectionMode: () -> Unit,
    onItemSelected: () -> Unit,
    onLongClick: () -> Unit,
): Modifier {
   return this then if (inSelectionMode) {
        clickable { onSelectionMode() }
    } else {
        combinedClickable(
            onClick = { onItemSelected() },
            onLongClick = { onLongClick() }
        )
    }
}