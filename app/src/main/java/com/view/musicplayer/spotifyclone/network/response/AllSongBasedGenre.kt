package com.view.musicplayer.spotifyclone.network.response

import com.google.gson.annotations.SerializedName
import com.view.musicplayer.spotifyclone.network.response.general.Attr
import com.view.musicplayer.spotifyclone.network.response.general.Image
import com.view.musicplayer.spotifyclone.network.response.general.Streamable

data class AllSongBasedGenre(
    val tracks: ContentTrackGenre,
)

data class ContentTrackGenre (
    @SerializedName("@attr") val track: List<TrackGenre>,
    @SerializedName("@attr") val attr: Attr
)
data class TrackGenre (
    val name: String,
    val duration: String,
    val mbid: String,
    val url: String,
    val streamable: Streamable,
    val artist: ArtistTrackGenre,
    val image: List<Image>,
    @SerializedName("@attr") val attr: TrackAttr
)

data class ArtistTrackGenre (
    val name: String,
    val mbid: String,
    val url: String
)

data class TrackAttr (
    val rank: String
)
