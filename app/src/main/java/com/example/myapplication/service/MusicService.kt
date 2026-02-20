package com.example.myapplication.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.example.myapplication.R
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

@AndroidEntryPoint
class MusicService : MediaSessionService() {
    @Inject
    lateinit var player: ExoPlayer
    private var mediaSession: MediaSession? = null
    override fun onCreate() {
        super.onCreate()
        mediaSession = MediaSession.Builder(this, player).build()
        startForegroundPlayback()
    }
    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }
    companion object {
        const val ACTION_PLAY = "com.example.myapplication.action.PLAY"
        const val ACTION_PAUSE = "com.example.myapplication.action.PAUSE"
        const val ACTION_STOP = "com.example.myapplication.action.STOP"
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {

        when (intent?.action) {
//            ACTION_PLAY -> play()
            ACTION_PAUSE -> player.pause()
            ACTION_STOP -> stopSelf()
        }

        // BẮT BUỘC gọi super cho MediaSession
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }
    private fun startForegroundService() {
        val channelId = "music_playback"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Music Playback",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val notification = Notification.Builder(this, channelId)
            .setContentTitle("Playing music")
            .setContentText("Test mp3")
            .setSmallIcon(R.mipmap.ic_launcher)
            .build()

        startForeground(1, notification)
    }
    private fun startForegroundPlayback() {
        val channelId = "music_playback"

        val channel = NotificationChannel(
            channelId,
            "Music Playback",
            NotificationManager.IMPORTANCE_LOW
        )

        val nm = getSystemService(NotificationManager::class.java)
        nm.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Playing music")
            .setContentText("Test mp3")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .build()

        startForeground(1, notification)
    }
    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.d("MusicService", "App was swiped away")
        player?.apply {
            stop()
            release()
        }
        stopSelf()
    }
}