package com.view.musicplayer.spotifyclone.ext

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

suspend fun <T : Any> flowOnValue(value : T) : Flow<T> {
    return  flow { emit(value) }.flowOn(Dispatchers.IO)
}

suspend fun <T : Any> flowOnValue(
    onRunning: suspend () -> T,
    onError: suspend () -> Unit = {}
): Flow<T> = flow {
    emit(onRunning())
}.catch { e ->
    if (e is HttpException) {
        onError()
    } else {
        throw e
    }
}.flowOn(Dispatchers.IO)

fun <T : Any, L : SingleLiveEvent<T>> LifecycleOwner.observer(liveData: L, body: (T?) -> Unit) =
    liveData.observe(this, Observer(body))

class EmptyClass