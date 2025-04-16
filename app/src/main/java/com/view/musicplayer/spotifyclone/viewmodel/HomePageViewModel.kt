package com.view.musicplayer.spotifyclone.viewmodel

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.view.musicplayer.spotifyclone.ext.SingleLiveEvent
import com.view.musicplayer.spotifyclone.ext.flowOnValue
import com.view.musicplayer.spotifyclone.network.Api
import com.view.musicplayer.spotifyclone.network.response.SongRecommendation
import com.view.musicplayer.spotifyclone.network.response.Track
import com.view.musicplayer.spotifyclone.room.AppDb
import com.view.musicplayer.spotifyclone.room.model.PlaylistModel
import com.view.musicplayer.spotifyclone.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomePageViewModel(private val api: Api, private val db: AppDb): BaseViewModel<Any?>() {
    val recommendationChart = SingleLiveEvent<List<SongRecommendation>>()
    val favoriteTrack = SingleLiveEvent<List<Track>>()
    val firstTrack = SingleLiveEvent<List<Track>>()
    val secondTrack = SingleLiveEvent<List<Track>>()
    val thirdTrack = SingleLiveEvent<List<Track>>()
    val fourthTrack = SingleLiveEvent<List<Track>>()
    val listPlaylist = SingleLiveEvent<List<PlaylistModel>>()

    internal fun getRecommendation(context: Context) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                flowOnValue(api.getRecommendation()).collect { response ->
                    isLoadingEvent.postValue(false)
                    recommendationChart.postValue(response.data)
                }
            }
        }
    }

    internal fun getSongRecommendation(context: Context) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                flowOnValue(api.searchArtist()).collectLatest { response ->
                    isLoadingEvent.postValue(false)
                    firstTrack.postValue(response.data?.shuffled()?.take(5))
                    secondTrack.postValue(response.data?.shuffled()?.take(5))
                    thirdTrack.postValue(response.data?.shuffled()?.take(5))
                    fourthTrack.postValue(response.data?.shuffled()?.take(5))
                }
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

    internal fun addTrackToPlaylist(context: Context, track: Track, playlist: PlaylistModel?) {
        if (playlist == null) return
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                val playlistFound = db.playlistDao().getPlaylistTrackById(playlist.idPk)

                val listAllTrackInPlaylist = arrayListOf<Track>()
                listAllTrackInPlaylist.addAll(playlistFound?.playlistTrack ?: arrayListOf())
                listAllTrackInPlaylist.add(track)

                val newSetPlaylist = PlaylistModel(
                    playlistName = playlistFound?.playlistName.toString(),
                    playlistIcon = playlistFound?.playlistIcon.toString(),
                    playlistCreated = playlistFound?.playlistCreated ?: 0,
                    playlistTrack = listAllTrackInPlaylist
                )

                db.playlistDao().delete(playlist)
                db.playlistDao().insert(newSetPlaylist)

                getAllPlaylist(context)
            }
        }
    }
}