package com.view.musicplayer.spotifyclone.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.view.musicplayer.spotifyclone.network.response.Track

@Entity(tableName = "tbl_track_playlist")
data class PlaylistModel(
    @PrimaryKey(autoGenerate = true)
    val idPk: Int = 0,
    val playlistName: String,
    val playlistCreated: Long,
    val playlistIcon: String,
    val playlistTrack: ArrayList<Track>
)