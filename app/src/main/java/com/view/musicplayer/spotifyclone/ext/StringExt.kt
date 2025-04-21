package com.view.musicplayer.spotifyclone.ext

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.view.musicplayer.spotifyclone.service.MusicService
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun Int.roundedNumber(): String {
    return if (this > 5000) return "5000+"
    else if (this > 3000) return "3000+"
    else if (this > 1000) return "1000+"
    else this.toString()
}

fun Long.toSecond(): Long {
    return this * 1000
}
fun Long.convertMillisToTime(): String {
    val seconds = this / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    val remainingHours = hours % 24
    val remainingMinutes = minutes % 60
    val remainingSeconds = seconds % 60

    return "${days}d ${remainingHours}h ${remainingMinutes}m ${remainingSeconds}s"
}

fun Int.convertToUri(context: Context): Uri? {
    return Uri.parse("android.resource://${context.packageName}/$this")
}

fun Long.convertTimeToHHSS(): String {
    val time = this.convertTimeToMinuteSecond()
    return String.format("%02d:%02d", time.first, time.second)
}

fun Long.convertTimeToMinuteSecond(): Pair<Long, Long> {
    val minutes = this / 60000
    val seconds = (this % 60000) / 1000
    return Pair(minutes, seconds)
}

fun MusicService.PlayerStatus.isGroupPlay(): Boolean {
    return this == MusicService.PlayerStatus.PLAY
            || this == MusicService.PlayerStatus.RESTART
            || this == MusicService.PlayerStatus.PREV_PLAY
            || this == MusicService.PlayerStatus.NEXT_PLAY
            || this == MusicService.PlayerStatus.SHUFFLE
            || this == MusicService.PlayerStatus.REPEAT
}

fun String.convertToPlayerStatus(): MusicService.PlayerStatus? {
    return MusicService.PlayerStatus.values().find { it.status == this }
}

fun getCurrentDate(): String? {
    return LocalDate.now().format(DateTimeFormatter.ISO_DATE) //"YYYY-MM-DD"
}

fun String.extractTextIntoDesiredListText(): List<String> {
    val regex = "\\[(.*?)]".toRegex()
    return regex.findAll(this).map { it.groupValues[1] }.toList()
}

fun Any.toJson(): String? {
    return Gson().toJson(this)
}

fun generateFirstPrompt(amount: Int): String {
    return "Give me $amount title of music section of today vibe: ${getCurrentDate()}. The response should show only $amount title of music section and wrap each with [] and don't forget to use millenial languages"
}