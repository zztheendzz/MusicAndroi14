package com.example.myapplication.ui

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.viewmodel.MusicViewModel


@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun MusicNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.SongList.route
    ) {
        composable(Screen.SongList.route) { backStackEntry ->
            val viewModel: MusicViewModel = hiltViewModel(backStackEntry)
            MusicListScreen(
                viewModel = viewModel,
                onSongClick = { song ->
                    viewModel.selectSong(song)
                    navController.navigate(Screen.Player.route)
                }
            )
        }
        composable(Screen.Player.route) {
            val parentEntry = remember(navController) {
                navController.getBackStackEntry(Screen.SongList.route)
            }
            val viewModel: MusicViewModel = hiltViewModel(parentEntry)
            MusicPlayerScreen(viewModel)
        }
    }
}
