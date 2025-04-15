package com.view.musicplayer.spotifyclone.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.view.musicplayer.spotifyclone.network.response.Track

@Entity(tableName = "tbl_track_favorite")
data class FavoriteTrack(
    @PrimaryKey(autoGenerate = true)
    val idPk: Int = 0,
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
    val toTrack: Track
        get() {
            return Track(id, title, artist, releaseDate, totalListener, description, imageUrl, streamedUrl, duration, genre)
        }
}