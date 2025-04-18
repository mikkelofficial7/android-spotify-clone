package com.view.musicplayer.spotifyclone.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.view.musicplayer.spotifyclone.network.response.Genre
import com.view.musicplayer.spotifyclone.network.response.SongRecommendation

@Dao
interface RecommendationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recommendation: SongRecommendation)

    @Query("SELECT * FROM tbl_track_recommendation")
    suspend fun getAllRecommendation(): List<SongRecommendation>

    @Delete
    suspend fun delete(recommendation: SongRecommendation)
}