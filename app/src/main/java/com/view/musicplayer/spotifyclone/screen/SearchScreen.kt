package com.view.musicplayer.spotifyclone.screen

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.remember
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.view.musicplayer.spotifyclone.EmptyView
import com.view.musicplayer.spotifyclone.R
import com.view.musicplayer.spotifyclone.ext.roundedNumber
import com.view.musicplayer.spotifyclone.loadIconToVector
import com.view.musicplayer.spotifyclone.network.response.AllGenre
import com.view.musicplayer.spotifyclone.network.response.TopChartTracks
import com.view.musicplayer.spotifyclone.ui.theme.Black80
import com.view.musicplayer.spotifyclone.ui.theme.Gray50
import com.view.musicplayer.spotifyclone.ui.theme.SpotifyAccent40
import com.view.musicplayer.spotifyclone.ui.theme.SpotifyGreen40
import com.view.musicplayer.spotifyclone.ui.theme.Transparent
import com.view.musicplayer.spotifyclone.ui.theme.White80
import com.view.musicplayer.spotifyclone.viewmodel.SearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel()
) {
    val context: Context = LocalContext.current

    val searchInteractSource = remember { MutableInteractionSource() }
    val genreData by viewModel.allGenre.observeAsState()
    val recommendTopTrack by viewModel.topTrack.observeAsState()

    val focusManager = LocalFocusManager.current
    var isSearchActive by remember { mutableStateOf(false) }
    var querySearch by rememberSaveable { mutableStateOf("") }

    // get search textview focus state
    LaunchedEffect(searchInteractSource) {
        searchInteractSource.interactions.collectLatest { interaction ->
            when (interaction) {
                is FocusInteraction.Focus -> isSearchActive = true
                is FocusInteraction.Unfocus -> isSearchActive = false
            }
        }
    }

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
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSearchActive) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "",
                    tint = White80,
                    modifier = Modifier.clickable {
                        querySearch = ""
                        focusManager.clearFocus(force = true)
                        isSearchActive = false
                    }
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
            SearchMusicBar(searchInteractSource, querySearch) {
                querySearch = it
            }
        }
        when (isSearchActive) {
            true -> showQuerySearchPage(viewModel, context, querySearch)
            false -> showDefaultSearchPage(recommendTopTrack, genreData)
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
fun SearchMusicBar(interactSource: MutableInteractionSource, musicSearched: String, onTypingChange: (String) -> Unit) {
    OutlinedTextField(
        interactionSource = interactSource,
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

@Composable
fun showDefaultSearchPage(recommendTopTrack: TopChartTracks?, genreData: AllGenre?) {
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

@Composable
fun showQuerySearchPage(viewModel: SearchViewModel, context: Context, query: String) {
    val currentPage by remember { mutableStateOf(1) }
    val listArtistSearch by viewModel.listSearchArtist.observeAsState()

    LaunchedEffect(query) {
        if (query.isBlank()) {
            return@LaunchedEffect
        }

        delay(2500) // delay 2.5 second after typing
        viewModel.searchArtist(context, query, currentPage)
    }

    if (query.isBlank()) {
        listArtistSearch?.clear()
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(Transparent)
            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
    ) {
        if (listArtistSearch.isNullOrEmpty()) {
            item {
                EmptyView(35)
            }
        } else {
            itemsIndexed(listArtistSearch.orEmpty()) { i, artist ->
                MusicItemCard(
                    id = artist.mbid,
                    title = artist.name,
                    description = "by ${artist.name} (${context.getString(R.string.total_listener, artist.listeners.toInt().roundedNumber())})",
                    imageUrl = artist.image.first().text
                )
            }
        }
    }
}