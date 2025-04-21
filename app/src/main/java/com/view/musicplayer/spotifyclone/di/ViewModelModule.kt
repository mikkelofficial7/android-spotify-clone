package com.view.musicplayer.spotifyclone.di
import com.view.musicplayer.spotifyclone.viewmodel.AlbumDetailViewModel
import com.view.musicplayer.spotifyclone.viewmodel.MainActivityViewModel
import com.view.musicplayer.spotifyclone.viewmodel.HomePageViewModel
import com.view.musicplayer.spotifyclone.viewmodel.PlaylistDetailViewModel
import com.view.musicplayer.spotifyclone.viewmodel.ProfileViewModel
import com.view.musicplayer.spotifyclone.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class ViewModelModule {
    companion object {
        val viewModelModule = module {
            viewModel { MainActivityViewModel(get(), get()) }
            viewModel { HomePageViewModel(get()) }
            viewModel { SearchViewModel(get()) }
            viewModel { AlbumDetailViewModel(get()) }
            viewModel { ProfileViewModel(get()) }
            viewModel { PlaylistDetailViewModel(get()) }
        }
    }
}