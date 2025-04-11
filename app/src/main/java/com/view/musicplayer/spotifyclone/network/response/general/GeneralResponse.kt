package com.view.musicplayer.spotifyclone.network.response.general

data class GeneralResponse<T>(
    var code: Int = 0,
    var message: String = "",
    var data: T?
)
