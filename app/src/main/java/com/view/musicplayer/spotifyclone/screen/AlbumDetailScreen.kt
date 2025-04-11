package com.view.musicplayer.spotifyclone.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.view.musicplayer.spotifyclone.network.response.Track
import com.view.musicplayer.spotifyclone.ui.theme.Black80
import com.view.musicplayer.spotifyclone.viewmodel.AlbumDetailViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AlbumDetailScreen(
    viewModel: AlbumDetailViewModel = koinViewModel(),
    isShowPlayerButton: Boolean,
    onClickMusic: (Track) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().background(Black80)) {

    }
}