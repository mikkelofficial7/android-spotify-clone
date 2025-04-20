package com.view.musicplayer.spotifyclone.service.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.view.musicplayer.spotifyclone.service.MusicService

class NotificationToServiceReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            MusicService.ActionNotification.ACTION_PLAY -> {
                val serviceIntent = Intent(context, MusicService::class.java)
                serviceIntent.putExtra(MusicService.ActionKey.ACTION, MusicService.ActionDetail.START_MODE)
                serviceIntent.action = MusicService.Notification.START_FOREGROUND_ACTION
                context.startService(serviceIntent)
            }

            MusicService.ActionNotification.ACTION_PAUSE -> {
                val serviceIntent = Intent(context, MusicService::class.java)
                serviceIntent.putExtra(MusicService.ActionKey.ACTION, MusicService.ActionDetail.PAUSE_MODE)
                serviceIntent.action = MusicService.Notification.START_FOREGROUND_ACTION
                context.startService(serviceIntent)
            }

            MusicService.ActionNotification.ACTION_NEXT -> {
                val serviceIntent = Intent(context, MusicService::class.java)
                serviceIntent.putExtra(MusicService.ActionKey.ACTION, MusicService.ActionDetail.NEXT_MODE)
                serviceIntent.action = MusicService.Notification.START_FOREGROUND_ACTION
                context.startService(serviceIntent)
            }

            MusicService.ActionNotification.ACTION_PREV -> {
                val serviceIntent = Intent(context, MusicService::class.java)
                serviceIntent.putExtra(MusicService.ActionKey.ACTION, MusicService.ActionDetail.PREV_MODE)
                serviceIntent.action = MusicService.Notification.START_FOREGROUND_ACTION
                context.startService(serviceIntent)
            }
        }
    }
}