package com.example.messageapp.ui.room

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.messageapp.R
import com.example.messageapp.RoomRepository
import com.example.messageapp.data.Event
import kotlinx.coroutines.launch

class RoomViewModel(application: Application, cookie: String):  AndroidViewModel(application) {


    private val roomRepository = RoomRepository.instance

    val roomShowResult: MutableLiveData<RoomShowResult> = MutableLiveData()

    val addFriendResult: MutableLiveData<AddFriendResult> = MutableLiveData()

    val onAdd = MutableLiveData<Event<String>>()

    val onLogout = MutableLiveData<Event<String>>()



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
                Log.e("roomShow:Failed", e.stackTrace.toString())
            }
        }

    }


    fun addFriend(cookie: String, address: String) {


        viewModelScope.launch {
            try {
                val response = roomRepository.addFriend(cookie, address)
                if (response.isSuccessful) {

                    if (response.body()!!) {
                        addFriendResult.value = AddFriendResult(success = response.body())

                    } else {
                        addFriendResult.value = AddFriendResult(error = R.string.addFriend_failed)
                        Log.d("roomResponse", "null")
                    }
                } else {
                    addFriendResult.value = AddFriendResult(error = R.string.connection_failed)
                }
            } catch (e: Exception) {
                Log.e("addFriend:Failed", e.stackTrace.toString())
            }
        }
    }

    fun addOnClick() {
        onAdd.value = Event("onAdd")
    }

    fun logoutOnClick() {
        onLogout.value = Event("onLogout")

        viewModelScope.launch {
            try {
                val response = roomRepository.logout()
                if (response.isSuccessful) {
                    Log.d("logout connection", "success")
                } else {
                  Log.e("logout connection", "failed")
                }
            } catch (e: Exception) {
                Log.e("loadProject:Failed", e.stackTrace.toString())
            }
        }

    }



    class Factory(private val application: Application, private val cookie: String) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RoomViewModel(application, cookie) as T
        }
    }
}