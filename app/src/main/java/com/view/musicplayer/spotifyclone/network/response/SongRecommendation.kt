package com.view.musicplayer.spotifyclone.network.response

data class SongRecommendation(
    val id: String,
    val title: String,
    val tracks: List<Track>
)

data class Track (
    val id: String,
    val title: String,
    val artist: String,
    val releaseDate: String,
    val totalListener: Long,
    val description: String,
    val imageUrl: String,
    val streamedUrl: String,
    val duration: Long,
    val genre: String
)