package com.view.musicplayer.spotifyclone.di

import android.content.Context
import androidx.room.Room
import com.view.musicplayer.spotifyclone.ext.NetworkHandler
import com.view.musicplayer.spotifyclone.network.RetrofitBuilder
import com.view.musicplayer.spotifyclone.room.AppDb
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

class RoomModule {
    companion object {
        val RoomModule = module {
            single { provideDB(androidContext()) }
        }

        private fun provideDB(context: Context): AppDb {
            return Room.databaseBuilder(context, AppDb::class.java, "db_app_clone")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}