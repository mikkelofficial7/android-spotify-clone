package com.view.musicplayer.spotifyclone.network.interceptor

import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

object HttpLoggingInterceptor {
    fun get(): List<Interceptor> {
        val debugInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.NONE
        }
        return listOf(debugInterceptor)
    }
}