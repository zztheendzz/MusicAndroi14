package com.example.myapplication.ui

sealed class Screen(val route: String) {
    object SongList : Screen("song_list")
    object Player : Screen("player")
}