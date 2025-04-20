package com.view.musicplayer.spotifyclone.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.view.musicplayer.spotifyclone.ext.isGroupPlay
import com.view.musicplayer.spotifyclone.network.response.Track
import com.view.musicplayer.spotifyclone.screen.shared.BackButton
import com.view.musicplayer.spotifyclone.screen.shared.ImageLoader
import com.view.musicplayer.spotifyclone.screen.shared.PlayerButton
import com.view.musicplayer.spotifyclone.service.MusicService
import com.view.musicplayer.spotifyclone.ui.theme.Black80
import com.view.musicplayer.spotifyclone.ui.theme.White80

@Composable
fun SongDetailScreen(
    navController: NavController,
    currentPlaying: Track,
    playerStatus: MusicService.PlayerStatus,
    trackProgress: Long,
    trackProgressTotal: Long,
    trackProgressText: String,
    isShuffle: Boolean,
    trackProgressTotalText: String,
    onPlayPauseClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    onPreviousClick: () -> Unit = {},
    onShuffleClick: () -> Unit = {},
    onRefreshClick: () -> Unit = {}
) {
    BackHandler {
        navController.popBackStack()
    }

   Column(
       modifier = Modifier
           .fillMaxSize()
           .background(Black80)
   ) {
       Row(
           modifier = Modifier
               .fillMaxWidth()
               .height(80.dp)
               .padding(horizontal = 10.dp, vertical = 10.dp),
           verticalAlignment = Alignment.Top
       ) {
           BackButton {
             navController.popBackStack()
           }
           Spacer(modifier = Modifier.width(10.dp))
           Text(
               text = "${currentPlaying.title}\nby ${currentPlaying.artist}", // song title
               color = White80,
               fontSize = 14.sp,
               textAlign = TextAlign.Center,
               fontWeight = FontWeight.Light,
               modifier = Modifier
                   .fillMaxWidth()
                   .weight(1f)
           )
           Spacer(modifier = Modifier.width(40.dp))
       }
       Column(
           modifier = Modifier
               .fillMaxWidth()
               .fillMaxHeight()
               .weight(1f)
       ) {
           Box(
               contentAlignment = Alignment.Center,
               modifier = Modifier
                   .padding(20.dp)
                   .fillMaxSize()
           ) {
               ImageLoader(
                   url = currentPlaying.imageUrl,
                   otherModifier = Modifier
                       .fillMaxSize()
                       .alpha(0.4f)
               )
               ImageLoader(
                   url = currentPlaying.imageUrl,
                   otherModifier = Modifier
                       .padding(20.dp)
                       .height(150.dp)
                       .width(150.dp)
               )
           }
       }
       PlayerButton(
           isShowTimeTrack = true,
           duration = trackProgress,
           durationTotal = trackProgressTotal,
           durationText = trackProgressText,
           isShuffle = isShuffle,
           durationTotalText = trackProgressTotalText,
           isPlaying = playerStatus.isGroupPlay(),
           onPlayPauseClick = onPlayPauseClick,
           onNextClick = onNextClick,
           onPreviousClick = onPreviousClick,
           onShuffleClick = onShuffleClick,
           onRefreshClick = onRefreshClick
       )
   }
}