package com.view.musicplayer.spotifyclone.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.view.musicplayer.spotifyclone.network.response.Track
import com.view.musicplayer.spotifyclone.screen.shared.BackButton
import com.view.musicplayer.spotifyclone.ui.theme.Black80
import com.view.musicplayer.spotifyclone.ui.theme.White80

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongDetailScreen(
    navController: NavController,
    isShowPlayerButton: Boolean,
    musicTitle: String,
    onClickMusic: (Track) -> Unit
) {
   Column(
       modifier = Modifier
           .fillMaxSize()
           .background(Black80)
   ) {
       Row(
           modifier = Modifier
               .fillMaxWidth()
               .height(60.dp)
               .padding(horizontal = 10.dp),
           verticalAlignment = Alignment.CenterVertically
       ) {
           BackButton {
             navController.popBackStack()
           }
           Spacer(modifier = Modifier.width(10.dp))
           Text(
               text = "aaaa",
               color = White80,
               fontSize = 16.sp,
               textAlign = TextAlign.Center,
               modifier = Modifier.fillMaxWidth().weight(1f)
           )
           Spacer(modifier = Modifier.width(40.dp))
       }
   }
}