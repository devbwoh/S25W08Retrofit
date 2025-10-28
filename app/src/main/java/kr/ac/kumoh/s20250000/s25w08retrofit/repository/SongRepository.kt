package kr.ac.kumoh.s20250000.s25w08retrofit.repository

import kr.ac.kumoh.s20250000.s25w08retrofit.api.SongApiConfig
import kr.ac.kumoh.s20250000.s25w08retrofit.model.Song

class SongRepository {
    private val songApi = SongApiConfig.songService

    suspend fun getSongs(): List<Song> {
        return songApi.getSongs()
    }
}
