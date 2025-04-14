package com.view.musicplayer.spotifyclone.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.view.musicplayer.spotifyclone.R
import com.view.musicplayer.spotifyclone.ext.roundedNumber
import com.view.musicplayer.spotifyclone.network.response.Track
import com.view.musicplayer.spotifyclone.screen.shared.EmptyView
import com.view.musicplayer.spotifyclone.screen.shared.ImageLoader
import com.view.musicplayer.spotifyclone.screen.shared.PlayerButton
import com.view.musicplayer.spotifyclone.ui.theme.Black60
import com.view.musicplayer.spotifyclone.ui.theme.Black80
import com.view.musicplayer.spotifyclone.ui.theme.Transparent
import com.view.musicplayer.spotifyclone.ui.theme.White80
import com.view.musicplayer.spotifyclone.viewmodel.HomePageViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomePageViewModel = koinViewModel(),
    isShowPlayerButton: Boolean,
    onClickMusic: (Track) -> Unit
) {
    val context: Context = LocalContext.current
    val recommendChart by viewModel.recommendationChart.observeAsState()
    val firstTrack by viewModel.firstTrack.observeAsState()
    val secondTrack by viewModel.secondTrack.observeAsState()
    val thirdTrack by viewModel.thirdTrack.observeAsState()
    val fourthTrack by viewModel.fourthTrack.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getRecommendation(context)
        viewModel.getSongRecommendation(context)
    }

    Column {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Black60)
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
                    items(firstTrack.orEmpty()) {  artist ->
                        MusicItemCard(
                            id = artist.id,
                            title = artist.title,
                            description = context.getString(R.string.total_listener, artist.totalListener.toInt().roundedNumber()),
                            imageUrl = artist.imageUrl
                        ) {
                            onClickMusic(artist)
                        }
                    }
                    if (firstTrack.isNullOrEmpty()) {
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
                            id = artist.id,
                            title = artist.title,
                            description = context.getString(R.string.total_listener, artist.totalListener.toInt().roundedNumber()),
                            imageUrl = artist.imageUrl
                        ) {
                            onClickMusic(artist)
                        }
                    }
                    if (secondTrack.isNullOrEmpty()) {
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
                    items(thirdTrack.orEmpty()) { artist ->
                        MusicItemCard(
                            id = artist.id,
                            title = artist.title,
                            description = context.getString(R.string.total_listener, artist.totalListener.toInt().roundedNumber()),
                            imageUrl = artist.imageUrl
                        ) {
                            onClickMusic(artist)
                        }
                    }
                    if (thirdTrack.isNullOrEmpty()) {
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
                    items(fourthTrack.orEmpty()) { artist ->
                        MusicItemCard(
                            id = artist.id,
                            title = artist.title,
                            description = context.getString(R.string.total_listener, artist.totalListener.toInt().roundedNumber()),
                            imageUrl = artist.imageUrl
                        ) {
                            onClickMusic(artist)
                        }
                    }
                    if (fourthTrack.isNullOrEmpty()) {
                        item {
                            EmptyView()
                        }
                    }
                }
            }
        }

        if (isShowPlayerButton) {
            PlayerButton()
        }
    }
}

@Composable
fun MusicItemCard(id: String,
                  title: String,
                  description: String,
                  imageUrl: String,
                  otherModifier: Modifier = Modifier,
                  onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { onClick() }
            .then(otherModifier),
        colors = CardDefaults.cardColors(containerColor = Black80),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            ImageLoader(imageUrl,
                otherModifier = Modifier
                    .size(64.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp)))
            itemCardTitleDescription(title = title, description = description)
        }
    }
}

@Composable
fun itemCardTitleDescription(title: String, description: String) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(start = 8.dp)
    ) {
        Text(
            text = title,
            color = White80,
            style = MaterialTheme.typography.bodySmall,
            fontSize = 14.sp
        )
        Text(
            text = description,
            color = White80,
            style = MaterialTheme.typography.bodySmall,
            fontSize = 12.sp
        )
    }
}