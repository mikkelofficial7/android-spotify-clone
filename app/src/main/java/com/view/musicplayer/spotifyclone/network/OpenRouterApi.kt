package com.view.musicplayer.spotifyclone.network

import com.view.musicplayer.spotifyclone.network.request.OpenRouterRequest
import com.view.musicplayer.spotifyclone.network.response.OpenRouterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface OpenRouterApi {
    @POST("chat/completions")
    suspend fun getResponse(
        @Body request: OpenRouterRequest
    ): OpenRouterResponse
}