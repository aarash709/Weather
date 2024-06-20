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
fun WindSpeedSettings(
    modifier: Modifier = Modifier,
    title: String,
    currentSettingsName: String,
    setWindSpeed: (WindSpeedUnits) -> Unit,
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
        WindSpeedOptions(
            onSetValue = { shouldShowOptions = false },
            currentWindSpeed = currentSettingsName,
            shouldShowOptions = shouldShowOptions,
            setWindSpeed = setWindSpeed,
            onDismissRequest = { shouldShowOptions = false })
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WindSpeedOptions(
    modifier: Modifier = Modifier,
    onSetValue: () -> Unit,
    currentWindSpeed: String,
    shouldShowOptions: Boolean,
    dialogInsets: WindowInsets = WindowInsets(left = 100, right = 100),
    setWindSpeed: (WindSpeedUnits) -> Unit,
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
                        title = stringResource(id = R.string.kilometer_per_hour_symbol),
                        isSelected = currentWindSpeed == stringResource(id = R.string.kilometer_per_hour)
                    ) {
                        setWindSpeed(WindSpeedUnits.KM)
                        onSetValue()
                    }
                    SettingOptionItem(
                        title = stringResource(id = R.string.miles_per_hour_symbol),
                        isSelected = currentWindSpeed == stringResource(id = R.string.miles_per_hour)
                    ) {
                        setWindSpeed(WindSpeedUnits.MPH)
                        onSetValue()
                    }
                    SettingOptionItem(
                        title = stringResource(id = R.string.meters_per_second_symbol),
                        isSelected = currentWindSpeed == stringResource(id = R.string.meters_per_second)
                    ) {
                        setWindSpeed(WindSpeedUnits.MS)
                        onSetValue()
                    }
                }
            }
        }
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