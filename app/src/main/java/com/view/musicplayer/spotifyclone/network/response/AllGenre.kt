package com.view.musicplayer.spotifyclone.network.response

data class AllGenre (
    val genreSection: List<Genre>
)
data class Genre (
    val name: String,
    val color: String,
    val imageUrl: String
)