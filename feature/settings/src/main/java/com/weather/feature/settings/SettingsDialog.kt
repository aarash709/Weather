package com.weather.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import com.weather.model.TemperatureUnits
import com.weather.model.WindSpeedUnits

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingDialog(
    modifier: Modifier = Modifier,
    showDialog: Boolean = false,
    onDismissRequest: (Boolean) -> Unit,
    content: @Composable () -> Unit,
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismissRequest(false) },
            modifier = modifier,
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    content()
                }
            }
        }
    }
}

@Composable
fun DialogItem(itemName: String, onClick: () -> Unit) {
    Surface(
        onClick = {
            onClick()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Text(
            text = itemName,
            modifier = Modifier.padding(16.dp)
        )
    }
}