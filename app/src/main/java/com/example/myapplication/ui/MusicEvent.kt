package com.example.myapplication.ui

import com.example.myapplication.data.Song

sealed class MusicEvent {
    object PlayPause : MusicEvent()
    object Next : MusicEvent()
    object Previous : MusicEvent()
    object  Random: MusicEvent()
    object  Like: MusicEvent()
    object  Repeat: MusicEvent()
    data class PlaySong(val song: Song) : MusicEvent()
    data class SeekTo(val position: Long) : MusicEvent()
}