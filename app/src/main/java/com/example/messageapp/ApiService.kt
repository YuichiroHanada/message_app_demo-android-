package com.example.messageapp

import com.example.messageapp.data.model.CreateReq
import com.example.messageapp.data.model.MessageList
import com.example.messageapp.data.model.MessageListResp
import com.example.messageapp.data.model.Room
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("/user/hello")
    suspend fun hello(): Response<String>

    @POST("/user/create")
    suspend fun create(@Body createReq: CreateReq): Response<Boolean>

    @POST("/user/login")
    suspend fun login(@Query("email") email: String, @Query("pass") pass: String): Response<Void>

    @GET("/user/logout")
    suspend fun logout(): Response<Void>

    @GET("/room/show")
    suspend fun roomShow(@Header("Cookie") userCookie: String): Response<List<Room>>

    @POST("/room/friend")
    suspend fun addFriend(@Header("Cookie") userCookie: String, @Query("address") address: String): Response<Boolean>

    @GET("/message/{roomId}/show")
    suspend fun messageShow(@Header("Cookie") userCookie: String, @Path("roomId") roomId: Int): Response<MessageList>

    @POST("message/{roomId}/send")
    suspend fun messageSend(@Header("Cookie") userCookie: String, @Path("roomId") roomId: Int, @Query("message") message: String): Response<Boolean>


}