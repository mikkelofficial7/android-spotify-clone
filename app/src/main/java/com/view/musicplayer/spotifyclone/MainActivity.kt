package com.view.musicplayer.spotifyclone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.view.musicplayer.spotifyclone.navigation.ScreenRoute
import com.view.musicplayer.spotifyclone.network.response.Track
import com.view.musicplayer.spotifyclone.screen.HomeScreen
import com.view.musicplayer.spotifyclone.screen.ProfileScreen
import com.view.musicplayer.spotifyclone.screen.SearchScreen
import com.view.musicplayer.spotifyclone.screen.shared.loadIconToVector
import com.view.musicplayer.spotifyclone.ui.theme.AndroidspotifycloneTheme
import com.view.musicplayer.spotifyclone.ui.theme.Black80
import com.view.musicplayer.spotifyclone.ui.theme.SpotifyAccent80
import com.view.musicplayer.spotifyclone.ui.theme.Transparent

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
    var isShowPlayerButton by rememberSaveable { mutableStateOf(false) }
    var currentPlaying by remember { mutableStateOf(Track.empty) }

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = ScreenRoute.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(ScreenRoute.Home.route) {
                HomeScreen(isShowPlayerButton = isShowPlayerButton) {
                   if (currentPlaying.id == it.id) {
                       currentPlaying = Track.empty
                       isShowPlayerButton = !isShowPlayerButton
                   } else {
                       currentPlaying = it
                       isShowPlayerButton = true
                   }
                }
            }
            composable(ScreenRoute.Search.route) {
                SearchScreen(isShowPlayerButton = isShowPlayerButton) {
                    if (currentPlaying.id == it.id) {
                        currentPlaying = Track.empty
                        isShowPlayerButton = !isShowPlayerButton
                    } else {
                        currentPlaying = it
                        isShowPlayerButton = true
                    }
                }
            }
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
