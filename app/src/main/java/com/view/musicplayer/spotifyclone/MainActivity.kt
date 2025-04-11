package com.view.musicplayer.spotifyclone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.view.musicplayer.spotifyclone.navigation.ScreenRoute
import com.view.musicplayer.spotifyclone.screen.HomeScreen
import com.view.musicplayer.spotifyclone.screen.ProfileScreen
import com.view.musicplayer.spotifyclone.screen.SearchScreen
import com.view.musicplayer.spotifyclone.ui.theme.AndroidspotifycloneTheme
import com.view.musicplayer.spotifyclone.ui.theme.Black80
import com.view.musicplayer.spotifyclone.ui.theme.SpotifyAccent80
import com.view.musicplayer.spotifyclone.ui.theme.Transparent
import com.view.musicplayer.spotifyclone.ui.theme.White80

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidspotifycloneTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainPage()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidspotifycloneTheme {
        MainPage()
    }
}

@Composable
fun MainPage() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = ScreenRoute.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(ScreenRoute.Home.route) { HomeScreen() }
            composable(ScreenRoute.Search.route) { SearchScreen() }
            composable(ScreenRoute.Profile.route) { ProfileScreen() }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(ScreenRoute.Home, ScreenRoute.Search, ScreenRoute.Profile)
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(loadIconToVector(screen.icon), contentDescription = screen.title, modifier = Modifier
                    .width(30.dp)
                    .height(30.dp)) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Transparent,
                    selectedIconColor = SpotifyAccent80,
                    unselectedIconColor = Black80,
                    selectedTextColor = SpotifyAccent80,
                    unselectedTextColor = Black80
                ),
                onClick = {
                    if (currentRoute == screen.route) return@NavigationBarItem
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                interactionSource = remember { MutableInteractionSource() },
            )
        }
    }
}

@Composable
fun loadIconToVector(@DrawableRes icon: Int): ImageVector {
    return ImageVector.vectorResource(id = icon)
}

@Composable
fun EmptyView(padding: Int = 5) {
    Box(
        modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "~ No Data Available ~",
            color = White80,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = padding.dp)
        )
    }
}

@Composable
fun showLoading(padding: Int = 5) {
    Box(
        modifier = Modifier.fillMaxSize().padding(top = padding.dp, bottom = padding.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = SpotifyAccent80)
    }
}
