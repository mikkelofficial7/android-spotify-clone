package com.view.musicplayer.spotifyclone.viewmodel.base

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.view.musicplayer.spotifyclone.ext.EmptyClass
import com.view.musicplayer.spotifyclone.ext.NetworkHandler
import com.view.musicplayer.spotifyclone.ext.SingleLiveEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus

open class BaseViewModel<T> : ViewModel() {
    internal val isLoadingEvent = SingleLiveEvent<Boolean>()
    internal val errorEvent = SingleLiveEvent<EmptyClass>()
    internal fun handleFailure() {
        isLoadingEvent.postValue( false)
        errorEvent.postValue(EmptyClass())
    }

    fun executeJob(context: Context, needShowLoading: Boolean = true, invoke:() -> Unit = {}) {
        if (needShowLoading) isLoadingEvent.postValue(true)
        when (NetworkHandler(context).isNetworkAvailable()) {
            true -> invoke()
            else -> {
                handleFailure()
            }
        }
    }

    fun safeScopeFun(context: Context) : CoroutineScope {
        return viewModelScope + CoroutineExceptionHandler { _, throwable ->
            handleFailure()
        }
    }
}