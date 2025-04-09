package com.view.musicplayer.spotifyclone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.view.musicplayer.spotifyclone.navigation.BottomNavBar
import com.view.musicplayer.spotifyclone.navigation.ScreenRoute
import com.view.musicplayer.spotifyclone.screen.HomeScreen
import com.view.musicplayer.spotifyclone.screen.ProfileScreen
import com.view.musicplayer.spotifyclone.screen.SearchScreen
import com.view.musicplayer.spotifyclone.ui.theme.AndroidspotifycloneTheme

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