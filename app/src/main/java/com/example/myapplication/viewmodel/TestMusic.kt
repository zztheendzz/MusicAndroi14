package com.example.myapplication.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import com.example.myapplication.R
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import androidx.core.net.toUri
import com.example.myapplication.service.MusicService

@HiltViewModel
class TestMusic @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    fun playMusic() {
        val intent = Intent(context, MusicService::class.java).apply {
            action = MusicService.ACTION_PLAY
        }
        context.startForegroundService(intent)
    }

    fun pauseMusic() {
        val intent = Intent(context, MusicService::class.java).apply {
            action = MusicService.ACTION_PAUSE
        }
        context.startForegroundService(intent)
    }

    fun stopMusic() {
        val intent = Intent(context, MusicService::class.java).apply {
            action = MusicService.ACTION_STOP
        }
        context.startForegroundService(intent)
    }
}