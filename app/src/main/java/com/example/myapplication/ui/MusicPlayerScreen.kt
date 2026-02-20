package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.viewmodel.MusicViewModel

@Composable
fun MusicPlayerScreen(viewModel: MusicViewModel ) {
    val uiState by viewModel.uiState.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentPosition by viewModel.currentPosition.collectAsState()
    val duration by viewModel.duration.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101418)) // Màu nền tối
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Top Bar (Danh sách, Bài hát, Lời bài hát)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Back", tint = Color.White)
            Text("Bài hát", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.White)
        }

        Spacer(modifier = Modifier.height(48.dp))

        // 2. Album Art (Hình tròn lớn)
        Box(
            modifier = Modifier
                .size(250.dp)
                .background(Color(0xFF2C3E50), CircleShape),
               // .clickable { viewModel.onEvent(MusicEvent.PlayPause) }, // Màu xám đậm
            contentAlignment = Alignment.Center
        ) {
//            Icon(
//// Đổi icon tùy theo trạng thái isPlaying
//
//                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
////                    contentDescription = null
//
//                contentDescription = "Play/Pause",
//                tint = Color.White,
//                modifier = Modifier.size(40.dp)
//            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 3. Tên bài hát & Ca sĩ
        Text(
            text = uiState.currentSong?.title ?: "Chưa chọn bài hát",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = uiState.currentSong?.artist ?: "<unknown>",
            color = Color.Gray,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 4. Các nút chức năng (Like, Add, Equalizer, Timer, Volume)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ActionButton(icon = Icons.Default.FavoriteBorder, onClick = {})
            ActionButton(icon = Icons.Default.AddCircleOutline, onClick = {})
            ActionButton(icon = Icons.Default.GraphicEq, onClick = {})
            ActionButton(icon = Icons.Default.Schedule, onClick = {})
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 5. Thanh SeekBar (Slider)
        var sliderPosition by remember { mutableStateOf(0.1f) }
        Column {

            Slider(
                value = if (duration > 0)
                    currentPosition.toFloat() / duration.toFloat()
                else 0f,

                onValueChange = { value ->
                    val newPosition = (value * duration).toLong()
                    viewModel.seekTo(newPosition)
                },

                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.LightGray,
                    inactiveTrackColor = Color.DarkGray
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatTime(currentPosition),
                    color = Color.White
                )
                Text(
                    text = formatTime(duration),
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // 6. Media Controls (Shuffle, Back, Play, Next, Repeat)
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Shuffle, contentDescription = "Shuffle", tint = Color.White)
            IconButton(
                onClick = { viewModel.onEvent(MusicEvent.Previous) }
            ) {
                Icon(
                    imageVector = Icons.Default.SkipPrevious,
                    contentDescription = "Next",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            // Nút Play to ở giữa
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .background(Color(0xFF00BFFF), CircleShape)
                    .clickable { viewModel.onEvent(MusicEvent.PlayPause) },// Màu xanh cyan
                contentAlignment = Alignment.Center
            ) {
                Icon(
                  imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Play/Pause",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            IconButton(
                onClick = { viewModel.onEvent(MusicEvent.Next) }
            ) {
                Icon(
                    imageVector = Icons.Default.SkipNext,
                    contentDescription = "Next",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            IconButton(
                onClick = { viewModel.onEvent(MusicEvent.Repeat) }
            ) {
                Icon(
                    imageVector = Icons.Default.Repeat,
                    contentDescription = "Repeat",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}
fun formatTime(timeMs: Long): String {
    val totalSeconds = timeMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
@Composable
fun ActionButton(
    icon: ImageVector,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}