package com.view.musicplayer.spotifyclone.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.view.musicplayer.spotifyclone.room.dao.TrackDao
import com.view.musicplayer.spotifyclone.room.model.FavoriteTrack

@Database(entities = [FavoriteTrack::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract fun trackDao(): TrackDao
}