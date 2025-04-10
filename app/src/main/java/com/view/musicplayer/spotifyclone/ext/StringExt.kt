package com.view.musicplayer.spotifyclone.ext

fun Int.roundedNumber(): String {
    return if (this > 5000) return "5000+"
    else if (this > 3000) return "3000+"
    else if (this > 1000) return "1000+"
    else this.toString()
}