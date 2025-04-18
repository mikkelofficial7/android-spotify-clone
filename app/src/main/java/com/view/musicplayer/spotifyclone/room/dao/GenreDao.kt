package com.view.musicplayer.spotifyclone.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.view.musicplayer.spotifyclone.network.response.Genre

@Dao
interface GenreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(genre: Genre)
    @Query("SELECT * FROM tbl_genre")
    suspend fun getAllGenre(): List<Genre>

    @Query("SELECT * FROM tbl_genre where name = :name")
    suspend fun getGenreByName(name: String): Genre?

    @Delete
    suspend fun delete(genre: Genre)
}