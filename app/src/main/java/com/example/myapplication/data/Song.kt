package com.example.myapplication.data

import android.net.Uri

data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val duration: Int,
    val uri: Uri,
    val albumId: Long
)