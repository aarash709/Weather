package com.weather.feature.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.weather.core.design.theme.WeatherTheme

@Composable
fun FiveDaySearch() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(5) {
            Item()
        }
    }
}

@Composable
private fun Item() {
    Column(
        modifier = Modifier.padding(vertical = 0.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Today")
        Icon(imageVector = Icons.Outlined.WbSunny, contentDescription = "Condition Icon")
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = "20°")
        Text(text = "11°")
    }
}

@PreviewLightDark
@Composable
private fun SearchFiveDayPrev() {
    WeatherTheme {
        Surface(modifier = Modifier.padding(horizontal = 0.dp)) {
            FiveDaySearch()
        }
    }
}