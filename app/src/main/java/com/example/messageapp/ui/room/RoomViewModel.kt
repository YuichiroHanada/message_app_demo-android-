package com.example.messageapp.ui.room

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.messageapp.R
import com.example.messageapp.RoomRepository
import com.example.messageapp.ui.login.LoggedInUserView
import com.example.messageapp.ui.login.LoginResult
import kotlinx.coroutines.launch

class RoomViewModel(application: Application, cookie: String):  AndroidViewModel(application) {


    private val roomRepository = RoomRepository.instance

    val roomShowResult: MutableLiveData<RoomShowResult> = MutableLiveData()



    init {
        roomShow(cookie)
    }


    fun roomShow(cookie : String) {
        Log.d("fun roomShow", "reached")

        viewModelScope.launch {
            try {
                val response = roomRepository.roomShow(cookie)
                if (response.isSuccessful) {

                    if (response.body() != null) {

                        roomShowResult.value = RoomShowResult(success = response.body())
                    } else {
                        Log.d("roomResponse", "null")
                    }
                } else {
                    roomShowResult.value = RoomShowResult(error = R.string.getRooms_failed)
                }
            } catch (e: Exception) {
                Log.e("loadProject:Failed", e.stackTrace.toString())
            }
        }

    }


    fun messageShow(roomId: Int) {

    }



    class Factory(private val application: Application, private val cookie: String) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RoomViewModel(application, cookie) as T
        }
    }
}