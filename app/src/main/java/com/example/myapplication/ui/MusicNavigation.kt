package com.example.myapplication.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.*
import com.example.myapplication.viewmodel.MusicViewModel

@Composable
fun MusicNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "music-flow"
    ) {

        navigation(
            route = "music-flow",
            startDestination = Screen.SongList.route
        ) {

            // ---------------- SONG LIST ----------------

            composable(Screen.SongList.route) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("music-flow")
                }

                val viewModel: MusicViewModel =
                    hiltViewModel(parentEntry)

                val uiState by viewModel.uiState.collectAsState()

                Scaffold(
                    bottomBar = {
                        if (uiState.currentSong != null) {
                            MiniPlayer(
                                song = uiState.currentSong!!,
                                isPlaying = uiState.isPlaying,
                                onPlayPause = {
                                    viewModel.onEvent(MusicEvent.PlayPause)
                                },
                                onNext = {
                                    viewModel.onEvent(MusicEvent.Next)
                                },
                                onPrevious = {
                                    viewModel.onEvent(MusicEvent.Previous)
                                },
                                onClick = {
                                    navController.navigate(Screen.Player.route)
                                }
                            )
                        }
                    }
                ) { padding ->

                    MusicListScreen(
                        viewModel = viewModel,
                        onSongClick = { song ->
                            viewModel.onEvent(MusicEvent.PlaySong(song))
                        },
                        modifier = Modifier.padding(padding)
                    )
                }
            }

            // ---------------- PLAYER SCREEN ----------------

            composable(Screen.Player.route) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("music-flow")
                }

                val viewModel: MusicViewModel =
                    hiltViewModel(parentEntry)

                MusicPlayerScreen(
                    viewModel = viewModel
                )
            }
        }
    }
}