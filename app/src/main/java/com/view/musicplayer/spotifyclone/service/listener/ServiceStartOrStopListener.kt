package com.view.musicplayer.spotifyclone.service.listener

import android.content.Context

interface ServiceStartOrStopListener {
    fun onPlay(context: Context, musicId: String)
    fun onRestart(context: Context)
    fun onNext(context: Context)
    fun onPrevious(context: Context)
    fun onPause(context: Context)
    fun onStop(context: Context)
    fun onShuffle(context: Context, isEnable: Boolean)
    fun onRepeat(context: Context, repeatMode: Int)
}