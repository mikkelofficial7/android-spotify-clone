package com.view.musicplayer.spotifyclone

import android.app.Application
import com.view.musicplayer.spotifyclone.di.ApiModule
import com.view.musicplayer.spotifyclone.di.NetworkModule
import com.view.musicplayer.spotifyclone.di.ViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this

        startKoin {
            androidContext(this@MainApp)
            modules(
                NetworkModule.NetworkModule,
                ApiModule.apiModule,
                ViewModelModule.viewModelModule
            )
        }
    }

    companion object {
        lateinit var instance: MainApp
    }
}