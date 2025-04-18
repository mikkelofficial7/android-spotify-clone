package com.view.musicplayer.spotifyclone.network.response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "tbl_genre")
data class Genre(
    @PrimaryKey(autoGenerate = false)
    var idPk: Int = 0,
    val name: String,
    val imageUrl: String,
    val color: String,
    val description: String,
    val listTrack: ArrayList<Track> = arrayListOf(),
) : Parcelable