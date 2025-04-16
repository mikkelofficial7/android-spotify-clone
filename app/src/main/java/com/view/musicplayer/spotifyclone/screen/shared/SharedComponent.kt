package com.view.musicplayer.spotifyclone.screen.shared

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.view.musicplayer.spotifyclone.R
import com.view.musicplayer.spotifyclone.ext.formatTimeTrackRunning
import com.view.musicplayer.spotifyclone.ext.roundedNumber
import com.view.musicplayer.spotifyclone.navigation.routeToMusicDetail
import com.view.musicplayer.spotifyclone.network.response.Track
import com.view.musicplayer.spotifyclone.room.model.PlaylistModel
import com.view.musicplayer.spotifyclone.ui.theme.Black100
import com.view.musicplayer.spotifyclone.ui.theme.Black60
import com.view.musicplayer.spotifyclone.ui.theme.Black80
import com.view.musicplayer.spotifyclone.ui.theme.Blue500
import com.view.musicplayer.spotifyclone.ui.theme.Red500
import com.view.musicplayer.spotifyclone.ui.theme.SpotifyAccent80
import com.view.musicplayer.spotifyclone.ui.theme.SpotifyGreen80
import com.view.musicplayer.spotifyclone.ui.theme.SpotifyGreenGrey40
import com.view.musicplayer.spotifyclone.ui.theme.SpotifyGreenGrey80
import com.view.musicplayer.spotifyclone.ui.theme.Transparent
import com.view.musicplayer.spotifyclone.ui.theme.White80

@Composable
fun loadIconToVector(@DrawableRes icon: Int): ImageVector {
    return ImageVector.vectorResource(id = icon)
}

@Composable
fun getDeviceScreenRatio(): Configuration {
    return LocalConfiguration.current
}

@Composable
fun EmptyView(padding: Int = 5) {
    Box(
        modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "~ No Data Available ~",
            color = White80,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = padding.dp)
        )
    }
}

@Composable
fun showLoading(padding: Int = 5) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = padding.dp, bottom = padding.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = SpotifyAccent80)
    }
}

@Composable
fun ImageLoader(url: String, otherModifier: Modifier = Modifier) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .size(200, 200)
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
fun BackButton(onBack: () -> Unit) {
    Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
        contentDescription = "",
        tint = White80,
        modifier = Modifier.clickable { onBack() }
    )
}

@Composable
fun AddToFavoriteButton(size: Int = 30, color: Color = White80, onClick: () -> Unit = {}) {
    Image(
        imageVector = loadIconToVector(icon = R.drawable.ic_favorite),
        contentDescription = "Icon favorite",
        colorFilter = ColorFilter.tint(color),
        modifier = Modifier
            .height(size.dp)
            .width(size.dp)
            .clickable { onClick() }
    )
}

@Composable
fun ShowDetailButton(size: Int = 30, color: Color = White80, onClick: () -> Unit = {}) {
    Image(
        imageVector = loadIconToVector(icon = R.drawable.ic_hamburger),
        contentDescription = "Icon go to detail",
        colorFilter = ColorFilter.tint(color),
        modifier = Modifier
            .height(size.dp)
            .width(size.dp)
            .clickable { onClick() }
    )
}

@Composable
fun AddToListButton(size: Int = 30,
                    color: Color = White80,
                    @StringRes title: Int? = null,
                    onClick: () -> Unit = {}) {
    Row(
       verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = loadIconToVector(icon = R.drawable.ic_add),
            contentDescription = "Icon add",
            colorFilter = ColorFilter.tint(color),
            modifier = Modifier
                .height(size.dp)
                .width(size.dp)
                .clickable { onClick() }
        )
        title?.let {
            Text(
                text = LocalContext.current.getString(title),
                color = SpotifyGreenGrey40,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun RemoveFromListButton(size: Int = 30,
                    color: Color = White80,
                    @StringRes title: Int? = null,
                    onClick: () -> Unit = {}) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = loadIconToVector(icon = R.drawable.ic_remove),
            contentDescription = "Icon add",
            colorFilter = ColorFilter.tint(color),
            modifier = Modifier
                .height(size.dp)
                .width(size.dp)
                .clickable { onClick() }
        )
        title?.let {
            Text(
                text = LocalContext.current.getString(title),
                color = SpotifyGreenGrey40,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun ItemTrackLayout(
    @DrawableRes iconDefault: Int? = null,
    icon: String? = null,
    title: String = "",
    message: String = "",
    textColor: Color = White80,
    isShowDelete: Boolean = false,
    onClick: () -> Unit,
    onDelete: () -> Unit = {}
) {
    val iconSize = 50

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp, top = 10.dp, start = 16.dp, end = 16.dp)
            .clickable { onClick() }
    ) {
        iconDefault?.let {
            Image(
                imageVector = loadIconToVector(icon = it),
                contentDescription = null,
                modifier = Modifier
                    .height(iconSize.dp)
                    .width(iconSize.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Black60)
            )
        }
        icon?.let {
            ImageLoader(
                url = it,
                otherModifier = Modifier
                    .height(iconSize.dp)
                    .width(iconSize.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Black60)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                color = textColor,
                fontSize = 16.sp
            )
            if (message.isNotBlank()) {
                Text(
                    text = message,
                    color = textColor,
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic
                )
            }
        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
        )
        if(isShowDelete) {
            Image(
                imageVector = loadIconToVector(icon = R.drawable.ic_delete),
                contentDescription = null,
                alignment = Alignment.CenterEnd,
                colorFilter = ColorFilter.tint(White80),
                modifier = Modifier
                    .height(20.dp)
                    .width(20.dp)
                    .clickable { onDelete() }

            )
        }
    }
}

@Composable
fun PlayerButton(
    currentPosition: Float = 00f,
    totalDuration: Float = 0f,
    isPlaying: Boolean = false,
    isShuffle: Boolean = false,
    isShowTimeTrack: Boolean = false,
    onSliderChange: (Float) -> Unit = {},
    onPlayPauseClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    onPreviousClick: () -> Unit = {},
    onShuffleClick: () -> Unit = {},
    onRefreshClick: () -> Unit = {}
) {
    val iconSize = 30
    val bigIconSize = 50

    Column {
        if (isShowTimeTrack) {
            Row {
                Text(
                    text = currentPosition.formatTimeTrackRunning(),
                    color = White80,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                        .fillMaxWidth()
                        .weight(1f)
                )
                Text(
                    text = totalDuration.formatTimeTrackRunning(),
                    color = White80,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
        CustomSeekBar(
            currentPosition = currentPosition,
            totalDuration = totalDuration,
            onSeekBarChange = {
               onSliderChange(it)
            }
        )
        Box(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Black100)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onShuffleClick, colors = IconButtonDefaults.iconButtonColors(
                    contentColor = White80
                )) {
                    Icon(
                        imageVector = if (isShuffle) loadIconToVector(icon = R.drawable.ic_shuffle_on) else loadIconToVector(icon = R.drawable.ic_shuffle),
                        contentDescription = "Shuffle",
                        modifier = Modifier
                            .height(iconSize.dp)
                            .width(iconSize.dp)
                    )
                }
                IconButton(onClick = onPreviousClick, colors = IconButtonDefaults.iconButtonColors(
                    contentColor = White80
                )) {
                    Icon(
                        imageVector = loadIconToVector(icon = R.drawable.ic_previous),
                        contentDescription = "Previous",
                        modifier = Modifier
                            .height(bigIconSize.dp)
                            .width(bigIconSize.dp)
                    )
                }
                IconButton(onClick = onPlayPauseClick, colors = IconButtonDefaults.iconButtonColors(
                    contentColor = White80
                )) {
                    Icon(
                        imageVector = if (isPlaying) loadIconToVector(icon = R.drawable.ic_pause) else loadIconToVector(icon = R.drawable.ic_play),
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        modifier = Modifier
                            .height(bigIconSize.dp)
                            .width(bigIconSize.dp)
                    )
                }
                IconButton(onClick = onNextClick, colors = IconButtonDefaults.iconButtonColors(
                    contentColor = White80
                )) {
                    Icon(
                        imageVector = loadIconToVector(icon = R.drawable.ic_next),
                        contentDescription = "Next",
                        modifier = Modifier
                            .height(bigIconSize.dp)
                            .width(bigIconSize.dp)
                    )
                }
                IconButton(onClick = onRefreshClick, colors = IconButtonDefaults.iconButtonColors(
                    contentColor = White80
                )) {
                    Icon(
                        imageVector = loadIconToVector(icon = R.drawable.ic_repeat),
                        contentDescription = "Refresh",
                        modifier = Modifier
                            .height(iconSize.dp)
                            .width(iconSize.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CustomSeekBar(
    currentPosition: Float = 0f,
    totalDuration: Float = 0f,
    layoutHeight: Int = 14,
    seekbarHeight: Int = 5,
    seekBarActiveColor: Color = SpotifyGreen80,
    seekBarBackgroundColor: Color = White80,
    onSeekBarChange: (Float) -> Unit = {}
) {
    onSeekBarChange(currentPosition)
    val aaa = currentPosition / totalDuration
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Black100)
            .height(layoutHeight.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(modifier = Modifier
            .padding(horizontal = 5.dp)
            .background(seekBarBackgroundColor)
            .height(seekbarHeight.dp)
            .fillMaxWidth())
        Box(modifier = Modifier
            .padding(horizontal = 5.dp)
            .background(seekBarActiveColor)
            .height(seekbarHeight.dp)
            .fillMaxWidth(aaa)
        )
    }
}


@Composable
fun GreenPlayButton(padding: Int = 0, onClick: () -> Unit) {
    Image(
        imageVector = loadIconToVector(icon = R.drawable.ic_play),
        contentDescription = "Icon play",
        alignment = Alignment.CenterEnd,
        colorFilter = ColorFilter.tint(SpotifyGreenGrey80),
        modifier = Modifier
            .padding(padding.dp)
            .height(70.dp)
            .width(70.dp)
            .clickable {
                onClick()
            }
    )
}

@Composable
fun MusicItemCard(
    navController: NavController,
    currentPlaying: Track,
    track: Track,
    listPlaylist: List<PlaylistModel>? = arrayListOf(),
    isShowRemoveFromPlaylist: Boolean = false,
    isShowGotoDetailButton: Boolean = false,
    isFavorite: Boolean = false,
    onClick: () -> Unit = {},
    onAddFavorite: (Track) -> Unit = {},
    onAddPlaylist:(Track, PlaylistModel?) -> Unit = { track, playlist -> },
    onRemoveTrackPlaylist: (Track) -> Unit = {}
) {
    var isOptionMenuExpand by remember { mutableStateOf(false) }
    var isShowSelectorTrack by remember { mutableStateOf(false) }

    val listPlaylistFiltered = listPlaylist?.filter { playlist ->
        playlist.playlistTrack.all { it.id != track.id }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Black80),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Box {
                Box {
                    ImageLoader(track.imageUrl,
                        otherModifier = Modifier
                            .size(64.dp)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(8.dp)))

                    if (isFavorite) {
                        Icon(
                            imageVector = loadIconToVector(icon = R.drawable.ic_favorite),
                            contentDescription = null,
                            tint = Red500,
                            modifier = Modifier
                                .height(25.dp)
                                .width(25.dp)
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = track.title,
                    color = if (currentPlaying.id == track.id) SpotifyGreenGrey80 else White80,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 14.sp
                )
                Text(
                    text = "by ${track.artist} (${LocalContext.current.getString(R.string.total_listener, track.totalListener.toInt().roundedNumber())})",
                    color = if (currentPlaying.id == track.id) SpotifyGreenGrey80 else White80,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .weight(1f))
            Column {
                Image(
                    imageVector = loadIconToVector(icon = R.drawable.ic_three_dots),
                    contentDescription = "Three dots",
                    alignment = Alignment.CenterEnd,
                    modifier = Modifier
                        .height(30.dp)
                        .wrapContentWidth()
                        .clickable {
                            isOptionMenuExpand = true
                        }
                )
                if (isOptionMenuExpand) {
                    ShowOptionMenu(
                        isOptionMenuExpand = isOptionMenuExpand,
                        isShowRemoveFromPlaylist = isShowRemoveFromPlaylist,
                        isShowGotoDetailButton = isShowGotoDetailButton,
                        isShowAddToPlaylist = !listPlaylistFiltered.isNullOrEmpty(),
                        isFavorite = isFavorite,
                        onDismiss = { isOptionMenuExpand = false },
                        onShowDetail = {
                            isOptionMenuExpand = false
                            navController.routeToMusicDetail(track.id)
                        },
                        onAddFavorite = {
                            isOptionMenuExpand = false
                            onAddFavorite(track)
                        },
                        onRemoveTrackPlaylist = {
                            isOptionMenuExpand = false
                            onRemoveTrackPlaylist(track)
                        }
                    ) {
                        isOptionMenuExpand = false
                        isShowSelectorTrack = true
                    }
                }
            }
        }
    }

    if (isShowSelectorTrack) {
        ShowSelectorPlaylist(
            track,
            listPlaylistFiltered,
            onDismiss = {
                isShowSelectorTrack = false
            },
            onConfirm = { track, playlist ->
                isShowSelectorTrack = false
                onAddPlaylist(track, playlist)
            }
        )
    }
}

@Composable
fun ShowOptionMenu(
    isOptionMenuExpand: Boolean,
    isShowRemoveFromPlaylist: Boolean,
    isShowGotoDetailButton: Boolean,
    isShowAddToPlaylist: Boolean,
    isFavorite: Boolean,
    onRemoveTrackPlaylist: () -> Unit = {},
    onDismiss: () -> Unit,
    onShowDetail: () -> Unit,
    onAddFavorite: () -> Unit,
    onAddPlaylist: () -> Unit,
) {
    val favoriteOptionText = if (isFavorite) {
        LocalContext.current.getString(R.string.remove_favorite)
    } else {
        LocalContext.current.getString(R.string.add_favorite)
    }

    DropdownMenu(
        expanded = isOptionMenuExpand,
        onDismissRequest = { onDismiss() }
    ) {
        if (isShowGotoDetailButton) {
            DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ShowDetailButton(color = Black80, size = 20)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = LocalContext.current.getString(R.string.show_detail),
                            color = Black80,
                            fontSize = 12.sp
                        )
                    }
                }, onClick = {
                    onShowDetail()
                }
            )
        }
        if (isShowAddToPlaylist) {
            DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AddToListButton(color = Blue500, size = 20) {
                            onAddPlaylist()
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = LocalContext.current.getString(R.string.add_playlist),
                            color = Blue500,
                            fontSize = 12.sp
                        )
                    }
                }, onClick = {
                    onAddPlaylist()
                }
            )
        }
        if (isShowRemoveFromPlaylist) {
            DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RemoveFromListButton(color = Blue500, size = 20) {
                            onRemoveTrackPlaylist()
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = LocalContext.current.getString(R.string.remove_playlist),
                            color = Blue500,
                            fontSize = 12.sp
                        )
                    }
                }, onClick = {
                    onRemoveTrackPlaylist()
                }
            )
        }
        DropdownMenuItem(
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AddToFavoriteButton(color = Red500, size = 20){
                        onAddFavorite()
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = favoriteOptionText,
                        color = Red500,
                        fontSize = 12.sp
                    )
                }
            }, onClick = {
                onAddFavorite()
            }
        )
    }
}

@Composable
fun ShowSelectorPlaylist(
    track: Track,
    listPlaylistFiltered: List<PlaylistModel>?,
    onDismiss: () -> Unit,
    onConfirm: (Track, PlaylistModel?) -> Unit
) {
    var selectedPlaylist: PlaylistModel? by remember { mutableStateOf(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = LocalContext.current.getString(R.string.choose_playlist),
                color = Black80,
                fontSize = 16.sp
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp)
            ) {
                items(listPlaylistFiltered.orEmpty()) {
                    Box(modifier = Modifier
                        .padding(5.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (selectedPlaylist?.playlistName == it.playlistName) SpotifyGreen80 else Transparent)
                    ) {
                        ItemTrackLayout(
                            icon = it.playlistIcon,
                            title = it.playlistName,
                            textColor = Black80,
                            onClick = { selectedPlaylist = it }
                        )
                        Spacer(modifier = Modifier.fillMaxWidth().height(8.dp))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (selectedPlaylist == null) return@TextButton
                onConfirm(track, selectedPlaylist)
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
