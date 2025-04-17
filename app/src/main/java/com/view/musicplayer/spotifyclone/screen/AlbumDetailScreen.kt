package com.view.musicplayer.spotifyclone.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.view.musicplayer.spotifyclone.R
import com.view.musicplayer.spotifyclone.ext.convertMillisToTime
import com.view.musicplayer.spotifyclone.ext.toSecond
import com.view.musicplayer.spotifyclone.network.response.Track
import com.view.musicplayer.spotifyclone.screen.shared.AddToFavoriteButton
import com.view.musicplayer.spotifyclone.screen.shared.AddToListButton
import com.view.musicplayer.spotifyclone.screen.shared.BackButton
import com.view.musicplayer.spotifyclone.screen.shared.EmptyView
import com.view.musicplayer.spotifyclone.screen.shared.GreenPlayButton
import com.view.musicplayer.spotifyclone.screen.shared.ImageLoader
import com.view.musicplayer.spotifyclone.screen.shared.MusicItemCard
import com.view.musicplayer.spotifyclone.screen.shared.PlayerButton
import com.view.musicplayer.spotifyclone.screen.shared.getDeviceScreenRatio
import com.view.musicplayer.spotifyclone.screen.shared.loadIconToVector
import com.view.musicplayer.spotifyclone.service.MusicService
import com.view.musicplayer.spotifyclone.ui.theme.Black100
import com.view.musicplayer.spotifyclone.ui.theme.Black60
import com.view.musicplayer.spotifyclone.ui.theme.Black80
import com.view.musicplayer.spotifyclone.ui.theme.Red500
import com.view.musicplayer.spotifyclone.ui.theme.SpotifyGreenGrey40
import com.view.musicplayer.spotifyclone.ui.theme.Transparent
import com.view.musicplayer.spotifyclone.ui.theme.White80
import com.view.musicplayer.spotifyclone.viewmodel.AlbumDetailViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlbumDetailScreen(
    viewModel: AlbumDetailViewModel = koinViewModel(),
    navController: NavController,
    isShowPlayerButton: Boolean,
    albumGenre: String,
    currentPlaying: Track,
    playerStatus: String,
    trackProgress: Long,
    trackProgressTotal: Long,
    trackProgressText: String,
    trackProgressTotalText: String,
    onClickMusic: (Track) -> Unit
) {
    val context = LocalContext.current
    val screenHeightDp = getDeviceScreenRatio().screenHeightDp
    val listMusicByGenre by viewModel.listArtistByGenre.observeAsState()
    val genreData by viewModel.genreData.observeAsState()
    val favoriteTrack by viewModel.favoriteTrack.observeAsState()
    val isThisPlaylistNameExist by viewModel.isThisPlaylistExist.observeAsState()
    val listPlaylist by viewModel.listPlaylist.observeAsState()

    val listState = rememberLazyListState()
    var isShowToolbar by remember { mutableStateOf(false) }
    var allListIncludedInFavorite by remember { mutableStateOf(true) }
    var targetedPx = 0
    var currentScrollYPosition = 0
    var visibleItemIndex: Int

    BackHandler {
        navController.popBackStack()
    }

    LaunchedEffect(Unit) {
        viewModel.getAllPlaylist(context)
        viewModel.getPlaylistByName(context, albumGenre)
        viewModel.getAllFavoriteTrack(context)
        viewModel.getAllArtistByGenre(context, albumGenre)
        viewModel.getGenreByName(context, albumGenre)
    }

    // Observe scroll to detect showing toolbar and green play button on top
    LaunchedEffect(listState) {
        snapshotFlow {
            visibleItemIndex = listState.firstVisibleItemIndex
            if (visibleItemIndex > 1) { // if item index > 1, add first item height with current Y position
                currentScrollYPosition += listState.firstVisibleItemScrollOffset + listState.firstVisibleItemIndex
            } else {
                currentScrollYPosition = listState.firstVisibleItemScrollOffset + listState.firstVisibleItemIndex
            }
            currentScrollYPosition
        }
        .distinctUntilChanged()
        .collectLatest { scrollY ->
            isShowToolbar = scrollY >= (targetedPx - 190)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(Black80)
        ) {
            stickyHeader {
                if (isShowToolbar) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Row(
                            modifier = Modifier
                                .background(Black100)
                                .fillMaxWidth()
                                .height(50.dp)
                                .padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BackButton {
                                navController.popBackStack()
                            }
                            Spacer(modifier = Modifier.width(20.dp))
                            Text(
                                text = genreData?.name.orEmpty(),
                                color = White80,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Box(
                            modifier = Modifier
                                .padding(20.dp)
                                .background(Black100, shape = RoundedCornerShape(100.dp))
                        ) {
                            GreenPlayButton {
                                val firstTrack = listMusicByGenre?.firstOrNull()
                                firstTrack?.let { onClickMusic(it) }
                            }
                        }
                    }
                }
            }
            item {
                Box(
                    modifier = Modifier
                        .padding(bottom = 40.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .onGloballyPositioned {
                            targetedPx = it.size.height
                        },
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Column(
                        modifier = Modifier
                            .padding(bottom = 55.dp)
                            .fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(((screenHeightDp) / 3).dp),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            ImageLoader(
                                url = genreData?.imageUrl.orEmpty(),
                                otherModifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Black60,
                                                Transparent
                                            )
                                        )
                                    )
                            )
                            Text(
                                text = genreData?.name.orEmpty(),
                                color = White80,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomStart)
                                    .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = genreData?.description.orEmpty(),
                            color = White80,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp, top = 5.dp),
                            fontSize = 14.sp
                        )
                        Row(
                            modifier = Modifier
                                .padding(start = 20.dp, end = 20.dp, top = 5.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                imageVector = loadIconToVector(icon = R.drawable.is_spotify_green),
                                contentDescription = "Icon spotify",
                                modifier = Modifier
                                    .padding(end = 5.dp)
                                    .height(20.dp)
                                    .wrapContentWidth()
                            )
                            Text(
                                text = context.getString(R.string.app_name),
                                color = White80,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Light
                            )
                        }
                        Row(
                            modifier = Modifier
                                .padding(start = 20.dp, end = 20.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = context.getString(R.string.total_listener, listMusicByGenre?.sumOf { it.totalListener }.toString()),
                                color = White80,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Light
                            )
                            Image(
                                imageVector = loadIconToVector(icon = R.drawable.ic_round_music),
                                contentDescription = "Icon dot",
                                colorFilter = ColorFilter.tint(White80),
                                modifier = Modifier
                                    .padding(end = 5.dp, start = 5.dp)
                                    .height(4.dp)
                                    .width(4.dp)
                            )
                            Text(
                                text = listMusicByGenre?.sumOf { it.duration.toSecond() }?.convertMillisToTime().orEmpty(),
                                color = White80,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Light
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        allListIncludedInFavorite = listMusicByGenre?.all { track -> favoriteTrack?.contains(track) == true } == true

                        AddToFavoriteButton(
                            color = if (allListIncludedInFavorite) Red500 else White80
                        ) {
                            if (allListIncludedInFavorite) {
                                viewModel.removeAllTrackFromFavorite(context, listMusicByGenre ?: arrayListOf())
                            } else {
                                viewModel.addAllTrackToFavorite(context, listMusicByGenre ?: arrayListOf())
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))
                        AddToListButton(
                            color = if (isThisPlaylistNameExist == true) SpotifyGreenGrey40 else White80,
                            title = if (isThisPlaylistNameExist == true) R.string.added_to_playlist else null
                        ) {
                            if (isThisPlaylistNameExist == true) return@AddToListButton
                            viewModel.createPlaylist(context, genreData?.name.orEmpty(), genreData?.imageUrl.orEmpty(), listMusicByGenre ?: arrayListOf())
                        }
                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f))
                        GreenPlayButton {
                            val firstTrack = listMusicByGenre?.firstOrNull()
                            firstTrack?.let { onClickMusic(it) }
                        }
                    }
                }
            }
            if (listMusicByGenre.isNullOrEmpty()) {
                item {
                    EmptyView(35)
                }
            } else {
                items(listMusicByGenre.orEmpty()) { artist ->
                    Box(
                        modifier = Modifier
                            .padding(start = 15.dp, end = 15.dp, top = 5.dp)
                    ) {
                        MusicItemCard(
                            navController = navController,
                            currentPlaying = currentPlaying,
                            track = artist,
                            listPlaylist = listPlaylist,
                            isFavorite = favoriteTrack?.find { it.id == artist.id } != null,
                            isShowGotoDetailButton = currentPlaying.id == artist.id,
                            onClick = {
                                onClickMusic(artist)
                            },
                            onAddFavorite = { track ->
                                viewModel.addOrRemoveFavorite(context, track)
                            },
                            onAddPlaylist = { track, playlist ->
                                viewModel.addTrackToPlaylist(context, track, playlist)
                            }
                        )
                    }
                }
            }
            item {
                if (isShowPlayerButton) {
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                    )
                } else {
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                    )
                }
            }
        }
        if (isShowPlayerButton) {
            PlayerButton(
                duration = trackProgress,
                durationTotal = trackProgressTotal,
                durationText = trackProgressText,
                durationTotalText = trackProgressTotalText,
                isPlaying = playerStatus == MusicService.PlayerStatus.PLAY.status
            )
        }
    }
}