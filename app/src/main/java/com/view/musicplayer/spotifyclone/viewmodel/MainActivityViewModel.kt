package com.view.musicplayer.spotifyclone.viewmodel

import android.content.Context
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.vertexai.vertexAI
import com.google.gson.Gson
import com.view.musicplayer.spotifyclone.ext.EmptyClass
import com.view.musicplayer.spotifyclone.ext.SingleLiveEvent
import com.view.musicplayer.spotifyclone.ext.flowOnValue
import com.view.musicplayer.spotifyclone.network.Api
import com.view.musicplayer.spotifyclone.network.response.Genre
import com.view.musicplayer.spotifyclone.network.response.SongRecommendation
import com.view.musicplayer.spotifyclone.network.response.Track
import com.view.musicplayer.spotifyclone.room.AppDb
import com.view.musicplayer.spotifyclone.service.MusicService
import com.view.musicplayer.spotifyclone.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivityViewModel(val db: AppDb, val api: Api): BaseViewModel<Any?>() {
    internal var finishLoad = SingleLiveEvent<EmptyClass>()

    internal var currentTrack = SingleLiveEvent<Track>().apply { value = Track.empty }
    internal var currentTrackStatus = SingleLiveEvent<String>().apply { value = MusicService.PlayerStatus.STOP.status }

    internal var currentTrackDuration = SingleLiveEvent<Long>().apply { value = 0L }
    internal var currentTrackDurationTotal = SingleLiveEvent<Long>().apply { value = 0L }

    internal var currentTrackDurationText = SingleLiveEvent<String>().apply { value = "" }
    internal var currentTrackDurationTotalText = SingleLiveEvent<String>().apply { value = "" }

    internal fun runGeminiGenAi(context: Context) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                val generativeModel = Firebase.vertexAI.generativeModel("gemini-2.0-flash")
                val response =
                    generativeModel.generateContent("Write a story about the green robot")

                Log.d("GENAI", Gson().toJson(response))
            }
        }
    }
    internal fun getAllTrackList(context: Context) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                flowOnValue(api.searchArtist()).collectLatest { response ->
                    isLoadingEvent.postValue(false)

                    response.data?.mapIndexed { index, track ->
                        val trackNew = Track(
                            idPk = index,
                            id = track.id,
                            title = track.title,
                            artist = track.artist,
                            releaseDate = track.releaseDate,
                            totalListener = track.totalListener,
                            description = track.description,
                            imageUrl = track.imageUrl,
                            streamedUrl = track.streamedUrl,
                            duration = track.duration,
                            genre = track.genre
                        )
                        db.trackDao().insertAllTrack(trackNew)
                    }

                    getAllGenre(context)
                    getRecommendation(context)
                }
            }
        }
    }

    internal fun getAllGenre(context: Context) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                flowOnValue(api.getAllGenre()).collectLatest { response ->
                    isLoadingEvent.postValue(false)

                    response.data?.mapIndexed { index, genre ->
                        val trackByGenre = db.trackDao().getAllTrackByGenreName(genre.name)

                        val genreNew = Genre(
                            idPk = index,
                            name = genre.name,
                            imageUrl = genre.imageUrl,
                            color = genre.color,
                            description = genre.description,
                            listTrack = trackByGenre as ArrayList<Track>
                        )
                        db.genreDao().insert(genreNew)
                    }
                }
            }
        }
    }

    private fun getRecommendation(context: Context) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                flowOnValue(api.getRecommendation()).collect { response ->
                    isLoadingEvent.postValue(false)

                    val allTrack = db.trackDao().getAllTrack()

                    response.data?.mapIndexed { index, songRecommendation ->
                        val result = SongRecommendation(
                            idPk = index,
                            id = songRecommendation.id,
                            title = songRecommendation.title,
                            listTrack = allTrack?.shuffled()?.take(5) as ArrayList<Track>
                        )
                        db.recommendDao().insert(result)
                    }

                    finishLoad.postValue(EmptyClass())
                }
            }
        }
    }
}