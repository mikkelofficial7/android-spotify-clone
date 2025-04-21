package com.view.musicplayer.spotifyclone.network.response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tbl_flag_open_ai")
data class OpenAIFlagDb(
    @PrimaryKey(autoGenerate = true)
    var idPk: Int = 0,
    val lastHitDate: String
) : Parcelable