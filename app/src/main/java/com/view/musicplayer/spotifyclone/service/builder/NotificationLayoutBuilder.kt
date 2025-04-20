package com.view.musicplayer.spotifyclone.service.builder

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.view.musicplayer.spotifyclone.MainActivity
import com.view.musicplayer.spotifyclone.R
import com.view.musicplayer.spotifyclone.network.response.Track
import com.view.musicplayer.spotifyclone.service.MusicService
import com.view.musicplayer.spotifyclone.service.MusicService.INTENT.PENDING_DESCRIPTION
import com.view.musicplayer.spotifyclone.service.MusicService.INTENT.PENDING_DURATION
import com.view.musicplayer.spotifyclone.service.MusicService.INTENT.PENDING_DURATION_TEXT
import com.view.musicplayer.spotifyclone.service.MusicService.INTENT.PENDING_DURATION_TOTAL_TEXT
import com.view.musicplayer.spotifyclone.service.MusicService.INTENT.PENDING_MUSIC_DATA
import com.view.musicplayer.spotifyclone.service.MusicService.INTENT.PENDING_TITLE
import com.view.musicplayer.spotifyclone.service.MusicService.Notification.NOTIFICATION_ID
import com.view.musicplayer.spotifyclone.service.receiver.NotificationToServiceReceiver


object NotificationLayoutBuilder {
    @SuppressLint("RemoteViewLayout")
    fun showCustomNotification(context: Context,
                               track: Track?,
                               progress: Float,
                               durationText: String,
                               durationTotalText: String,
                               title: String,
                               descriptions: String,
                               image: Bitmap?,
                               isPause: Boolean = false
        ) : Notification {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val defaultPendingIntent = getDefaultPendingIntent(context, track, progress, durationText, durationTotalText, title, descriptions)
        val playPendingIntent = getPlayPendingIntent(context)
        val pausePendingIntent = getPausePendingIntent(context)
        val nextPendingIntent = getNextPendingIntent(context)
        val prevPendingIntent = getPrevPendingIntent(context)

        val notificationLayout = RemoteViews(context.packageName, R.layout.layout_notification_small).apply {
            setTextViewText(R.id.iv_title_notification, title)
            setTextViewText(R.id.iv_subtitle_notification, descriptions)
            setImageViewResource(R.id.btn_play_pause, if (isPause) R.drawable.ic_play else R.drawable.ic_pause)
            setImageViewResource(R.id.btn_next, R.drawable.ic_next)
            setImageViewResource(R.id.btn_previous, R.drawable.ic_previous)
            setOnClickPendingIntent(R.id.btn_play_pause, if (isPause) playPendingIntent else pausePendingIntent)
            setOnClickPendingIntent(R.id.btn_next, nextPendingIntent)
            setOnClickPendingIntent(R.id.btn_previous, prevPendingIntent)
            image?.let {
                setImageViewBitmap(R.id.iv_notification, image)
                setImageViewBitmap(R.id.iv_notification_bg, image)
            }
        }

        val notificationLayoutExpanded = RemoteViews(context.packageName, R.layout.layout_notification).apply {
            setProgressBar(R.id.notification_seekbar, 100, progress.toInt(), false)
            setTextViewText(R.id.iv_title_notification, title)
            setTextViewText(R.id.iv_subtitle_notification, descriptions)
            setTextViewText(R.id.tv_timer_notification, durationText)
            setTextViewText(R.id.tv_timer_notification_total, durationTotalText)
            setImageViewResource(R.id.btn_next, R.drawable.ic_next)
            setImageViewResource(R.id.btn_previous, R.drawable.ic_previous)
            setImageViewResource(R.id.btn_play_pause, if (isPause) R.drawable.ic_play else R.drawable.ic_pause)
            setOnClickPendingIntent(R.id.btn_play_pause, if (isPause) playPendingIntent else pausePendingIntent)
            setOnClickPendingIntent(R.id.btn_next, nextPendingIntent)
            setOnClickPendingIntent(R.id.btn_previous, prevPendingIntent)
            image?.let {
                setImageViewBitmap(R.id.iv_notification, image)
                setImageViewBitmap(R.id.iv_notification_bg, image)
            }
        }

        val importance = NotificationManager.IMPORTANCE_LOW
        val notificationChannel = NotificationChannel(channelId, channelName, importance).apply {
            description = channelDescription
            enableLights(false)
            enableVibration(false)
            setSound(null, null)
        }

        notificationManager.createNotificationChannel(notificationChannel)

        val customNotification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.is_spotify_green)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setContentIntent(defaultPendingIntent)
            .setCustomContentView(notificationLayout)
            .setCustomBigContentView(notificationLayoutExpanded)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, customNotification)

        return customNotification
    }

    fun cancel(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(NOTIFICATION_ID)
    }

    private fun getDefaultPendingIntent(
        context: Context,
        track: Track?,
        progress: Float,
        durationText: String,
        durationTotalText: String,
        title: String,
        descriptions: String
    ): PendingIntent? {
        val pendingIntent = Intent(context, MainActivity::class.java)
        pendingIntent.putExtra(PENDING_MUSIC_DATA, track)
        pendingIntent.putExtra(PENDING_DURATION, progress)
        pendingIntent.putExtra(PENDING_DURATION_TEXT, durationText)
        pendingIntent.putExtra(PENDING_DURATION_TOTAL_TEXT, durationTotalText)
        pendingIntent.putExtra(PENDING_TITLE, title)
        pendingIntent.putExtra(PENDING_DESCRIPTION, descriptions)
        pendingIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        return PendingIntent.getActivity(context, NOTIFICATION_ID, pendingIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }

    private fun getPlayPendingIntent(
        context: Context
    ): PendingIntent? {
        val playIntent = Intent(context, NotificationToServiceReceiver::class.java).apply {
            action = MusicService.ActionNotification.ACTION_PLAY
        }

        return PendingIntent.getBroadcast(
            context,
            123,
            playIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun getPausePendingIntent(
        context: Context
    ): PendingIntent? {
        val playIntent = Intent(context, NotificationToServiceReceiver::class.java).apply {
            action = MusicService.ActionNotification.ACTION_PAUSE
        }

        return PendingIntent.getBroadcast(
            context,
            123,
            playIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun getNextPendingIntent(
        context: Context
    ): PendingIntent? {
        val playIntent = Intent(context, NotificationToServiceReceiver::class.java).apply {
            action = MusicService.ActionNotification.ACTION_NEXT
        }

        return PendingIntent.getBroadcast(
            context,
            123,
            playIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun getPrevPendingIntent(
        context: Context
    ): PendingIntent? {
        val playIntent = Intent(context, NotificationToServiceReceiver::class.java).apply {
            action = MusicService.ActionNotification.ACTION_PREV
        }

        return PendingIntent.getBroadcast(
            context,
            123,
            playIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    const val channelId = "Channel ID"
    const val channelName = "Music Playback"
    const val channelDescription = "Music Playback Description"
}