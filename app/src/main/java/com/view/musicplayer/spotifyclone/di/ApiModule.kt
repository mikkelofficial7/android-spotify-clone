package com.view.musicplayer.spotifyclone.di

import com.view.musicplayer.spotifyclone.constants.Constants
import com.view.musicplayer.spotifyclone.network.Api
import com.view.musicplayer.spotifyclone.network.RetrofitBuilder
import okhttp3.OkHttpClient
import org.koin.dsl.module

class ApiModule {
    companion object {
        val apiModule = module {
            single { provideUserApi(get()) }
        }

        private fun provideUserApi(okHttpClient: OkHttpClient): Api {
            return RetrofitBuilder.initRetrofit(Constants.BASE_URL, okHttpClient).create(Api::class.java)
        }
    }
}