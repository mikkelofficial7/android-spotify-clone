package com.view.musicplayer.spotifyclone.viewmodel

import android.content.Context
import com.view.musicplayer.spotifyclone.constants.Constants
import com.view.musicplayer.spotifyclone.ext.SingleLiveEvent
import com.view.musicplayer.spotifyclone.ext.extractTextIntoDesiredListText
import com.view.musicplayer.spotifyclone.ext.flowOnValue
import com.view.musicplayer.spotifyclone.ext.generateFirstPrompt
import com.view.musicplayer.spotifyclone.ext.getCurrentDate
import com.view.musicplayer.spotifyclone.network.Api
import com.view.musicplayer.spotifyclone.network.OpenRouterApi
import com.view.musicplayer.spotifyclone.network.request.OpenRouterMessage
import com.view.musicplayer.spotifyclone.network.request.OpenRouterRequest
import com.view.musicplayer.spotifyclone.network.response.Genre
import com.view.musicplayer.spotifyclone.network.response.OpenAIFlagDb
import com.view.musicplayer.spotifyclone.network.response.SongRecommendation
import com.view.musicplayer.spotifyclone.network.response.Track
import com.view.musicplayer.spotifyclone.room.AppDb
import com.view.musicplayer.spotifyclone.service.MusicService
import com.view.musicplayer.spotifyclone.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivityViewModel(val db: AppDb, val api: Api, val openRouterApi: OpenRouterApi): BaseViewModel<Any?>() {
    internal var finishLoad = SingleLiveEvent<Boolean>().apply { value = false }

    internal var currentTrack = SingleLiveEvent<Track>().apply { value = Track.empty }
    internal var currentTrackStatus = SingleLiveEvent<String>().apply { value = MusicService.PlayerStatus.STOP.status }

    internal var currentTrackDuration = SingleLiveEvent<Long>().apply { value = 0L }
    internal var currentTrackDurationTotal = SingleLiveEvent<Long>().apply { value = 0L }

    internal var currentTrackDurationText = SingleLiveEvent<String>().apply { value = "" }
    internal var currentTrackDurationTotalText = SingleLiveEvent<String>().apply { value = "" }

    private fun getOpenRouterRequest(prompt: String) : OpenRouterRequest {
       val message = ArrayList<OpenRouterMessage>()

        message.add(
           OpenRouterMessage(
               role = Constants.AI_ROLE,
               content = prompt
           )
       )

        return OpenRouterRequest(
           model = Constants.AI_MODEL,
           messages = message
       )
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
                val flagOpenAi = db.openAiDao().getAllOpenAiFlag()?.firstOrNull()

                if (flagOpenAi != null && flagOpenAi.lastHitDate == getCurrentDate()) {
                    finishLoad.postValue(true)
                    return@launch
                }

                flowOnValue(api.getRecommendation()).collect { response ->
                    isLoadingEvent.postValue(false)
                    val allTrack = db.trackDao().getAllTrack()
                    val recommendTextSize = response.data?.size ?: 0

                    runOpenAi(
                        context,
                        generateFirstPrompt(recommendTextSize),
                        response.data ?: listOf(),
                        allTrack ?: listOf()
                    )
                }
            }
        }
    }

    private suspend fun runOpenAi(context: Context,
                                  prompt: String,
                                  listRecommend: List<SongRecommendation>,
                                  listTrack: List<Track>
    ) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                flowOnValue(
                   onRunning = { openRouterApi.getResponse(getOpenRouterRequest(prompt)) },
                   onError = {
                       val listTrackChunked = listTrack.chunked(listTrack.size / listRecommend.size)

                       listRecommend.mapIndexed { index, listRecommend ->
                           val results = SongRecommendation(
                               idPk = index,
                               id = listRecommend.id,
                               title = listRecommend.title,
                               listTrack = listTrackChunked[index] as ArrayList<Track>
                           )
                           db.recommendDao().insert(results)
                       }

                       db.openAiDao().insert(OpenAIFlagDb(lastHitDate = getCurrentDate().orEmpty()))
                       finishLoad.postValue(true)
                   }
                ).collectLatest { response ->
                    val listExtracted = response.choices.first().message.content.extractTextIntoDesiredListText()
                    val listTrackChunked = listTrack.chunked(listTrack.size / listRecommend.size)

                    listRecommend.mapIndexed { index, listRecommend ->
                        val results = SongRecommendation(
                            idPk = index,
                            id = listRecommend.id,
                            title = listExtracted[index],
                            listTrack = listTrackChunked[index] as ArrayList<Track>
                        )
                        db.recommendDao().insert(results)
                    }

                    db.openAiDao().insert(OpenAIFlagDb(lastHitDate = getCurrentDate().orEmpty()))
                    finishLoad.postValue(true)
                }
            }
        }
    }

}