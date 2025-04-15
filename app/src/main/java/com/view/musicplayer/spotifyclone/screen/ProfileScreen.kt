package com.view.musicplayer.spotifyclone.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.view.musicplayer.spotifyclone.R
import com.view.musicplayer.spotifyclone.screen.shared.EmptyView
import com.view.musicplayer.spotifyclone.screen.shared.ImageLoader
import com.view.musicplayer.spotifyclone.screen.shared.ItemTrackLayout
import com.view.musicplayer.spotifyclone.ui.theme.Black80
import com.view.musicplayer.spotifyclone.ui.theme.SpotifyGreen80
import com.view.musicplayer.spotifyclone.ui.theme.Transparent
import com.view.musicplayer.spotifyclone.ui.theme.White80

@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    val tabTitles = listOf(context.getString(R.string.your_favorite), context.getString(R.string.your_playlists))
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black80)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageLoader(
            url = "",
            otherModifier = Modifier
                .height(80.dp)
                .width(80.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "name", color = White80, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = "email", color = White80, fontSize = 14.sp)
        Text(text = "Age", color = White80, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(24.dp))
        Column(
            modifier = Modifier.fillMaxWidth()
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
                0 -> FavoriteListContent()
                1 -> PlaylistListContent()
            }
        }
    }
}

@Composable
fun FavoriteListContent() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .background(Transparent)
            .fillMaxSize(),
    ) {
        EmptyView(40)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaylistListContent() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .background(Transparent)
            .fillMaxSize(),
    ) {
        LazyColumn {
            stickyHeader {
                ItemTrackLayout(
                    icon = R.drawable.ic_add_no_round,
                    title = context.getString(R.string.create_playlist)
               )
            }
        }
    }
}