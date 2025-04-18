package com.view.musicplayer.spotifyclone.network.response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.view.musicplayer.spotifyclone.room.model.FavoriteTrack
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tbl_track_recommendation")
data class SongRecommendation(
    @PrimaryKey(autoGenerate = false)
    var idPk: Int = 0,
    val id: String,
    val title: String,
    val listTrack: ArrayList<Track>? = arrayListOf()
) : Parcelable

@Parcelize
@Entity(tableName = "tbl_item_track")
data class Track (
    @PrimaryKey(autoGenerate = false)
    var idPk: Int = 0,
    val id: String,
    val title: String,
    val artist: String,
    val releaseDate: String,
    val totalListener: Long,
    val description: String,
    val imageUrl: String,
    val streamedUrl: String,
    val duration: Long,
    val genre: String
): Parcelable {
    val toFavoriteTrack: FavoriteTrack
        get() {
            return FavoriteTrack(
                idPk = idPk,
                id = id,
                title = title,
                artist = artist,
                releaseDate = releaseDate,
                totalListener = totalListener,
                description = description,
                imageUrl = imageUrl,
                streamedUrl = streamedUrl,
                duration = duration,
                genre = genre
            )
        }
    companion object {
        val empty: Track
            get() {
                return Track(
                    -1,
                    "",
                    "",
                    "",
                    "",
                    -1,
                    "",
                    "",
                    "",
                    -1,
                    ""
                )
            }
    }
}