package com.view.musicplayer.spotifyclone.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.view.musicplayer.spotifyclone.room.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)
    @Query("SELECT * FROM tbl_user")
    suspend fun getUserData(): User?

    @Delete
    suspend fun delete(user: User)
}