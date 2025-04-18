package com.view.musicplayer.spotifyclone.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.view.musicplayer.spotifyclone.R
import com.view.musicplayer.spotifyclone.navigation.routeToPlaylistDetail
import com.view.musicplayer.spotifyclone.network.response.Track
import com.view.musicplayer.spotifyclone.room.model.FavoriteTrack
import com.view.musicplayer.spotifyclone.room.model.PlaylistModel
import com.view.musicplayer.spotifyclone.screen.shared.EmptyView
import com.view.musicplayer.spotifyclone.screen.shared.ImageLoader
import com.view.musicplayer.spotifyclone.screen.shared.ItemTrackLayout
import com.view.musicplayer.spotifyclone.screen.shared.MusicItemCard
import com.view.musicplayer.spotifyclone.screen.shared.PlayerButton
import com.view.musicplayer.spotifyclone.service.MusicService
import com.view.musicplayer.spotifyclone.ui.theme.Black80
import com.view.musicplayer.spotifyclone.ui.theme.SpotifyGreen80
import com.view.musicplayer.spotifyclone.ui.theme.Transparent
import com.view.musicplayer.spotifyclone.ui.theme.White80
import com.view.musicplayer.spotifyclone.viewmodel.ProfileViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    navController: NavController,
    isShowPlayerButton: Boolean,
    isShuffle: Boolean,
    currentPlaying: Track,
    playerStatus: String,
    trackProgress: Long,
    trackProgressTotal: Long,
    trackProgressText: String,
    trackProgressTotalText: String,
    onClickMusic: (Track) -> Unit,
    onPlayPauseClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    onPreviousClick: () -> Unit = {},
    onShuffleClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val tabTitles = listOf(context.getString(R.string.your_favorite), context.getString(R.string.your_playlists))
    var selectedTabIndex by remember { mutableStateOf(0) }

    val listFavorite by viewModel.listFavTrack.observeAsState()
    val listPlaylist by viewModel.listPlaylist.observeAsState()
    var isShowAddPlaylistDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getAllFavoriteTrack(context)
        viewModel.getAllPlaylist(context)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black80),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImageLoader(
                url = "",
                otherModifier = Modifier
                    .height(80.dp)
                    .width(80.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "{{name}}", color = White80, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = "{{email}}", color = White80, fontSize = 14.sp)
            Text(text = "{{age}}", color = White80, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Transparent,
                    contentColor = SpotifyGreen80,
                    divider = {},
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = SpotifyGreen80
                        )
                    }
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = {
                                Text(
                                    text = title,
                                    color = if (selectedTabIndex == index) SpotifyGreen80 else White80
                                )
                            }
                        )
                    }
                }
                when (selectedTabIndex) {
                    0 -> FavoriteListContent(
                        listFavorite ?: listOf(),
                        navController,
                        currentPlaying,
                        onClickMusic = onClickMusic,
                        onClickFavorit = {
                            viewModel.addOrRemoveFavorite(context, it)
                        }
                    )
                    1 -> PlaylistListContent(
                        listPlaylist ?: listOf(),
                        navController,
                        onAddNewPlaylist = {
                            isShowAddPlaylistDialog = true
                        },
                        onDeletePlaylist = {
                            viewModel.removePlaylist(context, it)
                        },
                        onNavigatePlaylistDetail = {
                            navController.routeToPlaylistDetail(it.toString())
                        }
                    )
                }
            }
            if (isShowPlayerButton) {
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp))
            }
        }
        if (isShowPlayerButton) {
            PlayerButton(
                duration = trackProgress,
                durationTotal = trackProgressTotal,
                durationText = trackProgressText,
                durationTotalText = trackProgressTotalText,
                isShuffle = isShuffle,
                isPlaying = playerStatus == MusicService.PlayerStatus.PLAY.status,
                onPlayPauseClick = onPlayPauseClick,
                onNextClick = onNextClick,
                onPreviousClick = onPreviousClick,
                onShuffleClick = onShuffleClick
            )
        }
        if (isShowAddPlaylistDialog) {
            ShowDialogCreatePlaylist(
                onDismiss = {
                    isShowAddPlaylistDialog = false
                },
                onConfirm = { name, icon ->
                    isShowAddPlaylistDialog = false
                    viewModel.addPlaylist(context, name, icon)
                }
            )
        }
    }
}

@Composable
fun FavoriteListContent(
    listFavorite: List<FavoriteTrack>,
    navController: NavController,
    currentPlaying: Track,
    onClickMusic: (Track) -> Unit,
    onClickFavorit: (Track) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .background(Transparent)
            .fillMaxSize(),
    ) {
        if (listFavorite.isEmpty()) {
            item {
                EmptyView(40)
            }
        } else {
            items(listFavorite) { favTrack ->
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp))
                MusicItemCard(
                    navController = navController,
                    currentPlaying = currentPlaying,
                    track = favTrack.toTrack,
                    isFavorite = true,
                    isShowGotoDetailButton = currentPlaying.id == favTrack.id,
                    onClick = {
                        onClickMusic(favTrack.toTrack)
                    },
                    onAddFavorite = {
                        onClickFavorit(favTrack.toTrack)
                    }
                )
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp))
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaylistListContent(
    listPlaylist: List<PlaylistModel>,
    navController: NavController,
    onAddNewPlaylist: () -> Unit = {},
    onDeletePlaylist: (PlaylistModel) -> Unit = {},
    onNavigatePlaylistDetail: (Int) -> Unit = {}
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .background(Transparent)
            .fillMaxSize(),
    ) {
        LazyColumn {
            stickyHeader {
                ItemTrackLayout(
                    iconDefault = R.drawable.ic_add_no_round,
                    title = context.getString(R.string.create_playlist),
                    onClick = onAddNewPlaylist
               )
            }
            items(listPlaylist) {
                ItemTrackLayout(
                    icon = it.playlistIcon,
                    isShowDelete = true,
                    title = it.playlistName,
                    message = context.getString(R.string.song_in_playlist, it.playlistTrack.size.toString()),
                    onClick = {
                        onNavigatePlaylistDetail(it.idPk)
                    },
                    onDelete = {
                        onDeletePlaylist(it)
                    }
                )
            }
        }
    }
}

@Composable
fun ShowDialogCreatePlaylist(onDismiss: () -> Unit, onConfirm: (String, Int) -> Unit) {
    var playlistName by remember { mutableStateOf("") }
    var playlistIcon by remember { mutableStateOf(R.drawable.pattern1) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = LocalContext.current.getString(R.string.create_playlist))
        },
        text = {
            Column {
                Text(LocalContext.current.getString(R.string.background_playlist))
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    item {
                        Box(modifier = Modifier
                            .padding(10.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (playlistIcon == R.drawable.pattern1) SpotifyGreen80 else Transparent)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.pattern1),
                                contentDescription = null,
                                modifier = Modifier
                                    .height(50.dp)
                                    .width(50.dp)
                                    .clickable { playlistIcon = R.drawable.pattern1 }
                            )
                        }
                    }
                    item {
                        Box(modifier = Modifier
                            .padding(10.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (playlistIcon == R.drawable.pattern2) SpotifyGreen80 else Transparent)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.pattern2),
                                contentDescription = null,
                                modifier = Modifier
                                    .height(50.dp)
                                    .width(50.dp)
                                    .clickable { playlistIcon = R.drawable.pattern2 }
                            )
                        }
                    }
                    item {
                        Box(modifier = Modifier
                            .padding(10.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (playlistIcon == R.drawable.pattern3) SpotifyGreen80 else Transparent)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.pattern3),
                                contentDescription = null,
                                modifier = Modifier
                                    .height(50.dp)
                                    .width(50.dp)
                                    .clickable { playlistIcon = R.drawable.pattern3 }
                            )
                        }
                    }
                    item {
                        Box(modifier = Modifier
                            .padding(10.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (playlistIcon == R.drawable.pattern4) SpotifyGreen80 else Transparent)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.pattern4),
                                contentDescription = null,
                                modifier = Modifier
                                    .height(50.dp)
                                    .width(50.dp)
                                    .clickable { playlistIcon = R.drawable.pattern4 }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
                Text(LocalContext.current.getString(R.string.input_playlist))
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = playlistName,
                    onValueChange = { playlistName = it },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Transparent,
                        unfocusedIndicatorColor = Transparent,
                        disabledIndicatorColor = Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (playlistName.isEmpty()) return@TextButton
                onConfirm(playlistName, playlistIcon)
            }) {
                Text(LocalContext.current.getString(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(LocalContext.current.getString(R.string.cancel))
            }
        }
    )
}