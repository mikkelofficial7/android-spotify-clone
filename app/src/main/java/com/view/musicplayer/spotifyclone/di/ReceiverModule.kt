package com.view.musicplayer.spotifyclone.di

import com.view.musicplayer.spotifyclone.service.listener.ServiceStartOrStopListener
import com.view.musicplayer.spotifyclone.service.receiver.ActivityToServiceReceiver
import org.koin.dsl.module

class ReceiverModule {
    companion object {
        val module = module {
            single<ServiceStartOrStopListener> { provideActivityToServiceReceiver() }
        }

        private fun provideActivityToServiceReceiver() : ActivityToServiceReceiver {
            return ActivityToServiceReceiver()
        }
    }
}