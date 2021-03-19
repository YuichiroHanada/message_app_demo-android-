package com.example.messageapp.data.model

import com.example.messageapp.ui.login.LoggedInUserView
import org.json.JSONArray
import org.json.JSONObject


data class Message(private val recordJSON: JSONObject) {
    val id: Int = recordJSON.getInt("id")
    val message: String = recordJSON.getString("message")
    val roomId: Int = recordJSON.getInt("roomId")
    val sendId: Int = recordJSON.getInt("sendId")


}

data class MessageListResp(private val recordJSON: JSONObject) {
    val userId: Int = recordJSON.getInt("userId")
    val messages: JSONArray = recordJSON.getJSONArray("messageList")
}

data class MessageList(
    val userId: Int,
    val messageList: List<Message>
)

data class MchatMessageListResp (
    var userId: Int,
    val mchatMessageList: MchatMessageList
)
