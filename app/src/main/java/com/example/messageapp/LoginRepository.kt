package com.example.messageapp

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginRepository {

    var baseUrl = "http://10.0.2.2:8082"


    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private var apiService: ApiService = retrofit.create(ApiService::class.java)

    //APIにリクエストし、レスポンスをコルーチンで受け取る(一覧)
    suspend fun login(email: String, pass: String): Response<Void> =
        apiService.login(email, pass)

//    //APIにリクエストし、レスポンスをコルーチンで受け取る(詳細)
//    suspend fun getProjectDetails(userID: String, projectName: String): Response<Project> =
//        githubService.getProjectDetails(userID, projectName)

    //singletonでRepositoryインスタンスを返すFactory
    companion object Factory {

        val instance: LoginRepository
            @Synchronized get() {
                return LoginRepository()
            }
    }
}