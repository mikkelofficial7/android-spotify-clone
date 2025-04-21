package com.view.musicplayer.spotifyclone.navigation

import androidx.annotation.DrawableRes
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import com.view.musicplayer.spotifyclone.R

sealed class ScreenRoute(val route: String, val title: String, @DrawableRes val icon: Int) {
    object Home : ScreenRoute("home", "Home Page", R.drawable.ic_home)
    object Search : ScreenRoute("search", "Search Music", R.drawable.ic_search)
    object Profile : ScreenRoute("profile", "Your Profile", R.drawable.is_spotify)
    object Login : ScreenRoute("login", "Login", 0)

    object AlbumDetail : ScreenRoute("album_detail/{albumGenre}", "Album Detail", 0) {
        fun route(albumGenre: String): String = "album_detail/$albumGenre"
    }
    object MusicDetail : ScreenRoute("music_detail/{musicTitle}", "Song Detail", 0) {
        fun route(musicTitle: String): String = "music_detail/$musicTitle"
    }
    object PlaylistDetail : ScreenRoute("playlist_detail/{id}", "Playlist Detail", 0) {
        fun route(id: String): String = "playlist_detail/$id"
    }
}

fun NavController.routeToMusicDetail(id: String) {
    this.navigate(ScreenRoute.MusicDetail.route(id)) {
        popUpTo(ScreenRoute.AlbumDetail.route) { saveState = true }
        launchSingleTop = true
        restoreState = false
    }
}

fun NavController.routeToAlbumDetail(id: String) {
    this.navigate(ScreenRoute.AlbumDetail.route(id)) {
        popUpTo(ScreenRoute.Search.route) { saveState = true }
        launchSingleTop = true
        restoreState = false
    }
}

fun NavController.routeToPlaylistDetail(id: String) {
    this.navigate(ScreenRoute.PlaylistDetail.route(id)) {
        popUpTo(ScreenRoute.Profile.route) { saveState = true }
        launchSingleTop = true
        restoreState = false
    }
}

fun NavController.routeToLogin() {
    this.navigate(ScreenRoute.Login.route) {
        launchSingleTop = true
        restoreState = false
    }
}

fun NavController.routeToHomePage() {
    this.navigate(ScreenRoute.Home.route) {
        launchSingleTop = true
        restoreState = false
    }
}