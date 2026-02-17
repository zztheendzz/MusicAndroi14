//package com.example.myapplication.service
//
//import android.content.ComponentName
//import android.content.Context
//import androidx.media3.session.MediaController
//import androidx.media3.session.SessionToken
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import jakarta.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object MediaControllerModule {
//
//    @Provides
//    @Singleton
//    fun provideMediaController(
//        @ApplicationContext context: Context
//    ): MediaController {
//
//        val sessionToken = SessionToken(
//            context,
//            ComponentName(context, MusicService::class.java)
//        )
//
//        return MediaController.Builder(context, sessionToken)
//            .buildAsync().get()
//    }
//}