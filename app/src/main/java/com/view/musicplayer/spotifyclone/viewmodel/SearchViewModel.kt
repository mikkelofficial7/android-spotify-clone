package com.view.musicplayer.spotifyclone.viewmodel

import android.content.Context
import com.view.musicplayer.spotifyclone.ext.SingleLiveEvent
import com.view.musicplayer.spotifyclone.ext.flowOnValue
import com.view.musicplayer.spotifyclone.network.response.Genre
import com.view.musicplayer.spotifyclone.network.response.Track
import com.view.musicplayer.spotifyclone.room.AppDb
import com.view.musicplayer.spotifyclone.room.model.PlaylistModel
import com.view.musicplayer.spotifyclone.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchViewModel(private val db: AppDb): BaseViewModel<Any?>() {
    val favoriteTrack = SingleLiveEvent<List<Track>>()
    val allGenre = SingleLiveEvent<List<Genre>>()
    val topTrack = SingleLiveEvent<List<Track>>()
    val listSearchArtist = SingleLiveEvent<ArrayList<Track>>()
    val listPlaylist = SingleLiveEvent<List<PlaylistModel>>()

    internal fun getAllGenre(context: Context) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                val allDataGenre = db.genreDao().getAllGenre()

                isLoadingEvent.postValue(false)
                allGenre.postValue(allDataGenre)
            }
        }
    }

    internal fun getSongRecommendation(context: Context) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                isLoadingEvent.postValue(false)
                val allTrack = db.trackDao().getAllTrack()

                topTrack.postValue(allTrack?.shuffled()?.take(5))
            }
        }
    }

    internal fun searchArtistOrSong(context: Context, artistName: String) {
        if (artistName.isBlank()) return
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                isLoadingEvent.postValue(false)
                val allSongOrArtist = db.trackDao().getAllTrackByArtistOrSongName(artistName)

                listSearchArtist.postValue(arrayListOf())
                listSearchArtist.postValue(allSongOrArtist as ArrayList<Track>)
            }
        }
    }

    internal fun getAllFavoriteTrack(context: Context) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                flowOnValue(db.trackDao().getAllFavoriteTrack()).collectLatest {
                    isLoadingEvent.postValue(false)
                    favoriteTrack.postValue(it.map { it.toTrack })
                }
            }
        }
    }

    internal fun addOrRemoveFavorite(context: Context, track: Track) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                val dataExist = db.trackDao().getFavoriteTrackById(track.id)

                dataExist?.let {
                    flowOnValue(db.trackDao().delete(it))
                } ?: kotlin.run {
                    flowOnValue(db.trackDao().insert(track.toFavoriteTrack))
                }

                isLoadingEvent.postValue(false)
                getAllFavoriteTrack(context)
            }
        }
    }
    internal fun getAllPlaylist(context: Context) {
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                flowOnValue(db.playlistDao().getAllPlaylistTrack()).collectLatest {
                    isLoadingEvent.postValue(false)
                    listPlaylist.postValue(it)
                }
            }
        }
    }

    internal fun addTrackToPlaylist(context: Context, track: Track, playlist: PlaylistModel?) {
        if (playlist == null) return
        executeJob(context) {
            safeScopeFun(context).launch(Dispatchers.IO) {
                val playlistFound = db.playlistDao().getPlaylistTrackById(playlist.idPk)

                val listAllTrackInPlaylist = arrayListOf<Track>()
                listAllTrackInPlaylist.addAll(playlistFound?.playlistTrack ?: arrayListOf())
                listAllTrackInPlaylist.add(track)

                val newSetPlaylist = PlaylistModel(
                    playlistName = playlistFound?.playlistName.toString(),
                    playlistIcon = playlistFound?.playlistIcon.toString(),
                    playlistCreated = playlistFound?.playlistCreated ?: 0,
                    playlistTrack = listAllTrackInPlaylist
                )

                db.playlistDao().delete(playlist)
                db.playlistDao().insert(newSetPlaylist)

                isLoadingEvent.postValue(false)
                getAllPlaylist(context)
            }
        }
    }
}