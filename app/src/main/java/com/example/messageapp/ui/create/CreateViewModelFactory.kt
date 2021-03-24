package com.example.messageapp.ui.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.messageapp.CreateRepository

class CreateViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateViewModel::class.java)) {
            return CreateViewModel(
                createRepository = CreateRepository(
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}