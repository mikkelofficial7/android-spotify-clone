package com.view.musicplayer.spotifyclone.service

import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.view.musicplayer.spotifyclone.di.RoomModule
import com.view.musicplayer.spotifyclone.ext.LogExt
import com.view.musicplayer.spotifyclone.ext.convertTimeToHHSS
import com.view.musicplayer.spotifyclone.ext.convertTimeToMinuteSecond
import com.view.musicplayer.spotifyclone.ext.isGroupPlay
import com.view.musicplayer.spotifyclone.ext.loadImageNotification
import com.view.musicplayer.spotifyclone.network.response.Track
import com.view.musicplayer.spotifyclone.service.MusicService.Notification.NOTIFICATION_ID
import com.view.musicplayer.spotifyclone.service.builder.NotificationLayoutBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MusicService : Service() {
    private val exoplayer: ExoPlayer by lazy {
        ExoPlayer.Builder(this).build()
    }

    private var countdownJob: Job? = null
    private var isShuffleEnable: Boolean = false
    private var currentPlayingTrack: Track? = null
    private var playerStatus: PlayerStatus? = null

    var durationRunning: Long = 0L
    var durationTotal: Long = 0L
    var durationRunningText: String = ""
    var durationTotalText: String = ""

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)
    private val serviceScopeMain = CoroutineScope(Dispatchers.Main + serviceJob)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) {
            return START_NOT_STICKY
        }

        val musicId = intent.getStringExtra(TAG.MUSIC_ID) ?: ""
        val isShuffle = intent.getBooleanExtra(TAG.IS_SHUFFLE, false)
        val repeatMode = intent.getIntExtra(TAG.REPEAT_MODE, -1)

        serviceScope.launch {
            if (musicId.isNotEmpty()) {
                currentPlayingTrack = RoomModule.provideDB(applicationContext).trackDao().getTrackById(musicId)
            }
            LogExt.d("TAG", "Playback is preparing...${currentPlayingTrack?.title.toString()}")

            withContext(Dispatchers.Main) {
                if (intent.action == Notification.START_FOREGROUND_ACTION) {
                    val currentPlayerState = intent.getStringExtra(ActionKey.ACTION) ?: ""

                    LogExt.d("TAG", "State: ${currentPlayerState}")

                    when (currentPlayerState) {
                        ActionDetail.START_MODE -> {
                            delay(500)
                            playerStatus = PlayerStatus.PLAY
                            playPlayback()
                        }
                        ActionDetail.PAUSE_MODE -> {
                            delay(500)
                            playerStatus = PlayerStatus.PAUSE
                            pausePlayback()
                        }
                        ActionDetail.STOP_MODE -> {
                            delay(500)
                            playerStatus = PlayerStatus.STOP
                            stopPlayback()
                        }
                        ActionDetail.NEXT_MODE -> {
                            playerStatus = PlayerStatus.NEXT_PLAY
                            delay(500)
                            setNextTrack()
                        }
                        ActionDetail.PREV_MODE -> {
                            playerStatus = PlayerStatus.PREV_PLAY
                            delay(500)
                            setPrevTrack()
                        }
                        ActionDetail.RESTART_MODE -> {
                            playerStatus = PlayerStatus.RESTART
                            playPlayback(isRefreshSamePlayback = true)
                        }
                        ActionDetail.SHUFFLE_MODE -> {
                            isShuffleEnable = isShuffle
                            updatePlaybackDurationToActivity()
                        }
                        ActionDetail.REPEAT_MODE -> {
                            playerStatus = PlayerStatus.REPEAT
                            exoplayer.repeatMode = repeatMode
                        }
                    }

                }
            }
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        currentPlayingTrack = null
        exoplayer.release()
        countdownJob?.cancel()
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

    private fun playMusic(position: Int, lastPlaybackPosition: Long) {
        exoplayer.seekTo(position, lastPlaybackPosition)
        exoplayer.play()
    }

    private fun startCountDown() {
        LogExt.d("TAG", "Playback is ready...${currentPlayingTrack?.title.toString()}")

        exoplayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_READY) {
                    var imageNotification: Bitmap?
                    val interval = 1000L
                    val totalDuration = exoplayer.duration

                    loadImageNotification(this@MusicService, currentPlayingTrack?.imageUrl.orEmpty()) { imageAlbum ->
                        imageNotification = imageAlbum

                        countdownJob?.cancel()
                        countdownJob = serviceScopeMain.launch {
                            var runningDuration = withContext(Dispatchers.Main) { exoplayer.currentPosition }

                            while (isActive && runningDuration < totalDuration) {
                                val formattedTime = runningDuration.convertTimeToHHSS()
                                val formattedTimeTotal = totalDuration.convertTimeToHHSS()
                                val progress = ((runningDuration * 100) / totalDuration).toFloat()
                                val time = runningDuration.convertTimeToMinuteSecond()

                                durationRunning = time.second + (time.first * 60)
                                durationTotal = currentPlayingTrack?.duration ?: 0L
                                durationRunningText = formattedTime
                                durationTotalText = formattedTimeTotal

                                buildNotificationLayout(
                                    currentPlayingTrack,
                                    progress,
                                    formattedTime,
                                    formattedTimeTotal,
                                    imageNotification
                                )

                                updatePlaybackDurationToActivity()

                                delay(interval)

                                if (playerStatus?.isGroupPlay() == true) runningDuration += interval
                            }

                            withContext(Dispatchers.Main) {
                                onFinishRun()
                            }
                        }
                    }
                }
            }
        })
    }

    private fun onFinishRun() {
        LogExt.d("TAG", "Playback finish running..")
        playerStatus = if (isShuffleEnable) PlayerStatus.SHUFFLE else PlayerStatus.NEXT_PLAY

        if (isShuffleEnable) setRandomTrack() else setNextTrack()
    }

    private fun setRandomTrack() {
        serviceScope.launch {
            val totalTrack = RoomModule.provideDB(applicationContext).trackDao().getAllTrack()?.size ?: 0

            val nextIdTrack = (0 until totalTrack - 1).random()

            currentPlayingTrack = RoomModule.provideDB(applicationContext).trackDao().getTrackByIdPrimary(nextIdTrack)
            withContext(Dispatchers.Main) {
                playPlayback()
            }
        }
    }

    private fun setNextTrack() {
        serviceScope.launch {
            val totalTrack = RoomModule.provideDB(applicationContext).trackDao().getAllTrack()?.size ?: 0

            val nextIdTrack = if ((currentPlayingTrack?.idPk ?: 0) > (totalTrack - 1)) {
                0
            } else {
                (currentPlayingTrack?.idPk ?: 0) + 1
            }

            currentPlayingTrack = RoomModule.provideDB(applicationContext).trackDao().getTrackByIdPrimary(nextIdTrack)
            withContext(Dispatchers.Main) {
                playPlayback()
            }
        }
    }

    private fun setPrevTrack() {
        serviceScope.launch {
            val totalTrack = RoomModule.provideDB(applicationContext).trackDao().getAllTrack()?.size ?: 0

            val nextIdTrack = if ((currentPlayingTrack?.idPk ?: 0) == 0) {
                totalTrack - 1
            } else {
                (currentPlayingTrack?.idPk ?: 0) - 1
            }

            currentPlayingTrack = RoomModule.provideDB(applicationContext).trackDao().getTrackByIdPrimary(nextIdTrack)
            withContext(Dispatchers.Main) {
                playPlayback()
            }
        }
    }

    private fun playPlayback(isRefreshSamePlayback: Boolean = false) {
        if (exoplayer.currentMediaItem != MediaItem.fromUri(currentPlayingTrack?.streamedUrl.orEmpty())
            || exoplayer.playbackState == Player.STATE_IDLE
            || isRefreshSamePlayback) {

            exoplayer.setMediaItem(MediaItem.fromUri(currentPlayingTrack?.streamedUrl.orEmpty()))
            exoplayer.volume = 1f
            exoplayer.prepare()
        }

        playMusic(currentPlayingTrack?.idPk ?: 0, exoplayer.currentPosition)
        startCountDown()
    }

    private fun pausePlayback() {
        if (exoplayer.isPlaying) {
            exoplayer.pause()
        }

        updatePlaybackDurationToActivity()
        removeNotification()
    }

    private fun stopPlayback() {
        exoplayer.release()

        updatePlaybackDurationToActivity()
        removeNotification()
        killService()
    }

    private fun updatePlaybackDurationToActivity() {
        val intent = Intent(Notification.BROADCAST_NAME)

        intent.putExtra(INTENT.PENDING_MUSIC_DATA, currentPlayingTrack)
        intent.putExtra(INTENT.PENDING_TITLE, currentPlayingTrack?.title)
        intent.putExtra(INTENT.PENDING_DESCRIPTION, currentPlayingTrack?.description)
        intent.putExtra(INTENT.PENDING_MUSIC_STATUS,  playerStatus?.status)
        intent.putExtra(INTENT.PENDING_DURATION, durationRunning)
        intent.putExtra(INTENT.PENDING_DURATION_TOTAL, durationTotal)
        intent.putExtra(INTENT.PENDING_DURATION_TEXT, durationRunningText)
        intent.putExtra(INTENT.PENDING_DURATION_TOTAL_TEXT, durationTotalText)

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun buildNotificationLayout(
        track: Track?,
        progress: Float,
        formattedTime: String,
        formattedTimeTotal: String,
        imageNotification: Bitmap?
    ) {

        val notification = NotificationLayoutBuilder.showCustomNotification(
            context = this@MusicService,
            progress = progress,
            track = track,
            durationText = formattedTime,
            durationTotalText = formattedTimeTotal,
            title = track?.title.orEmpty(),
            descriptions = "by ${track?.artist.orEmpty()}",
            image = imageNotification,
            isPause = playerStatus == PlayerStatus.PAUSE
        )

        startForeground(NOTIFICATION_ID, notification)
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

    object ActionNotification {
        const val ACTION_PLAY = "ACTION_PLAY"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_NEXT = "ACTION_NEXT"
        const val ACTION_PREV = "ACTION_PREV"
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
        const val PENDING_MUSIC_DATA = "PENDING_MUSIC_DATA"
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
        PREV_PLAY("PREV_PLAY"),
        NEXT_PLAY("NEXT_PLAY"),
        SHUFFLE("SHUFFLE"),
        PLAY("PLAY"),
        PAUSE("PAUSE"),
        STOP("STOP"),
        RESTART("RESTART"),
        REPEAT("REPEAT"),
    }
}