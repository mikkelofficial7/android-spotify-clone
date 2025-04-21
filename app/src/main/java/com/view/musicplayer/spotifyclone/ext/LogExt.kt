package com.view.musicplayer.spotifyclone.ext

import android.util.Log
import com.view.musicplayer.spotifyclone.BuildConfig

object LogExt {
    fun d(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message)
        }
    }
}