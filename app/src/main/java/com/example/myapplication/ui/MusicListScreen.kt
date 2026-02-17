package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.Song
import com.example.myapplication.viewmodel.MusicViewModel

@Composable
fun MusicListScreen(
    viewModel: MusicViewModel,
    onSongClick: (Song) -> Unit
) {
    // Lấy danh sách từ ViewModel (đã giải thích ở phần StateFlow)
    val songList by viewModel.songs.collectAsState()
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF0A0E14))) {
        // Tiêu đề: Bài hát (số lượng)
        Text(
            text = "Bài hát (${songList.size})",
            color = Color.White,
            modifier = Modifier.padding(16.dp)
        )
        // Danh sách cuộn động
        LazyColumn {
            items(songList) { song ->
                SongItem(
                    song = song,
                    onClick = {
                        onSongClick(song);
                        viewModel.onEvent(MusicEvent.PlaySong(song))
                    }
                )
            }
        }
    }
}