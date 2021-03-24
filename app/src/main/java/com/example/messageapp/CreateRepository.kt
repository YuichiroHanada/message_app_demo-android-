package com.example.messageapp

import com.example.messageapp.data.model.CreateReq
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CreateRepository {

    var baseUrl = "http://10.0.2.2:8082"


    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private var apiService: ApiService = retrofit.create(ApiService::class.java)


    suspend fun create(createReq: CreateReq): Response<Boolean> = apiService.create(createReq)


    //singletonでRepositoryインスタンスを返すFactory
    companion object Factory {

        val instance: CreateRepository
            @Synchronized get() {
                return CreateRepository()
            }
    }
}