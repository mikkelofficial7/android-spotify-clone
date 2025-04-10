package com.view.musicplayer.spotifyclone.network.response.general

import com.google.gson.annotations.SerializedName

data class GeneralResponse<T>(
    var code: Int = 0,
    var message: String = "",
    var data: T?
)

data class GeneralError(
    var message: String = "",
    var error: Int
)

data class Attr (
    val page: String?,
    val perPage: String?,
    val totalPages: String?,
    val total: String?
)

data class Image (
    @SerializedName("#text") val text: String,
    @SerializedName("size") val size: String
)

data class Streamable (
    @SerializedName("#text") val text: String,
    @SerializedName("fulltrack") val fulltrack: String
)

