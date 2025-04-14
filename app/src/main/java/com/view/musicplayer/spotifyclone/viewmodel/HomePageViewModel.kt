package com.view.musicplayer.spotifyclone.viewmodel

import android.content.Context
import com.view.musicplayer.spotifyclone.ext.SingleLiveEvent
import com.view.musicplayer.spotifyclone.ext.flowOnValue
import com.view.musicplayer.spotifyclone.network.Api
import com.view.musicplayer.spotifyclone.network.response.SongRecommendation
import com.view.musicplayer.spotifyclone.network.response.Track
import com.view.musicplayer.spotifyclone.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomePageViewModel(private val api: Api): BaseViewModel<Any?>() {
    val recommendationChart = SingleLiveEvent<List<SongRecommendation>>()
    val firstTrack = SingleLiveEvent<List<Track>>()
    val secondTrack = SingleLiveEvent<List<Track>>()
    val thirdTrack = SingleLiveEvent<List<Track>>()
    val fourthTrack = SingleLiveEvent<List<Track>>()

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
}