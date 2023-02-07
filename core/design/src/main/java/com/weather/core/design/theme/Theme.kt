package com.weather.core.design.theme

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

//Use in WeatherTheme
private val DarkColorPalette = darkColors(
    primary = Black,
    primaryVariant = Purple700,
    secondary = White,
    onPrimary = White
)

private val LightColorPalette = lightColors(
    primary = White,
    primaryVariant = White,
    secondary = White,
    secondaryVariant = White,
    surface = White,
    background = White,
    onPrimary = Black,
    onSecondary = Black,
    onBackground = Black,
    onSurface = Black,
)

@Composable
fun WeatherTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        LightColorPalette
    } else {
        LightColorPalette
    }
    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

@Preview(showBackground = true, showSystemUi = false, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun ThemeTest() {
    WeatherTheme {
        Surface {
            Column(Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "this is a text")
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {},
                    elevation = ButtonDefaults.elevation(defaultElevation = 0.dp)
                ) {
                    Text(text = "this is a button")
                }
            }
        }
    }
}

