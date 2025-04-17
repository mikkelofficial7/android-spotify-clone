package com.view.musicplayer.spotifyclone.service.receiver

import android.content.Context
import android.content.Intent
import com.view.musicplayer.spotifyclone.service.MusicService
import com.view.musicplayer.spotifyclone.service.listener.ServiceStartOrStopListener

class NotificationReceiver: ServiceStartOrStopListener {
    override fun onPlay(
        context: Context,
        musicId: String
    ) {
        val serviceIntent = Intent(context, MusicService::class.java)
        serviceIntent.putExtra(MusicService.TAG.MUSIC_ID, musicId)
        serviceIntent.putExtra(MusicService.ActionKey.ACTION, MusicService.ActionDetail.START_MODE)
        serviceIntent.action = MusicService.Notification.START_FOREGROUND_ACTION
        context.startService(serviceIntent)
    }

    override fun onRestart(context: Context, musicId: String) {
        val serviceIntent = Intent(context, MusicService::class.java)
        serviceIntent.putExtra(MusicService.TAG.MUSIC_ID, musicId)
        serviceIntent.putExtra(MusicService.ActionKey.ACTION, MusicService.ActionDetail.RESTART_MODE)
        serviceIntent.action = MusicService.Notification.START_FOREGROUND_ACTION
        context.startService(serviceIntent)
    }

    override fun onPause(context: Context) {
        val serviceIntent = Intent(context, MusicService::class.java)
        serviceIntent.putExtra(MusicService.ActionKey.ACTION, MusicService.ActionDetail.PAUSE_MODE)
        serviceIntent.action = MusicService.Notification.START_FOREGROUND_ACTION
        context.startService(serviceIntent)
    }

    override fun onStop(context: Context) {
        val serviceIntent = Intent(context, MusicService::class.java)
        serviceIntent.putExtra(MusicService.ActionKey.ACTION, MusicService.ActionDetail.STOP_MODE)
        serviceIntent.action = MusicService.Notification.START_FOREGROUND_ACTION
        context.startService(serviceIntent)
    }

    override fun onShuffle(context: Context, isEnable: Boolean) {
        val serviceIntent = Intent(context, MusicService::class.java)
        serviceIntent.putExtra(MusicService.ActionKey.ACTION, MusicService.ActionDetail.SHUFFLE_MODE)
        serviceIntent.putExtra(MusicService.TAG.IS_SHUFFLE, isEnable)
        serviceIntent.action = MusicService.Notification.START_FOREGROUND_ACTION
        context.startService(serviceIntent)
    }

    override fun onRepeat(context: Context, repeatMode: Int) {
        val serviceIntent = Intent(context, MusicService::class.java)
        serviceIntent.putExtra(MusicService.ActionKey.ACTION, MusicService.ActionDetail.REPEAT_MODE)
        serviceIntent.putExtra(MusicService.TAG.REPEAT_MODE, repeatMode)
        serviceIntent.action = MusicService.Notification.START_FOREGROUND_ACTION
        context.startService(serviceIntent)
    }
}