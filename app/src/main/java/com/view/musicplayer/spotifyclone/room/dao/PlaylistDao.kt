package com.view.musicplayer.spotifyclone.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.view.musicplayer.spotifyclone.network.response.Track
import com.view.musicplayer.spotifyclone.room.model.PlaylistModel

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(playlist: PlaylistModel)
    @Query("SELECT * FROM tbl_track_playlist")
    suspend fun getAllPlaylistTrack(): List<PlaylistModel>

    @Query("SELECT * FROM tbl_track_playlist where idPk = :id")
    suspend fun getPlaylistTrackById(id: Int): PlaylistModel?

    @Query("SELECT * FROM tbl_track_playlist where playlistName = :name")
    suspend fun getPlaylistTrackByName(name: String): PlaylistModel?

    @Query("UPDATE tbl_track_playlist SET playlistTrack = :trackListJson WHERE idPk = :id")
    suspend fun updateTracksInPlaylist(id: Int, trackListJson: String)
    @Delete
    suspend fun delete(playlist: PlaylistModel)
}