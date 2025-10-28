package kr.ac.kumoh.s20250000.s25w08retrofit.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// object SongApiConfig {
object SongApiConfigExample {
    const val PROJECT_URL = "<본인의 Project URL>"
    const val SERVER_URL = "$PROJECT_URL/rest/v1/"
    const val API_KEY = "<본인의 key>"

    // Retrofit Singleton Instance
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // SongApi 인터페이스 구현체
    val songService: SongApi by lazy {
        retrofit.create(SongApi::class.java)
    }
}