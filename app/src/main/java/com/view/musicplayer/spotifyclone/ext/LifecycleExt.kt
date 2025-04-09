package com.view.musicplayer.spotifyclone.ext

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

suspend fun <T : Any> flowOnValue(value : T) : Flow<T> {
    return  flow { emit(value) }.flowOn(Dispatchers.IO)
}

fun <T : Any, L : SingleLiveEvent<T>> LifecycleOwner.observer(liveData: L, body: (T?) -> Unit) =
    liveData.observe(this, Observer(body))

class EmptyClass