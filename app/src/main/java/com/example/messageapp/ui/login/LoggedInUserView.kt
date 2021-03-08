package com.example.messageapp.ui.login

/**
 * User details post authentication that is exposed to the UI
 */
data class LoggedInUserView(
    val displayName: String,
    val cookie: String
    //... other data fields that may be accessible to the UI
)