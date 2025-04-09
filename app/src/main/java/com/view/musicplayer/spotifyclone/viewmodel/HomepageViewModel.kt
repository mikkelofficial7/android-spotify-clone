package com.view.musicplayer.spotifyclone.viewmodel

import android.content.Context
import com.view.musicplayer.spotifyclone.ext.SingleLiveEvent
import com.view.musicplayer.spotifyclone.ext.flowOnValue
import com.view.musicplayer.spotifyclone.network.Api
import com.view.musicplayer.spotifyclone.network.response.HomePageResponse
import com.view.musicplayer.spotifyclone.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomepageViewModel(private val api: Api): BaseViewModel<Any?>() {
    val homepageData = SingleLiveEvent<List<HomePageResponse>>()
    internal fun getHomePageData(context: Context) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                flowOnValue(api.getHomePageList()).collectLatest { response ->
                    isLoadingEvent.postValue(false)
                    homepageData.postValue(response.data)
                }
            }
        }
    }
}