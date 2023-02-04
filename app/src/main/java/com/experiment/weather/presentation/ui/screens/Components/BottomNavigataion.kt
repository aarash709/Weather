package com.experiment.weather.presentation.ui.screens.Components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

/*
BottomAppBar(
modifier = Modifier,
backgroundColor = MaterialTheme.colors.primary,
) {
    Icon(painter = painterResource(id = R.drawable.ic_add),
        contentDescription = "addButton", modifier = Modifier.clickable {
            navHost.navigate(Screens.addCityPage.route){
                popUpTo(Screens.currentPage.route)
                launchSingleTop = true
            }

        })
    Icon(painter = painterResource(id = R.drawable.ic_options),
        contentDescription = "showAllCitiesButton", modifier = Modifier.clickable {
            navHost.navigate(Screens.allCities.route){
                launchSingleTop = true
            }
        })
    Icon(
        painter = painterResource(id = R.drawable.ic_refresh),
        contentDescription = "refreshButton", modifier = Modifier.clickable {
            navHost.navigate(Screens.addCityPage.route){
                launchSingleTop = true
            }
        })
}*/
data class NavigationItem(
    val name: String,
    val route: String,
    val icon: Painter,

    )

@Composable
fun BottomNavigationBar(
    navigationItems: List<NavigationItem>? = emptyList(),
    navController: NavController,
    onItemClicked: (NavigationItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    BottomNavigation(
        modifier = Modifier,
    ) {
        navigationItems?.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            BottomNavigationItem(
                selected = selected,
                onClick = { onItemClicked(item) },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.Gray,
                icon = {
                    Column(modifier = Modifier,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = item.icon,
                            contentDescription = item.name
                        )
                        if (selected) Text(text = item.name,
                        textAlign = TextAlign.Center)
                    }
                }
            )
        }
    }
}