package com.view.musicplayer.spotifyclone.viewmodel

import android.content.Context
import android.util.Log
import com.view.musicplayer.spotifyclone.ext.SingleLiveEvent
import com.view.musicplayer.spotifyclone.ext.flowOnValue
import com.view.musicplayer.spotifyclone.network.Api
import com.view.musicplayer.spotifyclone.network.response.Track
import com.view.musicplayer.spotifyclone.room.AppDb
import com.view.musicplayer.spotifyclone.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BroadcastViewModel(val db: AppDb, val api: Api): BaseViewModel<Any?>() {
    internal var currentTrackId = SingleLiveEvent<String>().apply { value = "" }
    internal var currentTrackStatus = SingleLiveEvent<String>().apply { value = "" }

    internal var currentTrackDuration = SingleLiveEvent<Long>().apply { value = 0L }
    internal var currentTrackDurationTotal = SingleLiveEvent<Long>().apply { value = 0L }

    internal var currentTrackDurationText = SingleLiveEvent<String>().apply { value = "" }
    internal var currentTrackDurationTotalText = SingleLiveEvent<String>().apply { value = "" }

    internal var nextTrack = SingleLiveEvent<Track>().apply { value = Track.empty }
    internal var prevTrack = SingleLiveEvent<Track>().apply { value = Track.empty }

    internal fun getAllTrackList(context: Context) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                flowOnValue(api.searchArtist()).collectLatest { response ->
                    isLoadingEvent.postValue(false)

                    db.trackDao().deleteAllTrack()
                    response.data?.map {
                        db.trackDao().insertAllTrack(it)
                    }
                }
            }
        }
    }

    internal fun getNextTrack(context: Context, id: Int) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                executeJob(context) {
                    safeScopeFun(context).launch(Dispatchers.IO) {
                        Log.d("TAG", id.toString())

                        val trackSize = db.trackDao().getAllTrack()?.size ?: 0
                        val track = if (id > trackSize - 1) {
                            db.trackDao().getTrackByIdPrimary(0)
                        } else {
                            db.trackDao().getTrackByIdPrimary(id + 1)
                        }

                        nextTrack.postValue(track)
                    }
                }
            }
        }
    }

    internal fun getPrevTrack(context: Context, id: Int) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                executeJob(context) {
                    safeScopeFun(context).launch(Dispatchers.IO) {
                        Log.d("TAG", id.toString())
                        val trackSize = db.trackDao().getAllTrack()?.size ?: 0
                        val track = if (id < 1) {
                            db.trackDao().getTrackByIdPrimary(trackSize - 1)
                        } else {
                            db.trackDao().getTrackByIdPrimary(id - 1)
                        }

                        prevTrack.postValue(track)
                    }
                }
            }
        }
    }
}