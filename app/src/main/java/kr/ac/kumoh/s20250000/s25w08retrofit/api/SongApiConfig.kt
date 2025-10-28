package kr.ac.kumoh.s20250000.s25w08retrofit.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SongApiConfig {
    const val PROJECT_URL = "https://mcrumlcxwjowtrxztofj.supabase.co"
    const val SERVER_URL = "$PROJECT_URL/rest/v1/"
    const val API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1jcnVtbGN4d2pvd3RyeHp0b2ZqIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjE1Mjc2MTYsImV4cCI6MjA3NzEwMzYxNn0.PNXJ1vYn8m96mj99kTHNYohJzmGP0kGYi8o3Wa0mOa4"

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