package com.weather.core.design.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weather.core.design.theme.WeatherTheme

@Composable
fun CustomTopBar(
    modifier: Modifier = Modifier,
    text: String,
    backgroundColor: Color = MaterialTheme.colors.background,
    elevation: Dp = 0.dp,
    onBackPressed: () -> Unit,
) {
    val contentPadding = PaddingValues(horizontal = 0.dp)
    TopAppBar(
        modifier = modifier,
        backgroundColor = backgroundColor,
        elevation = elevation,
        contentPadding = contentPadding,
    ) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
            TopBarSimple(
                text = text,
                onBackPressed = onBackPressed
            )
        }
    }
}

@Composable
fun TopBarSimple(
    text: String,
    onBackPressed: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            modifier = Modifier.clickable() { onBackPressed() },
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back Icon"
        )
        Spacer(modifier = Modifier.width(32.dp))
        Text(
            text = text,
            fontSize = 18.sp
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun TopBarPreview() {
    WeatherTheme() {
        CustomTopBar(text = "Text") {}
    }
}