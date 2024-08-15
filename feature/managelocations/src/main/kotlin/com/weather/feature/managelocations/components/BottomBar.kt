package com.weather.feature.managelocations.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.weather.feature.managelocations.BottomBarItem

@Composable
internal fun LocationsBottomBar(
    isInEditMode: Boolean,
    selectedCitySize: Int,
    onDeleteItem: () -> Unit,
    onEmptyCitySelection: () -> Unit,
    onSetFavoriteItem: () -> Unit,
) {
    AnimatedVisibility(
        visible = isInEditMode,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
        label = "bottom bar content"
    ) {
        BottomAppBar(
            tonalElevation = 0.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                BottomBarItem(
                    buttonName = "Delete",
                    imageVector = Icons.Default.DeleteOutline,
                    onClick = {
                        onDeleteItem()
                        onEmptyCitySelection()
                    })
                BottomBarItem(
                    buttonName = "Favorite",
                    imageVector = Icons.Default.StarBorder,
                    enabled = selectedCitySize < 2,
                    onClick = { onSetFavoriteItem() })
            }
        }
    }

}