package kr.ac.kumoh.s20250000.s25w08retrofit.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.ac.kumoh.s20250000.s25w08retrofit.model.Song
import kr.ac.kumoh.s20250000.s25w08retrofit.repository.SongRepository
import java.util.UUID

class SongViewModel(
    private val repository: SongRepository = SongRepository()
) : ViewModel() {
    private val _songList = MutableStateFlow<List<Song>>(emptyList())
    val songList = _songList.asStateFlow()
//    val songList: StateFlow<List<Song>>
//        get() = _songList

    init {
        fetchSongs()
    }

    private fun fetchSongs() {
        viewModelScope.launch {
            try {
                val response = repository.getSongs()
                _songList.value = response
                //Log.i("fetchSongs()", response.toString())
            } catch(e: Exception) {
                Log.e("fetchSongs()", e.toString())
            }
        }
    }

    fun findSong(id: String) = _songList.value.find { it.id == id }

    fun addSong(title: String, singer: String, rating: Int, lyrics: String?) {
//        val newSong = Song(
//            id = UUID.randomUUID().toString(),
//            title = "test",
//            singer = "test",
//            rating = 5,
//            lyrics = null
//        )

        val newSong = Song(
            id = UUID.randomUUID().toString(),
            title = title,
            singer = singer,
            rating = rating,
            lyrics = lyrics
        )

        //Log.i("addSong()", newSong.toString())

        viewModelScope.launch {
            try {
                repository.addSong(newSong)
                _songList.value = _songList.value + newSong
            } catch(e: Exception) {
                Log.e("addSong()", e.toString())
            }
        }
    }

    fun deleteSong(songId: String) {
        viewModelScope.launch {
            try {
                repository.deleteSong(songId)
                _songList.value = _songList.value.filter { it.id != songId }
            } catch(e: Exception) {
                Log.e("deleteSong()", e.toString())
            }
        }
    }
}