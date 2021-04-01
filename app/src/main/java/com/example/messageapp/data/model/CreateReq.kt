package com.example.messageapp.data.model

import org.json.JSONObject

data class CreateReq(

    val account: String,
    val password: String,
    val name: String
)