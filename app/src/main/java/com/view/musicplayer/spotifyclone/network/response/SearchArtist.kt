package com.view.musicplayer.spotifyclone.network.response

import com.google.gson.annotations.SerializedName

data class SearchArtist (
    val results: ContentSearchArtist,
)
data class ContentSearchArtist (
    val artistmatches: Artistmatches,
)

data class Artistmatches (
    val artist: List<Artist>
)

data class Attr (
    @SerializedName("for")
    val attrFor: String
)

data class OpensearchQuery (
    @SerializedName("#text")
    val text: String,
    val role: String,
    val searchTerms: String,
    val startPage: String
)
