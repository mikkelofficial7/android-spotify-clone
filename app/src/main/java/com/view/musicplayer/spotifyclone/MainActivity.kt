package com.view.musicplayer.spotifyclone

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asFlow
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.view.musicplayer.spotifyclone.constants.Constants
import com.view.musicplayer.spotifyclone.ext.checkNotificationPermission
import com.view.musicplayer.spotifyclone.ext.convertToPlayerStatus
import com.view.musicplayer.spotifyclone.navigation.ScreenRoute
import com.view.musicplayer.spotifyclone.navigation.routeToHomePage
import com.view.musicplayer.spotifyclone.navigation.routeToLogin
import com.view.musicplayer.spotifyclone.network.response.Track
import com.view.musicplayer.spotifyclone.room.model.User
import com.view.musicplayer.spotifyclone.screen.AlbumDetailScreen
import com.view.musicplayer.spotifyclone.screen.HomeScreen
import com.view.musicplayer.spotifyclone.screen.LoginScreen
import com.view.musicplayer.spotifyclone.screen.PlaylistDetailScreen
import com.view.musicplayer.spotifyclone.screen.ProfileScreen
import com.view.musicplayer.spotifyclone.screen.SearchScreen
import com.view.musicplayer.spotifyclone.screen.SongDetailScreen
import com.view.musicplayer.spotifyclone.screen.shared.loadIconToVector
import com.view.musicplayer.spotifyclone.service.MusicService
import com.view.musicplayer.spotifyclone.service.listener.ServiceStartOrStopListener
import com.view.musicplayer.spotifyclone.ui.theme.AndroidspotifycloneTheme
import com.view.musicplayer.spotifyclone.ui.theme.Black80
import com.view.musicplayer.spotifyclone.ui.theme.SpotifyAccent80
import com.view.musicplayer.spotifyclone.ui.theme.Transparent
import com.view.musicplayer.spotifyclone.viewmodel.MainActivityViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val notificationListener: ServiceStartOrStopListener by inject()
    private lateinit var broadcastReceiver: BroadcastReceiver
    private val viewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkNotificationPermission()
        registerBroadcast()

        viewModel.getAllTrackList(this)
        viewModel.checkUserLogin(this)

        setContent {
            AndroidspotifycloneTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val isFinishLoad by viewModel.finishLoad.observeAsState()

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Black80)
                    ) {
                        if (isFinishLoad == true) {
                            MainPage(
                                viewModel = viewModel,
                                listener = notificationListener,
                                context = this@MainActivity
                            )
                        } else {
                            FirstLoadingPage()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        notificationListener.onStop(this)
        unRegisterBroadcast()
        super.onDestroy()
    }

    private fun unRegisterBroadcast() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }

    private fun registerBroadcast() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val bundle = intent.extras

                viewModel.currentTrack.postValue(bundle?.getParcelable(MusicService.INTENT.PENDING_MUSIC_DATA))
                viewModel.currentTrackStatus.postValue(bundle?.getString(MusicService.INTENT.PENDING_MUSIC_STATUS) ?: "")
                viewModel.currentTrackDuration.postValue(bundle?.getLong(MusicService.INTENT.PENDING_DURATION) ?: 0L)
                viewModel.currentTrackDurationTotal.postValue(bundle?.getLong(MusicService.INTENT.PENDING_DURATION_TOTAL) ?: 0L)
                viewModel.currentTrackDurationText.postValue(bundle?.getString(MusicService.INTENT.PENDING_DURATION_TEXT) ?: "")
                viewModel.currentTrackDurationTotalText.postValue(bundle?.getString(MusicService.INTENT.PENDING_DURATION_TOTAL_TEXT) ?: "")
            }
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, IntentFilter(MusicService.Notification.BROADCAST_NAME))
    }
}

@Composable
fun FirstLoadingPage() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Black80)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = SpotifyAccent80,
                strokeWidth = 4.dp,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = LocalContext.current.getString(R.string.loading_text, Constants.AI_MODEL),
                color = SpotifyAccent80,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
            )
        }
    }
}

@Composable
fun MainPage(
    viewModel: MainActivityViewModel,
    listener: ServiceStartOrStopListener,
    context: Context
) {
    val navController = rememberNavController()
    var isShowPlayerButton by rememberSaveable { mutableStateOf(false) }
    var isShuffle by rememberSaveable { mutableStateOf(false) }

    val userLogin by viewModel.userData.observeAsState()
    val successLogin by viewModel.successLogin.observeAsState()
    val currentPlaying by viewModel.currentTrack.observeAsState()
    val playerStatus by viewModel.currentTrackStatus.observeAsState()
    val trackProgress by viewModel.currentTrackDuration.observeAsState()
    val trackProgressTotal by viewModel.currentTrackDurationTotal.observeAsState()
    val trackProgressText by viewModel.currentTrackDurationText.observeAsState()
    val trackProgressTotalText by viewModel.currentTrackDurationTotalText.observeAsState()

    var isUserHasLogin by remember { mutableStateOf(userLogin != null) }

    if (successLogin == true) {
        isUserHasLogin = true
        navController.routeToHomePage()
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = ScreenRoute.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(ScreenRoute.Login.route) {
                LoginScreen { name, email, age ->
                    viewModel.doLogin(context,
                        User(fullname = name, email = email, age = age)
                    )
                }
            }
            composable(ScreenRoute.Home.route) {
                HomeScreen(
                    navController = navController,
                    isUserHasLogin = isUserHasLogin,
                    isShowPlayerButton = isShowPlayerButton,
                    playerStatus = playerStatus?.convertToPlayerStatus() ?: MusicService.PlayerStatus.STOP,
                    trackProgress = trackProgress ?: 0L,
                    trackProgressTotal = trackProgressTotal ?: 0L,
                    trackProgressText = trackProgressText.orEmpty(),
                    trackProgressTotalText = trackProgressTotalText.orEmpty(),
                    currentPlaying = currentPlaying ?: Track.empty,
                    isShuffle = isShuffle,
                    onClickMusic = {
                        if (!isUserHasLogin) {
                            navController.routeToLogin()
                            return@HomeScreen
                        }

                        if (currentPlaying?.id == it.id) {
                            isShowPlayerButton = !isShowPlayerButton
                            listener.onStop(context)
                        } else {
                            isShowPlayerButton = true
                            listener.onPlay(context, it.id)
                        }
                    },
                    onPlayPauseClick = {
                        if (playerStatus == MusicService.PlayerStatus.PLAY.status) {
                            listener.onPause(context)
                        } else {
                            listener.onPlay(context, currentPlaying?.id.orEmpty())
                        }
                    },
                    onNextClick = {
                        listener.onNext(context)
                    },
                    onPreviousClick = {
                        listener.onPrevious(context)
                    },
                    onRefreshClick = {
                        listener.onRestart(context)
                    },
                    onShuffleClick = {
                        isShuffle = !isShuffle
                        listener.onShuffle(context, isShuffle)
                    }
                )
            }
            composable(ScreenRoute.Search.route) {
                SearchScreen(
                    isShowPlayerButton = isShowPlayerButton,
                    navController = navController,
                    currentPlaying = currentPlaying ?: Track.empty,
                    playerStatus = playerStatus?.convertToPlayerStatus() ?: MusicService.PlayerStatus.STOP,
                    trackProgress = trackProgress ?: 0L,
                    isUserHasLogin = isUserHasLogin,
                    trackProgressTotal = trackProgressTotal ?: 0L,
                    trackProgressText = trackProgressText.orEmpty(),
                    trackProgressTotalText = trackProgressTotalText.orEmpty(),
                    isShuffle = isShuffle,
                    onClickMusic = {
                        if (!isUserHasLogin) {
                            navController.routeToLogin()
                            return@SearchScreen
                        }

                        if (currentPlaying?.id == it.id) {
                            isShowPlayerButton = !isShowPlayerButton
                            listener.onStop(context)
                        } else {
                            isShowPlayerButton = true
                            listener.onPlay(context, it.id)
                        }
                    },
                    onPlayPauseClick = {
                        if (playerStatus == MusicService.PlayerStatus.PLAY.status) {
                            listener.onPause(context)
                        } else {
                            listener.onPlay(context, currentPlaying?.id.orEmpty())
                        }
                    },
                    onNextClick = {
                        listener.onNext(context)
                    },
                    onPreviousClick = {
                        listener.onPrevious(context)
                    },
                    onRefreshClick = {
                        listener.onRestart(context)
                    },
                    onShuffleClick = {
                        isShuffle = !isShuffle
                        listener.onShuffle(context, isShuffle)
                    }
                )
            }
            composable(ScreenRoute.Profile.route) {
                if (!isUserHasLogin) {
                    LoginScreen { name, email, age ->
                        viewModel.doLogin(context,
                            User(fullname = name, email = email, age = age)
                        )
                    }
                } else {
                    ProfileScreen(
                        navController = navController,
                        isShowPlayerButton = isShowPlayerButton,
                        currentPlaying = currentPlaying ?: Track.empty,
                        playerStatus = playerStatus?.convertToPlayerStatus() ?: MusicService.PlayerStatus.STOP,
                        trackProgress = trackProgress ?: 0L,
                        trackProgressTotal = trackProgressTotal ?: 0L,
                        trackProgressText = trackProgressText.orEmpty(),
                        trackProgressTotalText = trackProgressTotalText.orEmpty(),
                        isShuffle = isShuffle,
                        onClickMusic = {
                            if (currentPlaying?.id == it.id) {
                                isShowPlayerButton = !isShowPlayerButton
                                listener.onStop(context)
                            } else {
                                isShowPlayerButton = true
                                listener.onPlay(context, it.id)
                            }
                        },
                        onPlayPauseClick = {
                            if (playerStatus == MusicService.PlayerStatus.PLAY.status) {
                                listener.onPause(context)
                            } else {
                                listener.onPlay(context, currentPlaying?.id.orEmpty())
                            }
                        },
                        onNextClick = {
                            listener.onNext(context)
                        },
                        onPreviousClick = {
                            listener.onPrevious(context)
                        },
                        onRefreshClick = {
                            listener.onRestart(context)
                        },
                        onShuffleClick = {
                            isShuffle = !isShuffle
                            listener.onShuffle(context, isShuffle)
                        }
                    )
                }
            }
            composable(
                route = ScreenRoute.AlbumDetail.route,
                arguments = listOf(navArgument("albumGenre") { type = NavType.StringType })
            ) { backStackEntry ->
                val albumGenre = backStackEntry.arguments?.getString("albumGenre").orEmpty()
                AlbumDetailScreen(
                    navController = navController,
                    isShowPlayerButton = isShowPlayerButton,
                    albumGenre = albumGenre,
                    currentPlaying = currentPlaying ?: Track.empty,
                    playerStatus = playerStatus?.convertToPlayerStatus() ?: MusicService.PlayerStatus.STOP,
                    trackProgress = trackProgress ?: 0L,
                    trackProgressTotal = trackProgressTotal ?: 0L,
                    trackProgressText = trackProgressText.orEmpty(),
                    trackProgressTotalText = trackProgressTotalText.orEmpty(),
                    isShuffle = isShuffle,
                    onClickMusic = {
                        if (currentPlaying?.id == it.id) {
                            isShowPlayerButton = !isShowPlayerButton
                            listener.onStop(context)
                        } else {
                            isShowPlayerButton = true
                            listener.onPlay(context, it.id)
                        }
                    },
                    onPlayPauseClick = {
                        if (playerStatus == MusicService.PlayerStatus.PLAY.status) {
                            listener.onPause(context)
                        } else {
                            listener.onPlay(context, currentPlaying?.id.orEmpty())
                        }
                    },
                    onNextClick = {
                        listener.onNext(context)
                    },
                    onPreviousClick = {
                        listener.onPrevious(context)
                    },
                    onRefreshClick = {
                        listener.onRestart(context)
                    },
                    onShuffleClick = {
                        isShuffle = !isShuffle
                        listener.onShuffle(context, isShuffle)
                    }
                )
            }
            composable(
                route = ScreenRoute.MusicDetail.route,
                arguments = listOf(navArgument("musicTitle") { type = NavType.StringType })
            ) { backStackEntry ->
                SongDetailScreen(
                    navController = navController,
                    playerStatus = playerStatus?.convertToPlayerStatus() ?: MusicService.PlayerStatus.STOP,
                    trackProgress = trackProgress ?: 0L,
                    trackProgressTotal = trackProgressTotal ?: 0L,
                    trackProgressText = trackProgressText.orEmpty(),
                    trackProgressTotalText = trackProgressTotalText.orEmpty(),
                    currentPlaying = currentPlaying ?: Track.empty,
                    isShuffle = isShuffle,
                    onPlayPauseClick = {
                        if (playerStatus == MusicService.PlayerStatus.PLAY.status) {
                            listener.onPause(context)
                        } else {
                            listener.onPlay(context, currentPlaying?.id.orEmpty())
                        }
                    },
                    onNextClick = {
                        listener.onNext(context)
                    },
                    onPreviousClick = {
                        listener.onPrevious(context)
                    },
                    onRefreshClick = {
                        listener.onRestart(context)
                    },
                    onShuffleClick = {
                        isShuffle = !isShuffle
                        listener.onShuffle(context, isShuffle)
                    }
                )
            }
            composable(
                route = ScreenRoute.PlaylistDetail.route,
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) { backStackEntry ->
                // onActivityResult in compose
                GetBackStackViewResult<Boolean>(navController = navController, key = "onDeletedTrackItem") {
                    navController.navigate(ScreenRoute.Profile.route)
                }

                val playlistId = backStackEntry.arguments?.getString("id").orEmpty()
                PlaylistDetailScreen(
                    navController = navController,
                    isShowPlayerButton = isShowPlayerButton,
                    playlistId = playlistId,
                    currentPlaying = currentPlaying ?: Track.empty,
                    playerStatus = playerStatus?.convertToPlayerStatus() ?: MusicService.PlayerStatus.STOP,
                    trackProgress = trackProgress ?: 0L,
                    trackProgressTotal = trackProgressTotal ?: 0L,
                    trackProgressText = trackProgressText.orEmpty(),
                    trackProgressTotalText = trackProgressTotalText.orEmpty(),
                    isShuffle = isShuffle,
                    onClickMusic = {
                        if (currentPlaying?.id == it.id) {
                            isShowPlayerButton = !isShowPlayerButton
                            listener.onStop(context)
                        } else {
                            isShowPlayerButton = true
                            listener.onPlay(context, it.id)
                        }
                    },
                    onPlayPauseClick = {
                        if (playerStatus == MusicService.PlayerStatus.PLAY.status) {
                            listener.onPause(context)
                        } else {
                            listener.onPlay(context, currentPlaying?.id.orEmpty())
                        }
                    },
                    onNextClick = {
                        listener.onNext(context)
                    },
                    onPreviousClick = {
                        listener.onPrevious(context)
                    },
                    onRefreshClick = {
                        listener.onRestart(context)
                    },
                    onShuffleClick = {
                        isShuffle = !isShuffle
                        listener.onShuffle(context, isShuffle)
                    }
                )
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController) {
    val itemsInNav = listOf(ScreenRoute.Home, ScreenRoute.Search, ScreenRoute.Profile)
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    NavigationBar {
        itemsInNav.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(loadIconToVector(screen.icon), contentDescription = screen.title, modifier = Modifier
                    .width(30.dp)
                    .height(30.dp)) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Transparent,
                    selectedIconColor = SpotifyAccent80,
                    unselectedIconColor = Black80,
                    selectedTextColor = SpotifyAccent80,
                    unselectedTextColor = Black80
                ),
                onClick = {
                    if (currentRoute == screen.route) return@NavigationBarItem
                    navController.navigate(screen.route) {
                        launchSingleTop = true
                        restoreState = false
                    }
                },
                interactionSource = remember { MutableInteractionSource() },
            )
        }
    }
}

@Composable
fun <T>GetBackStackViewResult(navController: NavController, key: String, onComplete: () -> Unit) {
    val backStackResult = navController.currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)?.asFlow()
    LaunchedEffect(backStackResult) {
        backStackResult?.collect { result ->
            navController.currentBackStackEntry?.savedStateHandle?.remove<T>(key)
            onComplete()
        }
    }
}

@Composable
fun <T>SetBackStackViewResult(navController: NavController, key: String, value: T): State<() -> Boolean> {
    val result = rememberUpdatedState {
        navController.previousBackStackEntry?.savedStateHandle?.set(key, value)
        navController.popBackStack()
    }
    return result
}
