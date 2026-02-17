package com.example.myapplication.ui

import com.example.myapplication.data.Song

// 2. Định nghĩa Data Class để chứa thông tin trạng thái
data class MusicUiState(
    val isPlaying: Boolean = false,
    val currentSong: Song? = null
)