package com.view.musicplayer.spotifyclone.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.view.musicplayer.spotifyclone.network.response.Genre
import com.view.musicplayer.spotifyclone.network.response.SongRecommendation
import com.view.musicplayer.spotifyclone.network.response.Track
import com.view.musicplayer.spotifyclone.room.converters.PlaylistConverter
import com.view.musicplayer.spotifyclone.room.dao.GenreDao
import com.view.musicplayer.spotifyclone.room.dao.PlaylistDao
import com.view.musicplayer.spotifyclone.room.dao.RecommendationDao
import com.view.musicplayer.spotifyclone.room.dao.TrackDao
import com.view.musicplayer.spotifyclone.room.model.FavoriteTrack
import com.view.musicplayer.spotifyclone.room.model.PlaylistModel

@Database(entities = [
    FavoriteTrack::class,
    PlaylistModel::class,
    Track::class,
    Genre::class,
    SongRecommendation::class
], version = 1)
@TypeConverters(PlaylistConverter::class)
abstract class AppDb : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun genreDao(): GenreDao
    abstract fun recommendDao(): RecommendationDao
}