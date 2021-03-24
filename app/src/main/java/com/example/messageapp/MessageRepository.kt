package com.example.messageapp

import com.example.messageapp.data.model.MessageList
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MessageRepository {


    var baseUrl = "http://10.0.2.2:8082"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private var apiService: ApiService = retrofit.create(ApiService::class.java)

    suspend fun messageShow(userCookie: String, roomId: Int): Response<MessageList> = apiService.messageShow(userCookie, roomId)

    suspend fun messageSend(userCookie: String, roomId: Int, message: String): Response<Boolean> = apiService.messageSend(userCookie, roomId, message)


    //singletonでRepositoryインスタンスを返すFactory
    companion object Factory {

        val instance: MessageRepository
            @Synchronized get() {
                return MessageRepository()
            }
    }
}