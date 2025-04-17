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
import com.view.musicplayer.spotifyclone.service.MusicService.INTENT.PENDING_DESCRIPTION
import com.view.musicplayer.spotifyclone.service.MusicService.INTENT.PENDING_DURATION
import com.view.musicplayer.spotifyclone.service.MusicService.INTENT.PENDING_DURATION_TEXT
import com.view.musicplayer.spotifyclone.service.MusicService.INTENT.PENDING_DURATION_TOTAL_TEXT
import com.view.musicplayer.spotifyclone.service.MusicService.INTENT.PENDING_MUSIC_ID
import com.view.musicplayer.spotifyclone.service.MusicService.INTENT.PENDING_TITLE
import com.view.musicplayer.spotifyclone.service.MusicService.Notification.NOTIFICATION_ID


object NotificationLayoutBuilder {
    @SuppressLint("RemoteViewLayout")
    fun showCustomNotification(context: Context,
                               id: String,
                               progress: Float,
                               durationText: String,
                               durationTotalText: String,
                               title: String,
                               descriptions: String,
                               image: Bitmap?) : Notification {
        val channelId = "CHANNEL_ID"
        val channelName = "Channel name"
        val channelDescription = "Channel description"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val pendingIntent = Intent(context, MainActivity::class.java)
        pendingIntent.putExtra(PENDING_MUSIC_ID, id)
        pendingIntent.putExtra(PENDING_DURATION, progress)
        pendingIntent.putExtra(PENDING_DURATION_TEXT, durationText)
        pendingIntent.putExtra(PENDING_DURATION_TOTAL_TEXT, durationTotalText)
        pendingIntent.putExtra(PENDING_TITLE, title)
        pendingIntent.putExtra(PENDING_DESCRIPTION, descriptions)
        pendingIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        val contentIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, pendingIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationChannel = NotificationChannel(channelId, channelName, importance).apply {
            description = channelDescription
            enableLights(false)
            enableVibration(false)
            setSound(null, null)
        }
        notificationManager.createNotificationChannel(notificationChannel)

        val notificationLayout = RemoteViews(context.packageName, R.layout.layout_notification).apply {
            setProgressBar(R.id.notification_seekbar, 100, progress.toInt(), false)
            setTextViewText(R.id.iv_title_notification, title)
            setTextViewText(R.id.iv_subtitle_notification, descriptions)
            setTextViewText(R.id.tv_timer_notification, durationText)
            setTextViewText(R.id.tv_timer_notification_total, durationTotalText)
            image?.let {
                setImageViewBitmap(R.id.iv_notification, image)
                setImageViewBitmap(R.id.iv_notification_bg, image)
            }
        }
        val notificationLayoutExpanded = RemoteViews(context.packageName,
            R.layout.layout_notification
        ).apply {
            setProgressBar(R.id.notification_seekbar, 100, progress.toInt(), false)
            setTextViewText(R.id.iv_title_notification, title)
            setTextViewText(R.id.iv_subtitle_notification, descriptions)
            setTextViewText(R.id.tv_timer_notification, durationText)
            setTextViewText(R.id.tv_timer_notification_total, durationTotalText)
            image?.let {
                setImageViewBitmap(R.id.iv_notification, image)
                setImageViewBitmap(R.id.iv_notification_bg, image)
            }
        }

        val customNotification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setContentIntent(contentIntent)
            .setCustomContentView(notificationLayout)
            .setCustomBigContentView(notificationLayoutExpanded)
            .setPriority(NotificationCompat.PRIORITY_MAX)
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
}