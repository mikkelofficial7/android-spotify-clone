package com.view.musicplayer.spotifyclone.navigation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.view.musicplayer.spotifyclone.R
import com.view.musicplayer.spotifyclone.ui.theme.Black80
import com.view.musicplayer.spotifyclone.ui.theme.SpotifyAccent80

sealed class ScreenRoute(val route: String, val title: String, @DrawableRes val icon: Int) {
    object Home : ScreenRoute("home", "Home Page", R.drawable.ic_home)
    object Search : ScreenRoute("search", "Search Music", R.drawable.ic_search)
    object Profile : ScreenRoute("profile", "Your Profile", R.drawable.is_spotify)
}

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(ScreenRoute.Home, ScreenRoute.Search, ScreenRoute.Profile)
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    BottomNavigation(backgroundColor = MaterialTheme.colors.surface) {
        items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(loadIconToVector(screen.icon), contentDescription = screen.title, modifier = Modifier.width(30.dp).height(30.dp)) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                selectedContentColor = SpotifyAccent80,
                unselectedContentColor = Black80,
                onClick = {
                    if (currentRoute == screen.route) return@BottomNavigationItem
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun loadIconToVector(@DrawableRes icon: Int): ImageVector {
    return ImageVector.vectorResource(id = icon)
}
