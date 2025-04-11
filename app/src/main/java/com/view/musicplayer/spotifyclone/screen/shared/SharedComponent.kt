package com.view.musicplayer.spotifyclone.screen.shared

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.view.musicplayer.spotifyclone.R
import com.view.musicplayer.spotifyclone.ui.theme.Black100
import com.view.musicplayer.spotifyclone.ui.theme.SpotifyAccent80
import com.view.musicplayer.spotifyclone.ui.theme.White80

@Composable
fun loadIconToVector(@DrawableRes icon: Int): ImageVector {
    return ImageVector.vectorResource(id = icon)
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
fun PlayerButton(
    isPlaying: Boolean = false,
    isShuffle: Boolean = false,
    onPlayPauseClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    onPreviousClick: () -> Unit = {},
    onShuffleClick: () -> Unit = {},
    onRefreshClick: () -> Unit = {}
) {
    val iconSize = 30

    Box(modifier = Modifier
        .fillMaxWidth()
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
                    modifier = Modifier.height(iconSize.dp).width(iconSize.dp)
                )
            }
            IconButton(onClick = onPreviousClick, colors = IconButtonDefaults.iconButtonColors(
                contentColor = White80
            )) {
                Icon(
                    imageVector = loadIconToVector(icon = R.drawable.ic_previous),
                    contentDescription = "Previous",
                    modifier = Modifier.height(iconSize.dp).width(iconSize.dp)
                )
            }
            IconButton(onClick = onPlayPauseClick, colors = IconButtonDefaults.iconButtonColors(
                contentColor = White80
            )) {
                Icon(
                    imageVector = if (isPlaying) loadIconToVector(icon = R.drawable.ic_pause) else loadIconToVector(icon = R.drawable.ic_play),
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    modifier = Modifier.height(iconSize.dp).width(iconSize.dp)
                )
            }
            IconButton(onClick = onNextClick, colors = IconButtonDefaults.iconButtonColors(
                contentColor = White80
            )) {
                Icon(
                    imageVector = loadIconToVector(icon = R.drawable.ic_next),
                    contentDescription = "Next",
                    modifier = Modifier.height(iconSize.dp).width(iconSize.dp)
                )
            }
            IconButton(onClick = onRefreshClick, colors = IconButtonDefaults.iconButtonColors(
                contentColor = White80
            )) {
                Icon(
                    imageVector = loadIconToVector(icon = R.drawable.ic_repeat),
                    contentDescription = "Refresh",
                    modifier = Modifier.height(iconSize.dp).width(iconSize.dp)
                )
            }
        }
    }
}
