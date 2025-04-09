package com.view.musicplayer.spotifyclone.network

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.view.musicplayer.spotifyclone.network.interceptor.AssetFileInterceptor
import com.view.musicplayer.spotifyclone.network.interceptor.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitBuilder {
    fun initRetrofit(url: String, okHttpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    fun initInterceptor(context: Context): OkHttpClient {
        return OkHttpClient()
            .newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(50, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS)
            .addInterceptor(AssetFileInterceptor(context.assets))
            .apply {
                HttpLoggingInterceptor.get().forEach { addInterceptor(it) }
            }
            .build()
    }
}