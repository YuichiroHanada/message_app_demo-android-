package com.example.messageapp.ui.message

import com.example.messageapp.data.model.MchatMessageList
import com.example.messageapp.data.model.Message
import com.example.messageapp.data.model.MessageList
import com.example.messageapp.data.model.User

class MessageData {

    fun messageConvert(messageList: MessageList) : MchatMessageList {

        val mchatMessageList : MchatMessageList = MchatMessageList()

        val messages : List<Message> = messageList.messageList

        for (message in messages) {


            var mchatMessage : com.github.bassaer.chatmessageview.model.Message = com.github.bassaer.chatmessageview.model.Message()

            var user : User = User(message.sendId, "", null)
            mchatMessage.user = user
            mchatMessage.text = message.message

            mchatMessageList.add(mchatMessage)
        }


        return mchatMessageList
    }
}