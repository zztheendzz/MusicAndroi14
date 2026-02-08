package com.example.myapplication.service

import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

@AndroidEntryPoint
class MusicService : MediaSessionService() { // Ensure it extends the class
    @Inject
    lateinit var player: ExoPlayer
    private var mediaSession: MediaSession? = null

    override fun onCreate() {
        super.onCreate()
        // Khi Service được tạo, player đã sẵn sàng để sử dụng
        mediaSession = MediaSession.Builder(this, player).build()
    }

    // Đừng quên giải phóng player khi Service bị hủy
    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }
}