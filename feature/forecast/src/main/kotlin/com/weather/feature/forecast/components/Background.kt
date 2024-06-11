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
    content: @Composable () -> Unit,
) {
    val clearDay = painterResource(id = R.drawable.day_clear)
    val thunderstorm = painterResource(id = R.drawable.thunderstorm)
    val drizzleAndRain = painterResource(id = R.drawable.drizzel)
    val snow = painterResource(id = R.drawable.snow)
    val fog = painterResource(id = R.drawable.fog)
    val clouds = painterResource(id = R.drawable.clouds)
    val dawn = painterResource(id = R.drawable.dawn)
    val clearNightSkyWithStars = painterResource(id = R.drawable.night_clear)
    var condition by remember {
        mutableIntStateOf(1)
    }
    LaunchedEffect(key1 = conditionID) {
        //more details -> https://openweathermap.org/weather-conditions#Weather-Condition-Codes-2
        when (conditionID) {
            800 -> condition = 1 // clear day
            in 200..232 -> condition = 2 //Thunderstorm
            in 300..321 -> condition = 3 //Drizzle
            in 500..531 -> condition = 3 //Rain
            in 600..622 -> condition = 4 //Snow
            in 701..787-> condition = 5 //Atmosphere(only fog is shown. will add more)
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
                    painter = clearDay,
                    contentScale = ContentScale.Crop,
                    contentDescription = "image of clouds"
                )

                2 -> Image(
                    painter = thunderstorm,
                    contentScale = ContentScale.Crop,
                    contentDescription = "image of clouds"
                )

                3 -> Image(
                    painter = drizzleAndRain,
                    contentScale = ContentScale.Crop,
                    contentDescription = "image of clouds"
                )

                4 -> Image(
                    painter = snow,
                    contentScale = ContentScale.Crop,
                    contentDescription = "image of clouds"
                )
                5 -> Image(
                    painter = fog,
                    contentScale = ContentScale.Crop,
                    contentDescription = "image of clouds"
                )
                6 -> Image(
                    painter = clouds,
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
        WeatherBackground(conditionID = 804) {
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