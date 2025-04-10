package com.view.musicplayer.spotifyclone.viewmodel

import android.content.Context
import com.view.musicplayer.spotifyclone.constants.Constants
import com.view.musicplayer.spotifyclone.ext.SingleLiveEvent
import com.view.musicplayer.spotifyclone.ext.flowOnValue
import com.view.musicplayer.spotifyclone.network.Api
import com.view.musicplayer.spotifyclone.network.response.AllGenre
import com.view.musicplayer.spotifyclone.network.response.Artist
import com.view.musicplayer.spotifyclone.network.response.TopChartTracks
import com.view.musicplayer.spotifyclone.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchViewModel(private val api: Api): BaseViewModel<Any?>() {
    val allGenre = SingleLiveEvent<AllGenre>()
    val topTrack = SingleLiveEvent<TopChartTracks>()
    val listSearchArtist = SingleLiveEvent<ArrayList<Artist>>()

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

    internal fun getTopChartTrack(context: Context) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                flowOnValue(api.getTopChartTracks(
                    apiKey = Constants.CLIENT_KEY,
                    maxPage = 10,
                    page = 2
                )).collectLatest { response ->
                    isLoadingEvent.postValue(false)
                    topTrack.postValue(response)
                }
            }
        }
    }

    internal fun searchArtist(context: Context, artistName: String, page: Int = 1) {
        if (artistName.isBlank()) return
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                flowOnValue(api.searchArtist(
                    apiKey = Constants.CLIENT_KEY,
                    artist = artistName,
                    maxPage = 10,
                    page = page
                )).collectLatest { response ->
                    isLoadingEvent.postValue(false)

                    listSearchArtist.postValue(arrayListOf())
                    listSearchArtist.postValue(response.results.artistmatches.artist as ArrayList<Artist>)
                }
            }
        }
    }
}