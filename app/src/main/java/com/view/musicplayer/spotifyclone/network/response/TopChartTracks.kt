package com.view.musicplayer.spotifyclone.network.response

import com.google.gson.annotations.SerializedName
import com.view.musicplayer.spotifyclone.network.response.general.Attr
import com.view.musicplayer.spotifyclone.network.response.general.Image
import com.view.musicplayer.spotifyclone.network.response.general.Streamable

data class TopChartTracks(
    val tracks: ContentTrack,
)

data class ContentTrack(
    @SerializedName("track") val track: List<Track> = listOf(),
    @SerializedName("@attr") val attr: Attr
)

data class Track(
    val name: String,
    val duration: String,
    val playcount: String,
    val listeners: String,
    val mbid: String,
    val url: String,
    val streamable: Streamable,
    val artist: ArtistTrack,
    val image: List<Image>
)

data class ArtistTrack (
    val name: String,
    val mbid: String,
    val url: String
)