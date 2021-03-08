package com.example.messageapp

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {

    @GET("/user/hello")
    suspend fun hello(): Response<String>

    @POST("/user/login")
    suspend fun login(@Query("email") email: String, @Query("pass") pass: String): Response<Void>
}