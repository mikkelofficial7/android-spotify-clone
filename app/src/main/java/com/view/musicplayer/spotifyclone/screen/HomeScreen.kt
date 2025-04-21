package com.view.musicplayer.spotifyclone.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.view.musicplayer.spotifyclone.ext.isGroupPlay
import com.view.musicplayer.spotifyclone.navigation.routeToLogin
import com.view.musicplayer.spotifyclone.network.response.Track
import com.view.musicplayer.spotifyclone.screen.shared.EmptyView
import com.view.musicplayer.spotifyclone.screen.shared.MusicItemCard
import com.view.musicplayer.spotifyclone.screen.shared.PlayerButton
import com.view.musicplayer.spotifyclone.service.MusicService
import com.view.musicplayer.spotifyclone.ui.theme.Black80
import com.view.musicplayer.spotifyclone.ui.theme.Transparent
import com.view.musicplayer.spotifyclone.ui.theme.White80
import com.view.musicplayer.spotifyclone.viewmodel.HomePageViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomePageViewModel = koinViewModel(),
    navController: NavController,
    isUserHasLogin: Boolean,
    currentPlaying: Track,
    isShowPlayerButton: Boolean,
    playerStatus: MusicService.PlayerStatus,
    trackProgress: Long,
    trackProgressTotal: Long,
    trackProgressText: String,
    trackProgressTotalText: String,
    isShuffle: Boolean,
    onClickMusic: (Track) -> Unit,
    onPlayPauseClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    onPreviousClick: () -> Unit = {},
    onRefreshClick: () -> Unit = {},
    onShuffleClick: () -> Unit = {}
) {
    val context: Context = LocalContext.current
    val recommendChart by viewModel.recommendationChart.observeAsState()
    val favoriteTrack by viewModel.favoriteTrack.observeAsState()
    val listPlaylist by viewModel.listPlaylist.observeAsState()

    val firstTrack = recommendChart?.first()?.listTrack ?: arrayListOf()
    val secondTrack = recommendChart?.get(1)?.listTrack ?: arrayListOf()
    val thirdTrack = recommendChart?.get(2)?.listTrack ?: arrayListOf()
    val fourthTrack = recommendChart?.last()?.listTrack ?: arrayListOf()

    LaunchedEffect(Unit) {
        viewModel.getAllFavoriteTrack(context)
        viewModel.getRecommendation(context)
        viewModel.getAllPlaylist(context)
    }

    Column {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Black80)
        ) {
            item {
                Text(
                    text = recommendChart?.get(0)?.title.orEmpty(),
                    style = MaterialTheme.typography.bodyLarge.copy(color = White80),
                    modifier = Modifier.padding(16.dp)
                )
            }
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                ) {
                    items(firstTrack) { artist ->
                        MusicItemCard(
                            navController = navController,
                            currentPlaying = currentPlaying,
                            track = artist,
                            listPlaylist = listPlaylist,
                            isShowGotoDetailButton = currentPlaying.id == artist.id,
                            isFavorite = favoriteTrack?.find { it.id == artist.id } != null,
                            onClick = {
                                if(!isUserHasLogin) {
                                    navController.routeToLogin()
                                    return@MusicItemCard
                                }
                                onClickMusic(artist)
                            },
                            onAddFavorite = {
                                if(!isUserHasLogin) {
                                    navController.routeToLogin()
                                    return@MusicItemCard
                                }
                                viewModel.addOrRemoveFavorite(context, it)
                            },
                            onAddPlaylist = { track, playlist ->
                                if(!isUserHasLogin) {
                                    navController.routeToLogin()
                                    return@MusicItemCard
                                }
                                viewModel.addTrackToPlaylist(context, track, playlist)
                            }
                        )
                    }
                    if (firstTrack.isEmpty()) {
                        item {
                            EmptyView()
                        }
                    }
                }
            }

            item {
                Text(
                    text = recommendChart?.get(1)?.title.orEmpty(),
                    style = MaterialTheme.typography.bodyLarge.copy(color = White80),
                    modifier = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                )
            }
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                ) {
                    items(secondTrack.orEmpty()) {  artist ->
                        MusicItemCard(
                            navController = navController,
                            currentPlaying = currentPlaying,
                            track = artist,
                            listPlaylist = listPlaylist,
                            isShowGotoDetailButton = currentPlaying.id == artist.id,
                            isFavorite = favoriteTrack?.find { it.id == artist.id } != null,
                            onClick = {
                                if(!isUserHasLogin) {
                                    navController.routeToLogin()
                                    return@MusicItemCard
                                }
                                onClickMusic(artist)
                            },
                            onAddFavorite = {
                                if(!isUserHasLogin) {
                                    navController.routeToLogin()
                                    return@MusicItemCard
                                }
                                viewModel.addOrRemoveFavorite(context, it)
                            },
                            onAddPlaylist = { track, playlist ->
                                if(!isUserHasLogin) {
                                    navController.routeToLogin()
                                    return@MusicItemCard
                                }
                                viewModel.addTrackToPlaylist(context, track, playlist)
                            }
                        )
                    }
                    if (secondTrack.isEmpty()) {
                        item {
                            EmptyView()
                        }
                    }
                }
            }

            item {
                Text(
                    text = recommendChart?.get(2)?.title.orEmpty(),
                    style = MaterialTheme.typography.bodyLarge.copy(color = White80),
                    modifier = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                )
            }
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Transparent)
                        .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                ) {
                    items(thirdTrack) { artist ->
                        MusicItemCard(
                            navController = navController,
                            currentPlaying = currentPlaying,
                            track = artist,
                            listPlaylist = listPlaylist,
                            isShowGotoDetailButton = currentPlaying.id == artist.id,
                            isFavorite = favoriteTrack?.find { it.id == artist.id } != null,
                            onClick = {
                                if(!isUserHasLogin) {
                                    navController.routeToLogin()
                                    return@MusicItemCard
                                }
                                onClickMusic(artist)
                            },
                            onAddFavorite = {
                                if(!isUserHasLogin) {
                                    navController.routeToLogin()
                                    return@MusicItemCard
                                }
                                viewModel.addOrRemoveFavorite(context, it)
                            },
                            onAddPlaylist = { track, playlist ->
                                if(!isUserHasLogin) {
                                    navController.routeToLogin()
                                    return@MusicItemCard
                                }
                                viewModel.addTrackToPlaylist(context, track, playlist)
                            }
                        )
                    }
                    if (thirdTrack.isEmpty()) {
                        item {
                            EmptyView()
                        }
                    }
                }
            }

            item {
                Text(
                    text = recommendChart?.get(3)?.title.orEmpty(),
                    style = MaterialTheme.typography.bodyLarge.copy(color = White80),
                    modifier = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                )
            }
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Transparent)
                        .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                ) {
                    items(fourthTrack) { artist ->
                        MusicItemCard(
                            navController = navController,
                            currentPlaying = currentPlaying,
                            track = artist,
                            listPlaylist = listPlaylist,
                            isShowGotoDetailButton = currentPlaying.id == artist.id,
                            isFavorite = favoriteTrack?.find { it.id == artist.id } != null,
                            onClick = {
                                if(!isUserHasLogin) {
                                    navController.routeToLogin()
                                    return@MusicItemCard
                                }
                                onClickMusic(artist)
                            },
                            onAddFavorite = {
                                if(!isUserHasLogin) {
                                    navController.routeToLogin()
                                    return@MusicItemCard
                                }
                                viewModel.addOrRemoveFavorite(context, it)
                            },
                            onAddPlaylist = { track, playlist ->
                                if(!isUserHasLogin) {
                                    navController.routeToLogin()
                                    return@MusicItemCard
                                }
                                viewModel.addTrackToPlaylist(context, track, playlist)
                            }
                        )
                    }
                    if (fourthTrack.isEmpty()) {
                        item {
                            EmptyView()
                        }
                    }
                }
            }
        }

        if (isShowPlayerButton) {
            PlayerButton(
                duration = trackProgress,
                durationTotal = trackProgressTotal,
                durationText = trackProgressText,
                durationTotalText = trackProgressTotalText,
                isShuffle = isShuffle,
                isPlaying = playerStatus.isGroupPlay(),
                onPlayPauseClick = onPlayPauseClick,
                onNextClick = onNextClick,
                onPreviousClick = onPreviousClick,
                onRefreshClick = onRefreshClick,
                onShuffleClick = onShuffleClick
            )
        }
    }
}