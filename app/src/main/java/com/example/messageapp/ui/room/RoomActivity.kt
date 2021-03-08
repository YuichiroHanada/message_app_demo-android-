package com.example.messageapp.ui.room

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.messageapp.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

val api = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl("http://10.0.2.2:8082")
    .build().create(ApiInterface::class.java)

data class RoomResp(
    val roomId: Int,
    val roomName: String
)

interface ApiInterface {
    @GET("/room/show")
    suspend fun getRoom(@Header("Cookie") userCookie: String): Response<List<RoomResp>>
}

suspend fun getRequest(userCookie: String): Response<List<RoomResp>>? {
    try {
        val response: Response<List<RoomResp>> = api.getRoom(userCookie)
        return response
    } catch (e: Exception) {
        return null
    }
}



class RoomActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        val data = getSharedPreferences("Data", Context.MODE_PRIVATE)

        val cookie = data.getString("cookie", null)


        Log.d("roomActivity ", "start")

        lifecycleScope.launch {

            if (cookie != null) {

                Log.d("roomActivity cookie ", cookie)

                val response = getRequest(cookie)
                if (response != null) {
                    Log.d("response ", response.body().toString())

                } else {
                    Log.d("response null ", "yes")
                }

            } else {

                Log.d("roomActivity cookie null", "yes")
            }


        }
    }
}