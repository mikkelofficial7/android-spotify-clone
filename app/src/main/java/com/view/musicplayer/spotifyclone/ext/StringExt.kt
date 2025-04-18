package com.view.musicplayer.spotifyclone.ext

import android.content.Context
import android.net.Uri
import android.util.Log
import kotlin.time.Duration.Companion.seconds

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