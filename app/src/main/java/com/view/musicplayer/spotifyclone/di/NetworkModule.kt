package com.view.musicplayer.spotifyclone.di

import android.content.Context
import com.view.musicplayer.spotifyclone.ext.NetworkHandler
import com.view.musicplayer.spotifyclone.network.RetrofitBuilder
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

class NetworkModule {
    companion object {
        val NetworkModule = module {
            single { provideOkHttpClient(androidContext()) }
            single { provideNetworkHandler(androidContext()) }
        }

        private fun provideNetworkHandler(context: Context) = NetworkHandler(context)

        private fun provideOkHttpClient(context: Context) : OkHttpClient {
            return RetrofitBuilder.initInterceptor(context)
        }
    }
}