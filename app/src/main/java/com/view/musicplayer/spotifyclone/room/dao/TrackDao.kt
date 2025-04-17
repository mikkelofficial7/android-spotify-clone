package com.view.musicplayer.spotifyclone.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.view.musicplayer.spotifyclone.network.response.Track
import com.view.musicplayer.spotifyclone.room.model.FavoriteTrack

@Dao
interface TrackDao {
    @Insert
    suspend fun insert(track: FavoriteTrack)
    @Query("SELECT * FROM tbl_track_favorite")
    suspend fun getAllFavoriteTrack(): List<FavoriteTrack>

    @Query("SELECT * FROM tbl_track_favorite where id = :id")
    suspend fun getFavoriteTrackById(id: String): FavoriteTrack?
    @Delete
    suspend fun delete(track: FavoriteTrack)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTrack(track: Track)

    @Query("SELECT * FROM tbl_item_track where id = :id")
    suspend fun getTrackById(id: String): Track?

    @Query("SELECT * FROM tbl_item_track where idPk = :id")
    suspend fun getTrackByIdPrimary(id: Int): Track?
}