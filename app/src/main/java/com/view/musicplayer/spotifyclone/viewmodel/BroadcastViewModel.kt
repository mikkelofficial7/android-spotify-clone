package com.view.musicplayer.spotifyclone.viewmodel

import com.view.musicplayer.spotifyclone.ext.SingleLiveEvent
import com.view.musicplayer.spotifyclone.viewmodel.base.BaseViewModel

class BroadcastViewModel: BaseViewModel<Any?>() {
    internal var currentTrackId = SingleLiveEvent<String>().apply { value = "" }
    internal var currentTrackStatus = SingleLiveEvent<String>().apply { value = "" }

    internal var currentTrackDuration = SingleLiveEvent<Long>().apply { value = 0L }
    internal var currentTrackDurationTotal = SingleLiveEvent<Long>().apply { value = 0L }

    internal var currentTrackDurationText = SingleLiveEvent<String>().apply { value = "" }
    internal var currentTrackDurationTotalText = SingleLiveEvent<String>().apply { value = "" }
}