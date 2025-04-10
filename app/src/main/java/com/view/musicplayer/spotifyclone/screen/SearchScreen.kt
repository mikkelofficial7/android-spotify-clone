package com.view.musicplayer.spotifyclone.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.view.musicplayer.spotifyclone.R
import com.view.musicplayer.spotifyclone.loadIconToVector
import com.view.musicplayer.spotifyclone.ui.theme.Black80
import com.view.musicplayer.spotifyclone.ui.theme.Gray50
import com.view.musicplayer.spotifyclone.ui.theme.SpotifyAccent40
import com.view.musicplayer.spotifyclone.ui.theme.SpotifyGreen40
import com.view.musicplayer.spotifyclone.ui.theme.Transparent
import com.view.musicplayer.spotifyclone.viewmodel.SearchViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel()
) {
    val context: Context = LocalContext.current

    val genreData by viewModel.allGenre.observeAsState()
    val recommendTopTrack by viewModel.topTrack.observeAsState()

    var musicSearched by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getAllGenre(context)
        viewModel.getTopChartTrack(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black80)
            .padding(16.dp)
    ) {
        SearchMusicBar(musicSearched) {
            musicSearched = it
        }
        Spacer(modifier = Modifier.height(5.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 5.dp, start = 5.dp, end = 5.dp, bottom = 5.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        )  {
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Transparent)
                ) {
                    items(recommendTopTrack?.tracks?.track.orEmpty()) { track ->
                        MusicItemCard(
                            id = track.mbid,
                            title = track.name,
                            description = "by ${track.artist.name}",
                            imageUrl = track.image.first().text
                        )
                    }
                }
            }
            item {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .height(550.dp)
                        .background(Transparent)
                        .padding(top = 8.dp),
                    userScrollEnabled = false,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(genreData?.genreSection.orEmpty()) { genre ->
                        ItemCardGenre(genre.name, genre.imageUrl, genre.color)
                    }
                }
            }
        }
    }
}

@Composable
fun readHexColor(color: String): Color {
    return Color(android.graphics.Color.parseColor(color))
}

@Composable
fun ItemCardGenre(name: String, imageUrl: String, color: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(readHexColor(color))
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            )
            ImageLoader(
                url = imageUrl,
                otherModifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomEnd)
                    .height(120.dp)
                    .graphicsLayer(alpha = 0.99f)
                    .drawWithContent {
                        drawContent()
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(Transparent, Black80),
                                startY = 0f,
                                endY = size.height
                            ),
                            blendMode = BlendMode.DstIn
                        )
                    }
            )
        }
    }
}

@Composable
fun SearchMusicBar(musicSearched: String, onTypingChange: (String) -> Unit) {
    OutlinedTextField(
        value = musicSearched,
        onValueChange = { onTypingChange(it) },
        placeholder = {
            Text(LocalContext.current.getString(R.string.search_song), color = Gray50)
        },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Black80,
            unfocusedContainerColor = Black80,
            focusedBorderColor = Transparent,
            unfocusedBorderColor = Transparent,
            focusedTextColor = SpotifyGreen40,
            unfocusedTextColor = SpotifyGreen40,
            cursorColor = SpotifyAccent40
        ),
        leadingIcon = {
            Icon(
                loadIconToVector(R.drawable.ic_search),
                contentDescription = null,
                tint = Gray50,
                modifier = Modifier
                    .height(30.dp)
                    .width(30.dp)
            )
        }
    )
}