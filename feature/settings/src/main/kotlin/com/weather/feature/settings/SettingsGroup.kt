package com.weather.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.experiment.weather.core.common.R
import com.weather.model.TemperatureUnits
import com.weather.model.WindSpeedUnits

@Composable
fun SettingGroup(
    modifier: Modifier = Modifier,
    groupName: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = groupName,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = .75f),
            fontSize = 14.sp
        )
        content()
    }
}

@Composable
fun TemperatureSettings(
    modifier: Modifier = Modifier,
    title: String,
    currentSettingsName: String,
    setTemperature: (TemperatureUnits) -> Unit,
) {
    var shouldShowOptions by remember {
        mutableStateOf(false)
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { shouldShowOptions = true }
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(title, color = MaterialTheme.colorScheme.onBackground)
        Text(
            text = currentSettingsName,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
        )
        TemperatureOptions(
            onSetValue = { shouldShowOptions = false },
            currentTempUnit = currentSettingsName,
            shouldShowOptions = shouldShowOptions,
            setTemperature = setTemperature,
            onDismissRequest = { shouldShowOptions = false }
        )
    }
}

@Composable
fun WindSpeedSettings(modifier: Modifier = Modifier) {

}
@Composable
fun WindSpeedSection(
    title: String,
    windSpeedUnitName: String,
    setWindSpeed: (WindSpeedUnits) -> Unit,
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    SettingItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, color = MaterialTheme.colorScheme.onBackground)
            Text(
                text = windSpeedUnitName,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            )
        }
        WindSpeedMenu(
            expanded = expanded,
            setWindSpeed = setWindSpeed,
            setExpanded = { expanded = it })
    }
}


@Composable
private fun WindSpeedMenu(
    expanded: Boolean,
    setWindSpeed: (WindSpeedUnits) -> Unit,
    setExpanded: (Boolean) -> Unit,
) {
    Column {
        SettingDialog(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets(left = 100, right = 100)),
            showDialog = expanded,
            onDismissRequest = {
                setExpanded(false)
            }
        ) {
            DialogItem(
                itemName = stringResource(id = R.string.kilometer_per_hour_symbol),
                onClick = {
                    setWindSpeed(WindSpeedUnits.KM)
                    setExpanded(false)
                }
            )
            DialogItem(
                itemName = stringResource(id = R.string.meters_per_second_symbol),
                onClick = {
                    setWindSpeed(WindSpeedUnits.MS)
                    setExpanded(false)
                }
            )
            DialogItem(
                itemName = stringResource(id = R.string.miles_per_hour_symbol),
                onClick = {
                    setWindSpeed(WindSpeedUnits.MPH)
                    setExpanded(false)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TemperatureOptions(
    modifier: Modifier = Modifier,
    onSetValue: () -> Unit,
    currentTempUnit: String,
    shouldShowOptions: Boolean,
    dialogInsets: WindowInsets = WindowInsets(left = 100, right = 100),
    setTemperature: (TemperatureUnits) -> Unit,
    onDismissRequest: () -> Unit,
) {
    if (shouldShowOptions) {
        AlertDialog(
            onDismissRequest = { onDismissRequest() },
            modifier = Modifier
                .windowInsetsPadding(dialogInsets),
        ) {
            Surface(
                modifier = modifier,
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    SettingOptionItem(
                        title = "°C",
                        isSelected = currentTempUnit == "°C"
                    ) {
                        setTemperature(TemperatureUnits.C)
                        onSetValue()
                    }
                    SettingOptionItem(
                        title = "°F",
                        isSelected = currentTempUnit == "°F"
                    ) {
                        setTemperature(TemperatureUnits.F)
                        onSetValue()
                    }
                }
            }
        }
    }
}

@Composable
fun SettingItem(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {
        content()
    }
}

@Composable
fun SettingOptionItem(
    modifier: Modifier = Modifier,
    title: String,
    isSelected: Boolean,
    onSetOption: () -> Unit,
) {
    Surface(
        onClick = { onSetOption() },
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) else Color.Transparent
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title)
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "selected icon"
                )
            }
        }
    }
}