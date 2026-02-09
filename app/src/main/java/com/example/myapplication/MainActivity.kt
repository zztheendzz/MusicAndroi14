package com.example.myapplication

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import com.example.myapplication.ui.MusicListScreen
import com.example.myapplication.viewmodel.MusicViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MusicViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // 1. Khai báo Launcher để xin quyền
            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                if (isGranted) {
                    // Nếu người dùng đồng ý, quét nhạc ngay
                    viewModel.loadSongs()
                } else {
                    // Xử lý khi bị từ chối (ví dụ: hiện thông báo lỗi)
                }
            }

            // 2. Tự động kích hoạt khi vào App
            LaunchedEffect(Unit) {
                val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Manifest.permission.READ_MEDIA_AUDIO
                } else {
                    Manifest.permission.READ_EXTERNAL_STORAGE
                }
                permissionLauncher.launch(input = permission)
            }
            MusicListScreen(viewModel = viewModel)

            // 3. Giao diện hiển thị (Gọi hàm Composable của bạn ở đây)
            // Ví dụ: MusicListScreen(viewModel)
        }
    }
}