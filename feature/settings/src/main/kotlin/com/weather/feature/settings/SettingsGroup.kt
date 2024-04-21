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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = .75f),
            fontSize = 14.sp
        )
        content()
    }
}

@Composable
fun TemperatureSection(
    title: String,
    tempUnitName: String,
    setTemperature: (TemperatureUnits) -> Unit,
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    SettingItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true }
            .padding(vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, color = MaterialTheme.colorScheme.onBackground)
            Text(
                text = tempUnitName,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            )
        }
        TemperatureDialogMenu(
            expanded = expanded,
            setTemperature = setTemperature,
            setExpanded = { expanded = it })
    }
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
            .padding(vertical = 8.dp)
    ) {
        Text(text = title, color = MaterialTheme.colorScheme.onBackground)
        WindSpeedMenu(
            tempUnitName = windSpeedUnitName,
            expanded = expanded,
            setWindSpeed = setWindSpeed,
            setExpanded = { expanded = it })
    }
}


@Composable
private fun WindSpeedMenu(
    tempUnitName: String,
    expanded: Boolean,
    setWindSpeed: (WindSpeedUnits) -> Unit,
    setExpanded: (Boolean) -> Unit,
) {
    Column {
        Text(
            text = tempUnitName,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
        )
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

@Composable
private fun TemperatureDialogMenu(
    expanded: Boolean,
    setTemperature: (TemperatureUnits) -> Unit,
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
            Surface(
                onClick = {
                    setTemperature(TemperatureUnits.C)
                    setExpanded(false)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                Text(
                    text = "°C",
                    modifier = Modifier.padding(16.dp)
                )
            }
            Surface(
                onClick = {
                    setTemperature(TemperatureUnits.F)
                    setExpanded(false)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                Text(
                    text = "°F",
                    modifier = Modifier.padding(16.dp)
                )
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
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {
        content()
    }
}