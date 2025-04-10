package com.view.musicplayer.spotifyclone.network.response

import com.google.gson.annotations.SerializedName
import com.view.musicplayer.spotifyclone.network.response.general.Attr
import com.view.musicplayer.spotifyclone.network.response.general.Image

data class TopChartArtist(
    val artists: ContentArtist,
)

data class ContentArtist(
    @SerializedName("artist") val artist: List<Artist> = listOf(),
    @SerializedName("@attr") val attr: Attr
)

data class Artist(
    val name: String,
    val playcount: String,
    val listeners: String,
    val mbid: String,
    val url: String,
    val streamable: String,
    val image: List<Image>
)