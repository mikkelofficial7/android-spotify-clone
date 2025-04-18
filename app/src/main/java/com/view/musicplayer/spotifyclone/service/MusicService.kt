package com.view.musicplayer.spotifyclone.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.CountDownTimer
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.view.musicplayer.spotifyclone.di.RoomModule
import com.view.musicplayer.spotifyclone.ext.loadImageNotification
import com.view.musicplayer.spotifyclone.network.response.Track
import com.view.musicplayer.spotifyclone.service.builder.NotificationLayoutBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.abs

class MusicService : Service() {
    private val exoplayer: ExoPlayer by lazy {
        ExoPlayer.Builder(this).build()
    }

    private var countdownTimer: CountDownTimer? = null
    private var notification: android.app.Notification? = null

    private var isShuffleEnable: Boolean = false
    private var currentPlayingTrack: Track? = null
    private var totalDuration: Long = 0
    private var lastDuration: Long = 0
    private var playerStatus: PlayerStatus? = null

    var durationRunning: Long = 0L
    var durationTotal: Long = 0L
    var durationRunningText: String = ""
    var durationTotalText: String = ""

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val musicId = intent.getStringExtra(TAG.MUSIC_ID) ?: ""
        val isShuffle = intent.getBooleanExtra(TAG.IS_SHUFFLE, false)
        val repeatMode = intent.getIntExtra(TAG.REPEAT_MODE, -1)

        serviceScope.launch {
            currentPlayingTrack = RoomModule.provideDB(applicationContext).trackDao().getTrackById(musicId)

            withContext(Dispatchers.Main) {
                if (intent.action == Notification.START_FOREGROUND_ACTION) {
                    val currentPlayerState = intent.getStringExtra(ActionKey.ACTION) ?: ""

                    when (currentPlayerState) {
                        ActionDetail.START_MODE -> {
                            playPlayback()
                        }
                        ActionDetail.PAUSE_MODE -> {
                            pausePlayback()
                        }
                        ActionDetail.STOP_MODE -> {
                            stopPlayback()
                        }
                        ActionDetail.NEXT_MODE -> {
                            playPlayback()
                        }
                        ActionDetail.PREV_MODE -> {
                            playPlayback()
                        }
                        ActionDetail.RESTART_MODE -> {
                            playerStatus = PlayerStatus.PLAY
                            stopMusic(currentPlayingTrack?.idPk ?: 0, 0)
                        }
                        ActionDetail.SHUFFLE_MODE -> {
                            playerStatus = PlayerStatus.PLAY
                            isShuffleEnable = isShuffle
                            exoplayer.shuffleModeEnabled = isShuffle
                        }
                        ActionDetail.REPEAT_MODE -> {
                            playerStatus = PlayerStatus.PLAY
                            exoplayer.repeatMode = repeatMode
                        }
                    }

                }
            }
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        currentPlayingTrack = null
        exoplayer.release()

        killService()
        super.onDestroy()
    }

    private fun removeNotification() {
        NotificationLayoutBuilder.cancel(this)
    }

    private fun killService() {
        stopSelf()
        stopForeground(true)
    }

    private fun stopMusic(position: Int, duration: Long) {
        exoplayer.seekTo(position, duration)
    }

    private fun playMusic(position: Int, lastPlaybackPosition: Long) {
        exoplayer.seekTo(position, lastPlaybackPosition)
        exoplayer.play()
    }

    private fun runCountDown() {
        countdownTimer?.cancel()
        startCountDown()
    }

    private fun showNotification(context: Context,
                                 musicID: String = "",
                                 progress: Float = 0f,
                                 durationText: String = "00:00",
                                 durationTotalText: String = "00:00",
                                 title: String = "",
                                 descriptions: String = "",
                                 image: Bitmap? = null
    ): android.app.Notification {
        return NotificationLayoutBuilder.showCustomNotification(
            context,
            musicID,
            progress,
            durationText,
            durationTotalText,
            title,
            descriptions,
            image
        )
    }

    private fun startCountDown() {
        countdownTimer = object : CountDownTimer(totalDuration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                lastDuration = exoplayer.currentPosition

                val minutes = lastDuration / 60000
                val seconds = (lastDuration % 60000) / 1000
                val formattedTime = String.format("%02d:%02d", minutes, seconds)
                val progress = ((lastDuration * 100) /totalDuration).toFloat()

                val minutesTotal = totalDuration / 60000
                val secondsTotal = (totalDuration % 60000) / 1000
                val formattedTimeTotal = String.format("%02d:%02d", minutesTotal, secondsTotal)

                loadImageNotification(this@MusicService, currentPlayingTrack?.imageUrl.orEmpty()) { imageAlbum ->
                    notification = showNotification(
                        this@MusicService,
                        progress = progress,
                        musicID = currentPlayingTrack?.id.orEmpty(),
                        durationText = formattedTime,
                        durationTotalText = formattedTimeTotal,
                        title = currentPlayingTrack?.title.orEmpty(),
                        descriptions = currentPlayingTrack?.artist.orEmpty(),
                        image = imageAlbum
                    )

                    durationRunning = seconds + (minutes * 60)
                    durationTotal = currentPlayingTrack?.duration ?: 0L
                    durationRunningText = formattedTime
                    durationTotalText = formattedTimeTotal

                    updatePlaybackDurationToActivity(
                        title = currentPlayingTrack?.title.orEmpty(),
                        descriptions = currentPlayingTrack?.artist.orEmpty()
                    )

                    startForeground(Notification.NOTIFICATION_ID, notification)
                }
            }
            override fun onFinish() {
                durationRunning = 0L
                durationTotal = currentPlayingTrack?.duration ?: 0L
                durationRunningText = "00:00"
                durationTotalText = "00:00"
                playerStatus = PlayerStatus.STOP

                updatePlaybackDurationToActivity(
                    title = currentPlayingTrack?.title.orEmpty(),
                    descriptions = currentPlayingTrack?.artist.orEmpty()
                )

                stopMusic(currentPlayingTrack?.idPk ?: 0, 0)
                if (isShuffleEnable) setRandomPlaylistPosition()
            }
        }

        countdownTimer?.start()
    }

    private fun setRandomPlaylistPosition() {
        playerStatus = PlayerStatus.PLAY

        serviceScope.launch {
            val trackSize = RoomModule.provideDB(applicationContext).trackDao().getAllFavoriteTrack().size
            val randomSort = (0..trackSize).random()
            currentPlayingTrack = RoomModule.provideDB(applicationContext).trackDao().getTrackByIdPrimary(randomSort)

            totalDuration = abs(currentPlayingTrack?.duration ?: 0) * 1000
            withContext(Dispatchers.Main) {
                delay(1000)
                playMusic(currentPlayingTrack?.idPk ?: 0, exoplayer.currentPosition)
                runCountDown()
            }
        }
    }

    private fun playPlayback() {
        if (exoplayer.currentMediaItem != MediaItem.fromUri(currentPlayingTrack?.streamedUrl.orEmpty())
            || exoplayer.playbackState == Player.STATE_IDLE) {

            exoplayer.setMediaItem(MediaItem.fromUri(currentPlayingTrack?.streamedUrl.orEmpty()))
            exoplayer.volume = 1f
            exoplayer.prepare()
        }

        playerStatus = PlayerStatus.PLAY
        totalDuration = abs(currentPlayingTrack?.duration ?: 0) * 1000

        playMusic(currentPlayingTrack?.idPk ?: 0, exoplayer.currentPosition)
        runCountDown()
    }

    private fun pausePlayback() {
        playerStatus = PlayerStatus.PAUSE
        if (exoplayer.isPlaying) exoplayer.pause()

        updatePlaybackDurationToActivity()
        removeNotification()
    }

    private fun stopPlayback() {
        playerStatus = PlayerStatus.STOP
        exoplayer.release()

        updatePlaybackDurationToActivity()
        removeNotification()
        killService()
    }

    private fun updatePlaybackDurationToActivity(title: String,
                                                 descriptions: String) {
        val intent = Intent(Notification.BROADCAST_NAME)

        intent.putExtra(INTENT.PENDING_MUSIC_ID, currentPlayingTrack?.id)
        intent.putExtra(INTENT.PENDING_TITLE, title)
        intent.putExtra(INTENT.PENDING_DESCRIPTION, descriptions)
        intent.putExtra(INTENT.PENDING_MUSIC_STATUS,  playerStatus?.status)
        intent.putExtra(INTENT.PENDING_DURATION, durationRunning)
        intent.putExtra(INTENT.PENDING_DURATION_TOTAL, durationTotal)
        intent.putExtra(INTENT.PENDING_DURATION_TEXT, durationRunningText)
        intent.putExtra(INTENT.PENDING_DURATION_TOTAL_TEXT, durationTotalText)

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun updatePlaybackDurationToActivity() {
        val intent = Intent(Notification.BROADCAST_NAME)

        intent.putExtra(INTENT.PENDING_MUSIC_STATUS, playerStatus?.status)
        intent.putExtra(INTENT.PENDING_DURATION, durationRunning)
        intent.putExtra(INTENT.PENDING_DURATION_TOTAL, durationTotal)
        intent.putExtra(INTENT.PENDING_DURATION_TEXT, durationRunningText)
        intent.putExtra(INTENT.PENDING_DURATION_TOTAL_TEXT, durationTotalText)

        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    object ActionDetail {
        const val START_MODE = "_MODE_START_FOREGROUND"
        const val STOP_MODE = "_MODE_STOP_FOREGROUND"
        const val PAUSE_MODE = "_MODE_PAUSE_FOREGROUND"
        const val RESTART_MODE = "_MODE_RESTART_FOREGROUND"
        const val SHUFFLE_MODE = "_MODE_SHUFFLE_FOREGROUND"
        const val REPEAT_MODE = "_MODE_REPEAT_FOREGROUND"
        const val NEXT_MODE = "_MODE_NEXT_FOREGROUND"
        const val PREV_MODE = "_MODE_PREV_FOREGROUND"
    }

    object ActionKey {
        const val ACTION = "ACTION"
    }
    object TAG {
        const val REPEAT_MODE = "REPEAT_MODE"
        const val IS_SHUFFLE = "IS_SHUFFLE"
        const val MUSIC_ID = "MUSIC_ID"
    }

    object INTENT {
        const val PENDING_MUSIC_ID = "PENDING_MUSIC_ID"
        const val PENDING_MUSIC_STATUS = "PENDING_MUSIC_STATUS"
        const val PENDING_TITLE = "PENDING_TITLE"
        const val PENDING_DESCRIPTION = "PENDING_DESCRIPTION"
        const val PENDING_DURATION = "PENDING_DURATION"
        const val PENDING_DURATION_TOTAL = "PENDING_DURATION_TOTAL"
        const val PENDING_DURATION_TEXT = "PENDING_DURATION_TEXT"
        const val PENDING_DURATION_TOTAL_TEXT = "PENDING_DURATION_TOTAL_TEXT"
    }

    object Notification {
        const val BROADCAST_NAME = "musicBroadcast"
        const val START_FOREGROUND_ACTION = "com.view.musicplayer.spotifyclone.action.START_FOREGROUND"
        const val NOTIFICATION_ID = 123
    }

    enum class PlayerStatus(val status: String) {
        PLAY("PLAY"),
        PAUSE("PAUSE"),
        STOP("STOP"),
    }
}