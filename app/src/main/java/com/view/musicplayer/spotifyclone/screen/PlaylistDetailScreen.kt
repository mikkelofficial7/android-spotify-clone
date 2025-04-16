package com.view.musicplayer.spotifyclone.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.view.musicplayer.spotifyclone.network.response.Track
import com.view.musicplayer.spotifyclone.viewmodel.PlaylistDetailViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaylistDetailScreen(
    viewModel: PlaylistDetailViewModel = koinViewModel(),
    navController: NavController,
    isShowPlayerButton: Boolean,
    playlistId: String,
    currentPlaying: Track,
    onClickMusic: (Track) -> Unit
) {

}