package com.view.musicplayer.spotifyclone.network.interceptor

import com.view.musicplayer.spotifyclone.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class OpenRouterInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url

        if (url.host != OPEN_ROUTER_BASE) {
            return chain.proceed(request)
        }

        val requestBuilder = request.newBuilder()
            .addHeader("Authorization", "Bearer ${BuildConfig.API_TOKEN}")
            .addHeader("Content-Type", "application/json")
            .build()

        return chain.proceed(requestBuilder)
    }

    companion object {
        private const val OPEN_ROUTER_BASE: String = "openrouter.ai"
    }
}