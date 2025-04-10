package com.view.musicplayer.spotifyclone.constants

object Constants {
    const val CLIENT_KEY = "9c5aea19b9e0e94444e5a17fcc83738a"
    const val CLIENT_SECRET = "d2fd2bcf44a0edb6d8ccf01c0280e760"
    const val BASE_URL = "https://ws.audioscrobbler.com/"

    const val DEFAULT_MAX_PAGE = 20
    const val DEFAULT_FORMAT = "json"

    const val METHOD_GET_ALL_TOP_ARTIST = "chart.gettopartists"
    const val METHOD_GET_ALL_TOP_TRACK = "chart.gettoptracks"
    const val METHOD_GET_ALL_TOP_TRACK_BASED_GENRE = "tag.gettoptracks"

    const val METHOD_SEARCH_ARTIST = "artist.search"
    const val METHOD_SEARCH_ALBUM = "album.search"
}