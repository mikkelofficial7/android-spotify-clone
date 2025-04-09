package com.view.musicplayer.spotifyclone.network

import com.view.musicplayer.spotifyclone.network.response.HomePageResponse
import com.view.musicplayer.spotifyclone.network.response.SearchRecommendResponse
import com.view.musicplayer.spotifyclone.network.response.general.GeneralResponse
import retrofit2.http.GET

interface Api {
    @GET("assets/homepage")
    suspend fun getHomePageList(): GeneralResponse<List<HomePageResponse>>

    @GET("assets/searchrecommend")
    suspend fun getSearchRecommend(): GeneralResponse<SearchRecommendResponse>

}