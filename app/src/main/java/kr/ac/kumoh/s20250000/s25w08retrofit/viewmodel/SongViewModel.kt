package kr.ac.kumoh.s20250000.s25w08retrofit.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.ac.kumoh.s20250000.s25w08retrofit.model.Song
import kr.ac.kumoh.s20250000.s25w08retrofit.repository.SongRepository

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
}