package com.view.musicplayer.spotifyclone.network

import com.view.musicplayer.spotifyclone.constants.Constants
import com.view.musicplayer.spotifyclone.network.response.AllGenre
import com.view.musicplayer.spotifyclone.network.response.AllSongBasedGenre
import com.view.musicplayer.spotifyclone.network.response.SearchArtist
import com.view.musicplayer.spotifyclone.network.response.TopChartArtist
import com.view.musicplayer.spotifyclone.network.response.TopChartTracks
import com.view.musicplayer.spotifyclone.network.response.general.GeneralResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("2.0")
    suspend fun getTopChartArtist(
        @Query("method") method: String = Constants.METHOD_GET_ALL_TOP_ARTIST,
        @Query("api_key") apiKey: String,
        @Query("format") format: String = Constants.DEFAULT_FORMAT,
        @Query("limit") maxPage: Int,
        @Query("page") page: Int = 1,
    ): TopChartArtist

    @GET("2.0")
    suspend fun getTopChartTracks(
        @Query("method") method: String = Constants.METHOD_GET_ALL_TOP_TRACK,
        @Query("api_key") apiKey: String,
        @Query("format") format: String = Constants.DEFAULT_FORMAT,
        @Query("limit") maxPage: Int,
        @Query("page") page: Int = 1,
    ): TopChartTracks

    @GET("2.0")
    suspend fun searchArtist(
        @Query("method") method: String = Constants.METHOD_SEARCH_ARTIST,
        @Query("api_key") apiKey: String,
        @Query("format") format: String = Constants.DEFAULT_FORMAT,
        @Query("artist") artist: String,
        @Query("limit") maxPage: Int,
        @Query("page") page: Int = 1,
    ): SearchArtist

    @GET("2.0")
    suspend fun getTopChartTracksBasedGenre(
        @Query("method") method: String = Constants.METHOD_GET_ALL_TOP_TRACK_BASED_GENRE,
        @Query("api_key") apiKey: String,
        @Query("format") format: String = Constants.DEFAULT_FORMAT,
        @Query("tag") genre: String,
        @Query("limit") maxPage: Int,
    ): AllSongBasedGenre

    @GET("assets/searchrecommend")
    suspend fun getAllGenre(): GeneralResponse<AllGenre>

//    @GET("?method=album.search&album={query}&api_key={api_key}&format=json&limit={limit}")
//    suspend fun searchAlbum(): GeneralResponse<SearchRecommendResponse>
//
//    @GET("?method=artist.search&artist={query}&api_key={api_key}&format=json&limit={limit}")
//    suspend fun searchArtist(): GeneralResponse<SearchRecommendResponse>
}