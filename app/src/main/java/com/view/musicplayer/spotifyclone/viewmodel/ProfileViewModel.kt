package com.view.musicplayer.spotifyclone.viewmodel

import android.content.Context
import com.view.musicplayer.spotifyclone.ext.SingleLiveEvent
import com.view.musicplayer.spotifyclone.ext.convertToUri
import com.view.musicplayer.spotifyclone.ext.flowOnValue
import com.view.musicplayer.spotifyclone.network.response.Track
import com.view.musicplayer.spotifyclone.room.AppDb
import com.view.musicplayer.spotifyclone.room.model.FavoriteTrack
import com.view.musicplayer.spotifyclone.room.model.PlaylistModel
import com.view.musicplayer.spotifyclone.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileViewModel(private val db: AppDb): BaseViewModel<Any?>() {
    val listFavTrack = SingleLiveEvent<List<FavoriteTrack>>()
    val listPlaylist = SingleLiveEvent<List<PlaylistModel>>()

    internal fun getAllFavoriteTrack(context: Context) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                flowOnValue(db.trackDao().getAllFavoriteTrack()).collectLatest { response ->
                    isLoadingEvent.postValue(false)
                    listFavTrack.postValue(response)
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

                isLoadingEvent.postValue(false)
                getAllFavoriteTrack(context)
            }
        }
    }

    internal fun getAllPlaylist(context: Context) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                flowOnValue(db.playlistDao().getAllPlaylistTrack()).collectLatest {
                    isLoadingEvent.postValue(false)
                    listPlaylist.postValue(it)
                }
            }
        }
    }

    internal fun addPlaylist(context: Context, playlistName: String, playlistIcon: Int) {
        val newPlaylist = PlaylistModel(
            playlistName = playlistName,
            playlistIcon = playlistIcon.convertToUri(context).toString(),
            playlistCreated = System.currentTimeMillis(),
            playlistTrack = arrayListOf()
        )

        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                flowOnValue(db.playlistDao().insert(newPlaylist))
                isLoadingEvent.postValue(false)

                getAllPlaylist(context)
            }
        }
    }

    internal fun removePlaylist(context: Context, playlist: PlaylistModel) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                flowOnValue(db.playlistDao().delete(playlist))
                isLoadingEvent.postValue(false)

                getAllPlaylist(context)
            }
        }
    }
}