package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.Song

@Composable
fun SongItem(song: Song, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon nốt nhạc bên trái
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color.DarkGray, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.MusicNote, contentDescription = null, tint = Color.LightGray)
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Thông tin chữ
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = song.title,
                color = Color.Cyan, // Màu xanh như trong ảnh mẫu
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${song.duration} - ${song.artist}",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }

        // Nút Menu 3 chấm bên phải
        Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color.White)
    }
}