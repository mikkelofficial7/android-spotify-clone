package com.view.musicplayer.spotifyclone.network.response

data class SearchRecommendResponse (
    val genreSection: List<Genre>,
    val songSection: List<Track>
)
data class Genre (
    val name: String,
    val color: String,
    val imageUrl: String,
    val songs: List<Song>
)

data class Song (
    val title: String,
    val artist: String,
    val albumArt: String
)