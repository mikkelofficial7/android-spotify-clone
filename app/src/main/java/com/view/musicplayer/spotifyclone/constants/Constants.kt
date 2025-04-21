package com.view.musicplayer.spotifyclone.constants

import com.view.musicplayer.spotifyclone.BuildConfig

object Constants {
    const val BASE_URL = BuildConfig.BASE_URL
    const val BASE_URL_AI = BuildConfig.BASE_URL_AI

    const val AI_MODEL = "google/gemini-2.0-flash-thinking-exp-1219:free"
    const val AI_ROLE = "user"
    const val DELAY_SEARCH = 2500L
}