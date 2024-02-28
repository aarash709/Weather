package com.weather.feature.managelocations.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChecklistRtl
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.weather.core.design.components.CustomTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LocationsTopbar(
    isInEditMode: Boolean,
    selectedCitySize: Int,
    scrollBehavior: TopAppBarScrollBehavior,
    onBackPressed: () -> Unit,
    onIsAllSelected: () -> Unit,
    onEmptyCitySelection: () -> Unit,
) {
    CustomTopBar(
        modifier = Modifier
            .fillMaxWidth(),
        text = if (isInEditMode) {
            "Selected Items ${selectedCitySize}"
        } else {
            "Manage Locations"
        },
        onBackPressed = { onBackPressed() },
        navigationIcon = {
            AnimatedContent(
                targetState = isInEditMode,
                label = "Top bar Icon"
            ) { isInEditMode ->
                if (isInEditMode) {
                    IconButton(onClick = { onEmptyCitySelection() }) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            tint = MaterialTheme.colorScheme.onBackground,
                            contentDescription = "Clear selection Button"
                        )
                    }
                } else {
                    IconButton(onClick = { onBackPressed() }) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            tint = MaterialTheme.colorScheme.onBackground,
                            contentDescription = "back icon"
                        )
                    }
                }
            }
        },
        actions = {
            if (isInEditMode) IconButton(onClick = { onIsAllSelected() }) {
                Icon(
                    imageVector = Icons.Default.ChecklistRtl,
                    contentDescription = "Select all button"
                )
            }
        },
        scrollBehavior = scrollBehavior
    )

}