package com.view.musicplayer.spotifyclone.navigation

import androidx.annotation.DrawableRes
import com.view.musicplayer.spotifyclone.R

sealed class ScreenRoute(val route: String, val title: String, @DrawableRes val icon: Int) {
    object Home : ScreenRoute("home", "Home Page", R.drawable.ic_home)
    object Search : ScreenRoute("search", "Search Music", R.drawable.ic_search)
    object Profile : ScreenRoute("profile", "Your Profile", R.drawable.is_spotify)
    object AlbumDetail : ScreenRoute("album_detail/{albumGenre}", "Album Detail", 0) {
        fun route(albumGenre: String): String = "album_detail/$albumGenre"
    }
    object MusicDetail : ScreenRoute("music_detail/{musicTitle}", "Song Detail", 0) {
        fun route(musicTitle: String): String = "music_detail/$musicTitle"
    }
}