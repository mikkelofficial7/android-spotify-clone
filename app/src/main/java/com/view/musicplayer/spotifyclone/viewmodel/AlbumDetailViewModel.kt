package com.view.musicplayer.spotifyclone.viewmodel

import android.content.Context
import com.view.musicplayer.spotifyclone.ext.SingleLiveEvent
import com.view.musicplayer.spotifyclone.ext.flowOnValue
import com.view.musicplayer.spotifyclone.network.Api
import com.view.musicplayer.spotifyclone.network.response.Genre
import com.view.musicplayer.spotifyclone.network.response.Track
import com.view.musicplayer.spotifyclone.room.AppDb
import com.view.musicplayer.spotifyclone.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AlbumDetailViewModel(private val api: Api, private val db: AppDb): BaseViewModel<Any?>() {
    val favoriteTrack = SingleLiveEvent<List<Track>>()
    val genreData = SingleLiveEvent<Genre>()
    val listArtistByGenre = SingleLiveEvent<ArrayList<Track>>()

    internal fun getGenreByName(context: Context, genre: String) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                flowOnValue(api.getAllGenre()).collectLatest { response ->
                    isLoadingEvent.postValue(false)
                    genreData.postValue(response.data?.find { it.name.equals(genre, true) })
                }
            }
        }
    }

    internal fun getAllArtistByGenre(context: Context, genre: String) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                executeJob(context) {
                    safeScopeFun(context).launch(Dispatchers.IO) {
                        flowOnValue(api.searchArtist()).collectLatest { response ->
                            isLoadingEvent.postValue(false)

                            val matchingTracks = response.data?.filter {
                                it.genre.equals(genre, ignoreCase = true)
                            }

                            listArtistByGenre.postValue(matchingTracks as ArrayList<Track>)
                        }
                    }
                }
            }
        }
    }

    internal fun getAllFavoriteTrack(context: Context) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                val dataExist = db.trackDao().getAllFavoriteTrack()
                favoriteTrack.postValue(dataExist.map { it.toTrack })
            }
        }
    }

    internal fun addOrRemoveFavorite(context: Context, track: Track) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                val dataExist = db.trackDao().getFavoriteTrackById(track.id)

                dataExist?.let {
                    db.trackDao().delete(it)
                } ?: kotlin.run {
                    db.trackDao().insert(track.toFavoriteTrack)
                }

                getAllFavoriteTrack(context)
            }
        }
    }

    internal fun addAllTrackToFavorite(context: Context, trackList: ArrayList<Track>) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                trackList.map {
                    val dataExist = db.trackDao().getFavoriteTrackById(it.id)
                    dataExist?.let { favTrack ->
                        db.trackDao().delete(favTrack)
                    }

                    db.trackDao().insert(it.toFavoriteTrack)
                }

                getAllFavoriteTrack(context)
            }
        }
    }

    internal fun removeAllTrackFromFavorite(context: Context, trackList: ArrayList<Track>) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                trackList.map {
                    val dataExist = db.trackDao().getFavoriteTrackById(it.id)
                    dataExist?.let { favTrack -> db.trackDao().delete(favTrack) }
                }

                getAllFavoriteTrack(context)
            }
        }
    }
}