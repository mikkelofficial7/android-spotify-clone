package com.view.musicplayer.spotifyclone.room.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.view.musicplayer.spotifyclone.network.response.Track

class PlaylistConverter {
    @TypeConverter
    fun fromTrackList(value: ArrayList<Track>): String {
        return Gson().toJson(value)
    }
    @TypeConverter
    fun toTrackList(value: String): ArrayList<Track> {
        val listType = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(value, listType)
    }
}