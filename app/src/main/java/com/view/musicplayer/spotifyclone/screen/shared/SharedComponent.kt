package com.view.musicplayer.spotifyclone.screen.shared

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.view.musicplayer.spotifyclone.R
import com.view.musicplayer.spotifyclone.ext.formatTimeTrackRunning
import com.view.musicplayer.spotifyclone.ui.theme.Black100
import com.view.musicplayer.spotifyclone.ui.theme.SpotifyAccent80
import com.view.musicplayer.spotifyclone.ui.theme.SpotifyGreen80
import com.view.musicplayer.spotifyclone.ui.theme.SpotifyGreenGrey80
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
    val middleIconSize = 50

    Column {
        if (isShowTimeTrack) {
            Text(
                text = "${currentPosition.formatTimeTrackRunning()} / ${totalDuration.formatTimeTrackRunning()}",
                color = White80,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .align(Alignment.End)
            )
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
                            .height(iconSize.dp)
                            .width(iconSize.dp)
                    )
                }
                IconButton(onClick = onPlayPauseClick, colors = IconButtonDefaults.iconButtonColors(
                    contentColor = White80
                )) {
                    Icon(
                        imageVector = if (isPlaying) loadIconToVector(icon = R.drawable.ic_pause) else loadIconToVector(icon = R.drawable.ic_play),
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        modifier = Modifier
                            .height(middleIconSize.dp)
                            .width(middleIconSize.dp)
                    )
                }
                IconButton(onClick = onNextClick, colors = IconButtonDefaults.iconButtonColors(
                    contentColor = White80
                )) {
                    Icon(
                        imageVector = loadIconToVector(icon = R.drawable.ic_next),
                        contentDescription = "Next",
                        modifier = Modifier
                            .height(iconSize.dp)
                            .width(iconSize.dp)
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
fun BackButton(onBack: () -> Unit) {
    Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
        contentDescription = "",
        tint = White80,
        modifier = Modifier.clickable { onBack() }
    )
}
