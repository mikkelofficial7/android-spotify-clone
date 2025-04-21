package com.view.musicplayer.spotifyclone.network.request

data class OpenRouterRequest (
    val model: String,
    val messages: List<OpenRouterMessage>
)
data class OpenRouterMessage (
    val role: String,
    val content: String
)