package com.example.myapplication.ui

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.myapplication.viewmodel.MusicViewModel
@Composable
fun MusicNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "music_flow" // Route của cả cụm
    ) {
        // Tạo một cụm (Graph) riêng cho Nhạc
        navigation(
            route = "music_flow",
            startDestination = Screen.SongList.route
        ) {
            composable(Screen.SongList.route) { backStackEntry ->
                // Lấy ViewModel theo "music_flow"
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("music_flow")
                }
                val viewModel: MusicViewModel = hiltViewModel(parentEntry)

                MusicListScreen(
                    viewModel = viewModel,
                    onSongClick = { song ->
                        viewModel.onEvent(MusicEvent.PlaySong(song))
                        navController.navigate(Screen.Player.route)
                    }
                )
            }

            composable(Screen.Player.route) { backStackEntry ->
                // Cũng lấy chung một instance ViewModel từ "music_flow"
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("music_flow")
                }
                val viewModel: MusicViewModel = hiltViewModel(parentEntry)

                MusicPlayerScreen(viewModel)
            }
        }
    }
}