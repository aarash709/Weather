package com.weather.feature.forecast.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weather.core.design.theme.WeatherTheme
import com.weather.feature.forecast.R

@Composable
fun WeatherBackground(
    modifier: Modifier = Modifier,
//    @DrawableRes background: Int,
    showBackground: Boolean = true,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        if (showBackground) {
            val contentScale = ContentScale.Crop
            Image(
                painter = painterResource(id = R.drawable.dawn),
                contentScale = contentScale,
                contentDescription = "background image"
            )
        }
        content()
    }
}

@Preview
@Composable
private fun PreviewImage() {
    WeatherTheme {
        WeatherBackground(/*background = R.drawable.dawn,*/ showBackground = false) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                ) {
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("this is a text")
                    }
                }
            }
        }
    }
}