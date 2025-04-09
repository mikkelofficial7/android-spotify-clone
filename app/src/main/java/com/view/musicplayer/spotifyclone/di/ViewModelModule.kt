package com.view.musicplayer.spotifyclone.di
import com.view.musicplayer.spotifyclone.viewmodel.HomepageViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class ViewModelModule {
    companion object {
        val viewModelModule = module {
            viewModel { HomepageViewModel(get()) }
        }
    }
}