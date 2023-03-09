package com.weather.core.design.theme

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

//Use in WeatherTheme
private val DarkColorPalette = darkColors(
    primary = Blue,
    surface = LightGray900,
    background = Black,
    onPrimary = White,
    onBackground = White,
    onSurface = White
)

private val LightColorPalette = lightColors(
    primary = Blue,
    surface = LightGray100,
    background = White,
    onPrimary = Black,
    onBackground = Black,
    onSurface = Black,
)

@Composable
fun WeatherTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
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
        Surface() {
            Box(modifier = Modifier) {
                Column(
                    Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        border = BorderStroke(1.dp, color = MaterialTheme.colors.onSurface),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "This is a surface")
                    }
                    Text(text = "this is a text")
                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(
                        onClick = {},
                        border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.onSurface),
                        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp)
                    ) {
                        Text(text = "this is text button")
                    }
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
}

