package com.view.musicplayer.spotifyclone.network

import com.view.musicplayer.spotifyclone.network.response.Genre
import com.view.musicplayer.spotifyclone.network.response.SongRecommendation
import com.view.musicplayer.spotifyclone.network.response.Track
import com.view.musicplayer.spotifyclone.network.response.general.GeneralResponse
import retrofit2.http.GET

interface Api {
    @GET("api/songlistrecommend.json")
    suspend fun getRecommendation(): GeneralResponse<List<SongRecommendation>>

    @GET("api/songlist.json")
    suspend fun searchArtist(): GeneralResponse<List<Track>>

    @GET("api/genre.json")
    suspend fun getAllGenre(): GeneralResponse<List<Genre>>
}