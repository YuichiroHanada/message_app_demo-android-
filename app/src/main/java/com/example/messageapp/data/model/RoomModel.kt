package com.example.messageapp.data.model

import org.json.JSONObject

class RoomModel(val room: Room) {
}


data class Room(private val recordJSON: JSONObject){

    val roomId: Int = recordJSON.getInt("roomId")
    val roomName: String = recordJSON.getString("roomName")
    val myId: Int = recordJSON.getInt("myId")
    val yourId: Int = recordJSON.getInt("yourId")
}