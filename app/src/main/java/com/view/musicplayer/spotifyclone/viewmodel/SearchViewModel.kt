package com.view.musicplayer.spotifyclone.viewmodel

import android.content.Context
import com.view.musicplayer.spotifyclone.ext.SingleLiveEvent
import com.view.musicplayer.spotifyclone.ext.flowOnValue
import com.view.musicplayer.spotifyclone.network.Api
import com.view.musicplayer.spotifyclone.network.response.Genre
import com.view.musicplayer.spotifyclone.network.response.SongRecommendation
import com.view.musicplayer.spotifyclone.network.response.Track
import com.view.musicplayer.spotifyclone.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchViewModel(private val api: Api): BaseViewModel<Any?>() {
    val allGenre = SingleLiveEvent<List<Genre>>()
    val topTrack = SingleLiveEvent<SongRecommendation>()
    val listSearchArtist = SingleLiveEvent<ArrayList<Track>>()

    internal fun getAllGenre(context: Context) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                flowOnValue(api.getAllGenre()).collectLatest { response ->
                    isLoadingEvent.postValue(false)
                    allGenre.postValue(response.data)
                }
            }
        }
    }

    internal fun getSongRecommendation(context: Context) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                flowOnValue(api.getRecommendation()).collectLatest { response ->
                    isLoadingEvent.postValue(false)
                    topTrack.postValue(response.data?.lastOrNull())
                }
            }
        }
    }

    internal fun searchArtistOrSong(context: Context, artistName: String) {
        if (artistName.isBlank()) return
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                flowOnValue(api.searchArtist()).collectLatest { response ->
                    isLoadingEvent.postValue(false)

                    val matchingTracks = response.data?.filter {
                            it.artist.contains(artistName, ignoreCase = true) ||
                                    it.title.contains(artistName, ignoreCase = true)
                        } ?: listOf()

                    listSearchArtist.postValue(arrayListOf())
                    listSearchArtist.postValue(matchingTracks as ArrayList<Track>)
                }
            }
        }
    }
}