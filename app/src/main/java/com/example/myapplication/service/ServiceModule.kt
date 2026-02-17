package com.example.myapplication.service

import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.exoplayer.ExoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import androidx.media3.common.C
import androidx.media3.common.Player
import jakarta.inject.Singleton
@Module
@InstallIn(ServiceComponent::class) // Chỉ tồn tại trong phạm vi Service
object ServiceModule {
    @Provides
    @ServiceScoped // Đảm bảo chỉ có 1 instance duy nhất trong Service này
    fun provideAttributes(): AudioAttributes = AudioAttributes.Builder()
        .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build()
    @Provides
    @ServiceScoped
    fun provideExoPlayer(
        @ApplicationContext context: Context,
        audioAttributes: AudioAttributes
    ): ExoPlayer = ExoPlayer.Builder(context)
        .setAudioAttributes(audioAttributes, true)
        .setHandleAudioBecomingNoisy(true) // Tự dừng khi rút tai nghe
        .build()
}