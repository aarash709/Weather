package com.weather.core.design.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.weather.core.design.theme.WeatherTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(
    modifier: Modifier = Modifier,
    text: String,
    scrollBehavior: TopAppBarScrollBehavior,
    onBackPressed: () -> Unit,
    navigationIcon: @Composable () -> Unit = {
        IconButton(onClick = { onBackPressed()}) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back Icon"
            )
        }
    },
    actions: @Composable () -> Unit = {},
) {
    MediumTopAppBar(
        title = {
            Text(
                text = text,
                modifier = Modifier,
                fontSize = 20.sp
            )
        },
        modifier = modifier,
        navigationIcon = { navigationIcon() },
        actions = { actions() },
        colors = TopAppBarDefaults
            .mediumTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                scrolledContainerColor = MaterialTheme.colorScheme.background
            ),
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun TopBarPreview() {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    WeatherTheme() {
        CustomTopBar(text = "Page name", onBackPressed = {}, scrollBehavior = scrollBehavior)
    }
}