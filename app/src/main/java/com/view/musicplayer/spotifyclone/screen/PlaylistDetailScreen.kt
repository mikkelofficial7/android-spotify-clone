package com.view.musicplayer.spotifyclone.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.view.musicplayer.spotifyclone.SetBackStackViewResult
import com.view.musicplayer.spotifyclone.network.response.Track
import com.view.musicplayer.spotifyclone.screen.shared.BackButton
import com.view.musicplayer.spotifyclone.screen.shared.EmptyView
import com.view.musicplayer.spotifyclone.screen.shared.MusicItemCard
import com.view.musicplayer.spotifyclone.screen.shared.PlayerButton
import com.view.musicplayer.spotifyclone.ui.theme.Black100
import com.view.musicplayer.spotifyclone.ui.theme.Black80
import com.view.musicplayer.spotifyclone.ui.theme.White80
import com.view.musicplayer.spotifyclone.viewmodel.PlaylistDetailViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlaylistDetailScreen(
    viewModel: PlaylistDetailViewModel = koinViewModel(),
    navController: NavController,
    isShowPlayerButton: Boolean,
    playlistId: String,
    currentPlaying: Track,
    onClickMusic: (Track) -> Unit
) {
    val context = LocalContext.current
    val playlistData by viewModel.playlistData.observeAsState()
    val favoriteTrack by viewModel.favoriteTrack.observeAsState()

    val backCallback = SetBackStackViewResult(navController = navController, key = "onDeletedTrackItem", value = true)

    LaunchedEffect(Unit) {
        viewModel.getAllPlaylistById(context, playlistId)
        viewModel.getAllFavoriteTrack(context)
    }

    BackHandler {
        backCallback.value()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black80),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButton {
                    backCallback.value()
                }
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = playlistData?.playlistName.orEmpty(),
                    color = White80,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(10.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                item {
                    if (playlistData?.playlistTrack.isNullOrEmpty()) {
                        EmptyView(20)
                    }
                }
                items(playlistData?.playlistTrack.orEmpty()) { track ->
                    MusicItemCard(
                        navController = navController,
                        currentPlaying = currentPlaying,
                        track = track,
                        isShowRemoveFromPlaylist = true,
                        isShowGotoDetailButton = currentPlaying.id == track.id,
                        isFavorite = favoriteTrack?.find { it.id == track.id } != null,
                        onClick = {
                            onClickMusic(track)
                        },
                        onAddFavorite = {
                            viewModel.addOrRemoveFavorite(context, it)
                        },
                        onRemoveTrackPlaylist = {
                            viewModel.removeTrackFromPlaylist(context, track, playlistData)
                        }
                    )
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp))
                }
                item {
                    if (isShowPlayerButton) {
                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp))
                    }
                }
            }
        }
        if (isShowPlayerButton) {
            PlayerButton()
        }
    }
}