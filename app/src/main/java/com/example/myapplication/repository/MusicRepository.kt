package com.example.myapplication.repository

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.example.myapplication.data.Song
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

class MusicRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun fetchLocalSongs(): List<Song> {
        val songList = mutableListOf<Song>()
        android.util.Log.d("MusicDebug", "Bắt đầu quét nhạc...")
        // Chỉ quét các file nhạc có thời lượng > 30 giây để loại bỏ âm báo, tin nhắn
        val selection = "${MediaStore.Audio.Media.DURATION} >= ?"
        val selectionArgs = arrayOf("30000")

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM_ID
        )

        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            "${MediaStore.Audio.Media.TITLE} ASC"
        )

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val albumIdColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val title = it.getString(titleColumn)
                val artist = it.getString(artistColumn)
                val duration = it.getInt(durationColumn)
                val albumId = it.getLong(albumIdColumn)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id
                )

                songList.add(Song(id, title, artist, duration, contentUri, albumId))
            }
        }
        return songList
    }
}