package com.view.musicplayer.spotifyclone.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.view.musicplayer.spotifyclone.R
import com.view.musicplayer.spotifyclone.constants.Constants
import com.view.musicplayer.spotifyclone.ext.roundedNumber
import com.view.musicplayer.spotifyclone.navigation.ScreenRoute
import com.view.musicplayer.spotifyclone.network.response.Genre
import com.view.musicplayer.spotifyclone.network.response.Track
import com.view.musicplayer.spotifyclone.room.model.FavoriteTrack
import com.view.musicplayer.spotifyclone.screen.shared.BackButton
import com.view.musicplayer.spotifyclone.screen.shared.EmptyView
import com.view.musicplayer.spotifyclone.screen.shared.ImageLoader
import com.view.musicplayer.spotifyclone.screen.shared.MusicItemCard
import com.view.musicplayer.spotifyclone.screen.shared.PlayerButton
import com.view.musicplayer.spotifyclone.screen.shared.loadIconToVector
import com.view.musicplayer.spotifyclone.screen.shared.showLoading
import com.view.musicplayer.spotifyclone.ui.theme.Black80
import com.view.musicplayer.spotifyclone.ui.theme.Gray50
import com.view.musicplayer.spotifyclone.ui.theme.SpotifyAccent40
import com.view.musicplayer.spotifyclone.ui.theme.SpotifyGreen40
import com.view.musicplayer.spotifyclone.ui.theme.Transparent
import com.view.musicplayer.spotifyclone.viewmodel.SearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel(),
    isShowPlayerButton: Boolean,
    currentPlaying: Track,
    navController: NavController,
    onClickMusic: (Track) -> Unit
) {
    val context: Context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val searchInteractSource = remember { MutableInteractionSource() }
    val genreData by viewModel.allGenre.observeAsState()
    val recommendTopTrack by viewModel.topTrack.observeAsState()
    val favoriteTrack by viewModel.favoriteTrack.observeAsState()

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
        viewModel.getAllFavoriteTrack(context)
        viewModel.getAllGenre(context)
        viewModel.getSongRecommendation(context)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Black80)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Transparent)
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isSearchActive) {
                    BackButton {
                        querySearch = ""
                        focusManager.clearFocus(force = true)
                        isSearchActive = false
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }
                SearchMusicBar(searchInteractSource, querySearch) {
                    querySearch = it
                }
            }
            when (isSearchActive) {
                true -> showQuerySearchPage(favoriteTrack ?: listOf(), currentPlaying, navController, viewModel, context, querySearch,
                    onClick = {
                        keyboardController?.hide()
                        onClickMusic(it)
                    },
                    onClickFavorite = {
                        viewModel.addOrRemoveFavorite(context, it)
                    })
                false -> showDefaultSearchPage(favoriteTrack ?: listOf(), currentPlaying, navController, recommendTopTrack, genreData ?: listOf(),
                    onClickMusic = {
                        keyboardController?.hide()
                        onClickMusic(it)
                    },
                    onClickGenre = {
                        keyboardController?.hide()
                        navController.navigate(ScreenRoute.AlbumDetail.route(it.name)) {
                            popUpTo(ScreenRoute.Search.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = false
                        }
                    },
                    onClickFavorite = {
                        viewModel.addOrRemoveFavorite(context, it)
                    })
            }
        }

        if (isShowPlayerButton) {
            PlayerButton()
        }
    }
}

@Composable
fun readHexColor(color: String): Color {
    return Color(android.graphics.Color.parseColor(color))
}

@Composable
fun ItemCardGenre(name: String, imageUrl: String, color: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .height(80.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
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
fun showDefaultSearchPage(favoriteTrack: List<Track>,
                          currentPlaying: Track,
                          navController: NavController,
                          recommendTopTrack: List<Track>?,
                          genreData: List<Genre>,
                          onClickMusic: (Track) -> Unit = {},
                          onClickGenre: (Genre) -> Unit = {},
                          onClickFavorite: (Track) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    )  {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(Transparent)
                .padding(top = 5.dp)
        ) {
            items(recommendTopTrack.orEmpty()) { track ->
                MusicItemCard(
                    navController = navController,
                    currentPlaying = currentPlaying,
                    track = track,
                    isFavorite = favoriteTrack.find { it.id == track.id } != null,
                    isShowGotoDetailButton = currentPlaying.id == track.id,
                    onClick = {
                        onClickMusic(track)
                    },
                    onAddFavorite = { track ->
                        onClickFavorite(track)
                    }
                )
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .background(Transparent)
                .padding(5.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            userScrollEnabled = true // Set to false if nested inside another scrollable
        ) {
            items(genreData) { item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    ItemCardGenre(item.name, item.imageUrl, item.color) {
                        onClickGenre(item)
                    }
                }
            }
        }
    }
}

@Composable
fun showQuerySearchPage(favoriteTrack: List<Track>,
                        currentPlaying: Track,
                        navController: NavController,
                        viewModel: SearchViewModel,
                        context: Context,
                        query: String,
                        onClick: (Track) -> Unit = {},
                        onClickFavorite: (Track) -> Unit = {}
) {
    val listArtistSearch by viewModel.listSearchArtist.observeAsState()
    val isLoading by viewModel.isLoadingEvent.observeAsState()

    LaunchedEffect(query) {
        if (query.isBlank()) {
            return@LaunchedEffect
        }

        delay(Constants.DELAY_SEARCH) // delay 2.5 second after typing
        viewModel.searchArtistOrSong(context, query)
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
        if (isLoading == true) {
            item {
                showLoading(35)
            }
        } else {
            if (listArtistSearch.isNullOrEmpty()) {
                item {
                    EmptyView(35)
                }
            } else {
                itemsIndexed(listArtistSearch.orEmpty()) { i, artist ->
                    MusicItemCard(
                        navController = navController,
                        currentPlaying = currentPlaying,
                        track = artist,
                        isFavorite = favoriteTrack.find { it.id == artist.id } != null,
                        isShowGotoDetailButton = currentPlaying.id == artist.id,
                        onClick = {
                            onClick(artist)
                        },
                        onAddFavorite = { track ->
                            onClickFavorite(track)
                        }
                    )
                }
            }
        }
    }
}