package com.view.musicplayer.spotifyclone.viewmodel

import android.content.Context
import com.view.musicplayer.spotifyclone.ext.SingleLiveEvent
import com.view.musicplayer.spotifyclone.ext.flowOnValue
import com.view.musicplayer.spotifyclone.network.Api
import com.view.musicplayer.spotifyclone.network.response.SearchRecommendResponse
import com.view.musicplayer.spotifyclone.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchViewModel(private val api: Api): BaseViewModel<Any?>() {
    val recommendData = SingleLiveEvent<SearchRecommendResponse>()
    internal fun getSearchRecommendation(context: Context) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                flowOnValue(api.getSearchRecommend()).collectLatest { response ->
                    isLoadingEvent.postValue(false)
                    recommendData.postValue(response.data)
                }
            }
        }
    }
}