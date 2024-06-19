package com.weather.feature.forecast.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.weather.core.design.theme.WeatherTheme
import com.weather.feature.forecast.R

@Composable
fun WeatherBackground(
    modifier: Modifier = Modifier,
    conditionID: Int,
    isDay: Boolean,
    isDawn: Boolean,
    content: @Composable () -> Unit,
) {
    var condition by remember {
        mutableIntStateOf(1)
    }
    LaunchedEffect(key1 = conditionID) {
        //more details -> https://openweathermap.org/weather-conditions#Weather-Condition-Codes-2
        when (conditionID) {
            800 -> {
                condition =
                    if (isDay) {
                        if (isDawn) 8
                        else 1
                    } else 7
            }  // clear day or night or dawn
            in 200..232 -> condition = 2 //Thunderstorm
            in 300..321 -> condition = 3 //Drizzle
            in 500..531 -> condition = 3 //Rain
            in 600..622 -> condition = 4 //Snow
            in 701..787 -> condition = 5 //Atmosphere(only fog is shown. will add more)
            in 801..804 -> condition = 6 //clouds
        }
    }
    Box(modifier = modifier
        .drawWithCache {
            onDrawBehind {
            }
        }) {
        AnimatedContent(
            targetState = condition,
            transitionSpec = { fadeIn() togetherWith fadeOut(tween(300)) },
            label = "background condition image"
        ) { condition ->
            when (condition) {
                1 -> Image(
                    painter = painterResource(id = R.drawable.day_clear),
                    contentScale = ContentScale.Crop,
                    contentDescription = "image of clouds"
                )

                2 -> Image(
                    painter = painterResource(id = R.drawable.thunderstorm),
                    contentScale = ContentScale.Crop,
                    contentDescription = "image of clouds"
                )

                3 -> Image(
                    painter = painterResource(id = R.drawable.drizzel),
                    contentScale = ContentScale.Crop,
                    contentDescription = "image of clouds"
                )

                4 -> Image(
                    painter = painterResource(id = R.drawable.snow),
                    contentScale = ContentScale.Crop,
                    contentDescription = "image of clouds"
                )

                5 -> Image(
                    painter = painterResource(id = R.drawable.fog),
                    contentScale = ContentScale.Crop,
                    contentDescription = "image of clouds"
                )

                6 -> Image(
                    painter = painterResource(id = R.drawable.clouds),
                    contentScale = ContentScale.Crop,
                    contentDescription = "image of clouds"
                )

                7 -> Image(
                    painter = painterResource(id = R.drawable.night_clear),
                    contentScale = ContentScale.Crop,
                    contentDescription = "image of clouds"
                )

                8 -> Image(
                    painter = painterResource(id = R.drawable.dawn),
                    contentScale = ContentScale.Crop,
                    contentDescription = "image of clouds"
                )
            }
        }
        content()
    }
}

@Preview
@Composable
private fun PreviewImage() {
    WeatherTheme {
        WeatherBackground(conditionID = 804, isDay = false, isDawn = false) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Row {
                        Text("this is a text")
                    }
                }
            }
        }
    }
}