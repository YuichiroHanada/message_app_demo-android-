package com.example.messageapp

import android.content.Context
import com.example.messageapp.data.model.Room
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RoomRepository {


    var baseUrl = "http://10.0.2.2:8082"



    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private var apiService: ApiService = retrofit.create(ApiService::class.java)

    //APIにリクエストし、レスポンスをコルーチンで受け取る(一覧)
    suspend fun roomShow(userCookie: String): Response<List<Room>> = apiService.roomShow(userCookie)




    //singletonでRepositoryインスタンスを返すFactory
    companion object Factory {

        val instance: RoomRepository
            @Synchronized get() {
                return RoomRepository()
            }
    }
}