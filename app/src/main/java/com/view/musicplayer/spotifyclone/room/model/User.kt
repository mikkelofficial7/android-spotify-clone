package com.view.musicplayer.spotifyclone.room.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tbl_user")
data class User(
    @PrimaryKey(autoGenerate = true)
    var idPk: Int = 0,
    val fullname: String,
    val email: String,
    val age: String
) : Parcelable