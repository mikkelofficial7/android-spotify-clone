package com.view.musicplayer.spotifyclone.network.response

data class HomePageResponse (
    val title: String,
    val description: String,
    val tracks: List<Track>? = listOf()
)

data class Track (
    val id: String,
    val title: String,
    val artist: String,
    val albumArt: String,
    val duration: Long,
    val isExplicit: Boolean
)