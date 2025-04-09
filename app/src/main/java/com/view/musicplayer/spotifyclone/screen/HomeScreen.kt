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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.view.musicplayer.spotifyclone.R
import com.view.musicplayer.spotifyclone.ui.theme.Black60
import com.view.musicplayer.spotifyclone.ui.theme.Black80
import com.view.musicplayer.spotifyclone.ui.theme.White80
import com.view.musicplayer.spotifyclone.viewmodel.HomepageViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomepageViewModel = koinViewModel()
) {
    val context: Context = LocalContext.current
    val homepageData by viewModel.homepageData.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getHomePageData(context)
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(Black60)
            .padding(16.dp)
    ) {
        items(homepageData.orEmpty()) { item ->
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyMedium.copy(color = White80),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .background(Black60)
                    .padding(16.dp)
            ) {
                items(item.tracks.orEmpty()) { track ->
                    MusicItemCard(
                        id = track.id,
                        title = track.title,
                        description = "by ${track.artist}",
                        imageUrl = track.albumArt
                    )
                }
            }
        }
    }
}

@Composable
fun MusicItemCard(id: String, title: String, description: String, imageUrl: String, onClick: (String) -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { onClick(id) },
        colors = CardDefaults.cardColors(containerColor = Black80),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            ImageLoader(imageUrl,
                otherModifier = Modifier.size(64.dp).padding(8.dp).clip(RoundedCornerShape(8.dp)))
            itemCardTitleDescription(title = title, description = description)
        }
    }
}

@Composable
fun ImageLoader(url: String, otherModifier: Modifier = Modifier) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        contentScale = ContentScale.Crop,
        contentDescription = url,
        modifier = Modifier.then(otherModifier),
        placeholder = painterResource(R.drawable.is_spotify_green),
        error = painterResource(R.drawable.is_spotify_green)
    )
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
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = description,
            color = White80,
            style = MaterialTheme.typography.bodySmall
        )
    }
}