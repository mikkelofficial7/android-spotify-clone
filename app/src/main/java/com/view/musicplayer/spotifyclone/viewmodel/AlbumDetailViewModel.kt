package com.view.musicplayer.spotifyclone.viewmodel

import android.content.Context
import com.view.musicplayer.spotifyclone.ext.SingleLiveEvent
import com.view.musicplayer.spotifyclone.ext.flowOnValue
import com.view.musicplayer.spotifyclone.network.Api
import com.view.musicplayer.spotifyclone.network.response.Genre
import com.view.musicplayer.spotifyclone.network.response.Track
import com.view.musicplayer.spotifyclone.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AlbumDetailViewModel(private val api: Api): BaseViewModel<Any?>() {
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
}