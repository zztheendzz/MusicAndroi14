package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.example.myapplication.data.Song
import com.example.myapplication.repository.MusicRepository
import com.example.myapplication.service.MusicServiceConnection
import com.example.myapplication.ui.MusicEvent
import com.example.myapplication.ui.MusicUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MusicViewModel @Inject constructor(
    private val repository: MusicRepository,
    private val serviceConnection: MusicServiceConnection
) : ViewModel() {
    init {
        loadSongs()
        setupPlayerListener()
    }
    private val mediaController get() = serviceConnection.mediaController
    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs = _songs.asStateFlow()
    private val _uiState = MutableStateFlow(MusicUiState())
    val uiState = _uiState.asStateFlow()
    fun setupPlayerListener(){
        mediaController?.addListener(object : androidx.media3.common.Player.Listener {

            // Khi bấm Play/Pause, hàm này sẽ tự chạy
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _uiState.value = _uiState.value.copy(isPlaying = isPlaying)
            }

            // Khi bài hát thay đổi (Next/Previous)
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                // Logic để tìm bài hát tương ứng từ list của bạn và update currentSong
                val currentSongId = mediaItem?.mediaId
                val song = _songs.value.find { it.id.toString() == currentSongId }
                _uiState.value = _uiState.value.copy(currentSong = song)
            }

            // Bạn có thể thêm lắng nghe RepeatMode, ShuffleMode ở đây...
        })

    }

    fun loadSongs() {
        viewModelScope.launch(Dispatchers.IO) {
            val musicList = repository.fetchLocalSongs()
            _songs.value = musicList
        }
    }


    fun onEvent(event: MusicEvent) {
        // Trình biên dịch báo lỗi nếu thiếu 1 trong 4 loại event trên
        when(event) {
            is MusicEvent.PlaySong -> handlePlaySong(event.song)
            is MusicEvent.PlayPause -> handlePlayPause()
            is MusicEvent.Next ->handleNext()
            is MusicEvent.Previous ->  handlePrevious()
            is MusicEvent.Repeat -> handleRepeat()
            is MusicEvent.Random -> handleRandom()
            is MusicEvent.Like -> handleLike()
            is MusicEvent.SeekTo -> {
                // Có thể truy cập event.position ở đây
                val pos = event.position
//                handleSeekTo()
                /* Xử lý */
            }

        }
    }

    private fun handlePlaySong(song: Song) {
        mediaController?.let { controller ->
            // 1. Tạo MediaItem từ dữ liệu bài hát (quan trọng nhất là URI)
            val mediaItem = MediaItem.Builder()
                .setMediaId(song.id.toString())
                .setUri(song.uri) // URI này lấy từ lúc bạn quét máy
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(song.title)
                        .setArtist(song.artist)
                        .build()
                )
                .build()

            // 2. Đưa vào Controller và phát
            controller.setMediaItem(mediaItem)
            controller.prepare()
            controller.play()
            _uiState.update {
                it.copy(
                    currentSong = song,
                    isPlaying = true
                )
            }
        }?: run {
            // Thêm log ở đây để kiểm tra
            println("LỖI: MediaController đang bị NULL, không thể phát nhạc")
        }
    }

    private fun handleRandom() {
        // Ví dụ: musicServiceConnection.playOrPause()
    }

    private fun handleRepeat() {
        // Ví dụ: musicServiceConnection.transportControls.skipToNext()
    }

    private fun handleLike() {
        // Logic quay lại bài trước
    }

    private fun handlePlayPause() {
        // Sử dụng ?.let hoặc kiểm tra null trước khi gọi
        mediaController?.let { controller ->
            if (controller.isPlaying) {
                controller.pause()
            } else {
                controller.play()
            }
        }
    }

    private fun handleNext() {
        // Ví dụ: musicServiceConnection.transportControls.skipToNext()
    }

    private fun handlePrevious() {
        // Logic quay lại bài trước
    }

    private fun handleSeekTo(position: Long) {
        // Logic nhảy đến đoạn nhạc cụ thể
    }

    fun selectSong(song: Song) {
        _uiState.update {
            it.copy(currentSong = song)
        }
    }

}


