package com.view.musicplayer.spotifyclone.network.response

import com.view.musicplayer.spotifyclone.room.model.FavoriteTrack

data class SongRecommendation(
    val id: String,
    val title: String
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
) {
    val toFavoriteTrack: FavoriteTrack
        get() {
            return FavoriteTrack(
                id = id,
                title = title,
                artist = artist,
                releaseDate = releaseDate,
                totalListener = totalListener,
                description = description,
                imageUrl = imageUrl,
                streamedUrl = streamedUrl,
                duration = duration,
                genre = genre
            )
        }
    companion object {
        val empty: Track
            get() {
                return Track(
                    "",
                    "",
                    "",
                    "",
                    -1,
                    "",
                    "",
                    "",
                    -1,
                    ""
                )
            }
    }
}