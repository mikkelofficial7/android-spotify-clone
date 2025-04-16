package com.view.musicplayer.spotifyclone.viewmodel

import android.content.Context
import com.view.musicplayer.spotifyclone.ext.SingleLiveEvent
import com.view.musicplayer.spotifyclone.ext.flowOnValue
import com.view.musicplayer.spotifyclone.network.Api
import com.view.musicplayer.spotifyclone.network.response.Track
import com.view.musicplayer.spotifyclone.room.AppDb
import com.view.musicplayer.spotifyclone.room.model.PlaylistModel
import com.view.musicplayer.spotifyclone.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PlaylistDetailViewModel(private val db: AppDb): BaseViewModel<Any?>() {
    val playlistData = SingleLiveEvent<PlaylistModel>()
    val favoriteTrack = SingleLiveEvent<List<Track>>()

    internal fun getAllPlaylistById(context: Context, id: String) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                val playlist = db.playlistDao().getPlaylistTrackById(id.toInt())
                playlistData.postValue(playlist)
            }
        }
    }

    internal fun getAllFavoriteTrack(context: Context) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                flowOnValue(db.trackDao().getAllFavoriteTrack()).collectLatest {
                    favoriteTrack.postValue(it.map { it.toTrack })
                }
            }
        }
    }

    internal fun addOrRemoveFavorite(context: Context, track: Track) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                val dataExist = db.trackDao().getFavoriteTrackById(track.id)

                dataExist?.let {
                    flowOnValue(db.trackDao().delete(it))
                } ?: kotlin.run {
                    flowOnValue(db.trackDao().insert(track.toFavoriteTrack))
                }

                getAllFavoriteTrack(context)
            }
        }
    }
}