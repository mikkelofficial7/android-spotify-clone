package com.view.musicplayer.spotifyclone.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.view.musicplayer.spotifyclone.network.response.OpenAIFlagDb

@Dao
interface OpenAiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(db: OpenAIFlagDb)
    @Query("SELECT * FROM tbl_flag_open_ai")
    suspend fun getAllOpenAiFlag(): List<OpenAIFlagDb>?

    @Delete
    suspend fun delete(db: OpenAIFlagDb)
}