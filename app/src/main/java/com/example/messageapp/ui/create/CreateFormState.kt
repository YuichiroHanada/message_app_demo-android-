package com.example.messageapp.ui.create

data class CreateFormState (val usernameError: Int? = null,
                           val passwordError: Int? = null,
                            val nameError: Int? = null,
                           val isDataValid: Boolean = false)