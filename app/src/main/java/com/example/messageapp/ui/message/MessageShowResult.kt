package com.example.messageapp.ui.message

import com.example.messageapp.data.model.MchatMessageList
import com.example.messageapp.data.model.MessageList
import com.example.messageapp.data.model.MessageListResp


data class MessageShowResult (

    val success: MchatMessageList?= null,
    val error:Int? = null
)