package kr.ac.kumoh.s20250000.s25w08retrofit.api

import kr.ac.kumoh.s20250000.s25w08retrofit.model.Song
import retrofit2.http.GET
import retrofit2.http.Query

interface SongApi {
    @GET("songs")
    suspend fun getSongs(
        @Query("apikey")
        apikey: String = SongApiConfig.API_KEY
    ): List<Song>
}