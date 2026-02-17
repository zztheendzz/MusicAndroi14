package com.example.myapplication.service

import android.content.ComponentName
import android.content.Context
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@Singleton
class MusicServiceConnection @Inject constructor(
    @ApplicationContext context: Context
) {
    private val _isConnected = MutableStateFlow(false)
    val isConnected = _isConnected.asStateFlow()

    var mediaController: MediaController? = null

    init {
        val sessionToken = SessionToken(context, ComponentName(context, MusicService::class.java))
        val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()

        // Kết nối bất đồng bộ - KHÔNG DÙNG .get() trực tiếp
        controllerFuture.addListener({
            mediaController = controllerFuture.get()
            _isConnected.value = true
        }, MoreExecutors.directExecutor())
    }
}