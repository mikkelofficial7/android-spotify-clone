package com.view.musicplayer.spotifyclone.network.interceptor

import com.view.musicplayer.spotifyclone.BuildConfig
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

class HttpLoggingInterceptor {
    companion object {
        fun get(): List<Interceptor> {
            val debugInterceptor = HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            }
            return listOf(debugInterceptor)
        }
    }
}