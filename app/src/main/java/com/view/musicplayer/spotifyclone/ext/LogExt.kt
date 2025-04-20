package com.view.musicplayer.spotifyclone.ext

import android.util.Log
import com.google.android.exoplayer2.BuildConfig

object LogExt {
    fun d(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message)
        }
    }
}