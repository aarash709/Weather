package com.weather.feature.settings

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weather.core.design.theme.WeatherTheme

@Composable
fun Settings(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
) {
    Settings {
        onBackPressed()
    }
}

@Composable
fun Settings(onBackPressed: () -> Unit) {
    Box(modifier = Modifier.background(color = MaterialTheme.colors.background)) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.background,
                elevation = 0.dp
            ) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                    SettingsTopBar(onBackPressed)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Units", color = MaterialTheme.colors.onBackground.copy(alpha = .5f),
                fontSize = 12.sp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Temp Unit", color = MaterialTheme.colors.onBackground)
                Switch(false, onCheckedChange = {}, enabled = true)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Wind Speed Unit", color = MaterialTheme.colors.onBackground,)
                Switch(false, onCheckedChange = {}, enabled = true)
            }
            Text(text = "About", color = MaterialTheme.colors.onBackground.copy(alpha = .5f),
                fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "A Weather Demo App Developed By Arash Ebrahimzade.\n" +
                        "This app is not a production app and is in development" +
                        " using best practices of a real app.",
                color = MaterialTheme.colors.onBackground.copy(alpha = .5f),
                fontSize = 12.sp,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Weather Data from OpenWeather.com",
                color = MaterialTheme.colors.onBackground.copy(alpha = .5f),
                fontSize = 12.sp,
            )
        }
    }
}

@Composable
fun SettingsTopBar(onBackPressed: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(onClick = { onBackPressed() }) {
            Icon(
                modifier = Modifier,
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back Icon"
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Settings",
            fontSize = 18.sp
        )
    }
}

@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
private fun SettingsPreview() {
    WeatherTheme() {
        Settings { }
    }
}
