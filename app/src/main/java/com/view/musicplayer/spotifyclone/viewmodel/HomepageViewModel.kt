package com.view.musicplayer.spotifyclone.viewmodel

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.view.musicplayer.spotifyclone.constants.Constants
import com.view.musicplayer.spotifyclone.ext.SingleLiveEvent
import com.view.musicplayer.spotifyclone.ext.flowOnValue
import com.view.musicplayer.spotifyclone.network.Api
import com.view.musicplayer.spotifyclone.network.response.TopChartArtist
import com.view.musicplayer.spotifyclone.network.response.TopChartTracks
import com.view.musicplayer.spotifyclone.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onErrorReturn
import kotlinx.coroutines.launch

class HomepageViewModel(private val api: Api): BaseViewModel<Any?>() {
    val topChart = SingleLiveEvent<TopChartArtist>()
    val topTrack = SingleLiveEvent<TopChartTracks>()

    internal fun getTopChartArtist(context: Context) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                flowOnValue(api.getTopChartArtist(
                    apiKey = Constants.CLIENT_KEY,
                    maxPage = 10
                )).catch {
                    Log.d("TAG", Gson().toJson(it))
                }.collect { response ->
                    isLoadingEvent.postValue(false)
                    topChart.postValue(response)
                }
            }
        }
    }

    internal fun getTopChartTrack(context: Context) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                flowOnValue(api.getTopChartTracks(
                    apiKey = Constants.CLIENT_KEY,
                    maxPage = 10
                )).collectLatest { response ->
                    isLoadingEvent.postValue(false)
                    topTrack.postValue(response)
                }
            }
        }
    }
}