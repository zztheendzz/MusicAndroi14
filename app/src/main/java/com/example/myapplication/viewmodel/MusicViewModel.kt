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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class MusicViewModel @Inject constructor(
    private val repository: MusicRepository,
    private val serviceConnection: MusicServiceConnection
) : ViewModel() {
    init {
        viewModelScope.launch {
            serviceConnection.isConnected.collect { connected ->
                if (connected) {
                    loadSongs()
                    setupPlayerListener()
                }
            }
        }
        viewModelScope.launch {
            while (true) {
                mediaController?.let { controller ->
                    _currentPosition.value = controller.currentPosition
                    _duration.value = controller.duration.coerceAtLeast(0L)
                }
                delay(500) // cập nhật mỗi 0.5s
            }
        }
    }
    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition = _currentPosition.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    val duration = _duration.asStateFlow()
    // Chuyển Flow từ Service thành State để UI dễ dùng
    val isPlaying = serviceConnection.isPlaying
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
                println("Transition to mediaId = $currentSongId")
                val song = _songs.value.find { it.id.toString() == currentSongId }
                println("Found song = $song")
                _uiState.value = _uiState.value.copy(currentSong = song)
                val index = mediaController?.currentMediaItemIndex ?: -1
                println("🔥 Transition fired. New index = $index")
            }
            // Bạn có thể thêm lắng nghe RepeatMode, ShuffleMode ở đây...
        })
    }
    fun loadSongs() {
        viewModelScope.launch {

            // 1️⃣ Load dữ liệu ở IO
            val musicList = withContext(Dispatchers.IO) {
                repository.fetchLocalSongs()
            }

            _songs.value = musicList

            // 2️⃣ Gọi MediaController trên Main thread
            mediaController?.let { controller ->

                val mediaItems = musicList.map { s ->
                    MediaItem.Builder()
                        .setMediaId(s.id.toString())
                        .setUri(s.uri)
                        .build()
                }
                controller.setMediaItems(mediaItems)
                controller.prepare()
            }
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

            val index = _songs.value.indexOfFirst { it.id == song.id }

            println("Play index = $index")

            if (index != -1) {
                controller.seekTo(index, 0L)
                controller.play()
            }
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
        println("Player item count: ${mediaController?.mediaItemCount}")
        println("Current index = ${mediaController?.currentMediaItemIndex}")
        println("Total items = ${mediaController?.mediaItemCount}")
        mediaController?.seekToNextMediaItem()
    }

    private fun handlePrevious() {
        mediaController?.seekToPreviousMediaItem()
    }

    private fun handleSeekTo(position: Long) {
        // Logic nhảy đến đoạn nhạc cụ thể
    }

    fun selectSong(song: Song) {
        _uiState.update {
            it.copy(currentSong = song)
        }
    }
    fun seekTo(position: Long) {
        mediaController?.seekTo(position)
    }
}


