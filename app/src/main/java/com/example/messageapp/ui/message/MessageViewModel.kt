package com.example.messageapp.ui.message

import android.app.Application
import android.os.Handler
import android.util.Log
import androidx.lifecycle.*
import com.example.messageapp.MessageRepository
import com.example.messageapp.R
import com.example.messageapp.data.model.MchatMessageList
import com.example.messageapp.data.model.Message
import com.example.messageapp.data.model.MessageList
import com.example.messageapp.data.model.User
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule


class MessageViewModel(application: Application, cookie: String, roomId: Int) :  AndroidViewModel(
    application
) {

    private val messageRepository = MessageRepository.instance

    val messageShowResult: MutableLiveData<MessageShowResult> = MutableLiveData()
    private var timer = Timer()



    init {
        messageShow(cookie, roomId)
    }




    fun messageShow(cookie: String, roomId: Int) {
        Log.d("fun messageShow", "reached")

        viewModelScope.launch {
            try {
                val response = messageRepository.messageShow(cookie, roomId)
                if (response.isSuccessful) {

                    Log.d("messageShow try", "reached")

                    if (response.body() != null) {

                        val mchatMessageList : MchatMessageList = messageConvert(response.body()!!)

                        messageShowResult.value = MessageShowResult(success = mchatMessageList)

                    } else {
                        Log.d("roomResponse", "null")
                    }
                } else {
                    messageShowResult.value = MessageShowResult(error = R.string.getRooms_failed)
                }
            } catch (e: Exception) {
                Log.e("messageShow:Failed", e.toString())
            }
        }

    }



    fun messageSend(cookie: String, roomId: Int, message: String) {


        viewModelScope.launch {
            try {
                val response = messageRepository.messageSend(cookie, roomId, message)
                if (response.isSuccessful) {

                    if (response.body()!!) {
                        Log.d("sending message is successful", message)

                    } else {
                        Log.d("sending message is failed", message)
                    }
                } else {
                    Log.e("messageSend response", "not successful")
                }
            } catch (e: Exception) {
                Log.e("messageSend:Failed", e.toString())
            }


        }
    }


    private val timerTask: TimerTask.() -> Unit = { ->
        viewModelScope.launch {


            try {
                val response = messageRepository.messageShow(cookie, roomId)
                if (response.isSuccessful) {

                    Log.d("messageShow try", "reached")

                    if (response.body() != null) {

                        val mchatMessageList : MchatMessageList = messageConvert(response.body()!!)

                        messageShowResult.value = MessageShowResult(success = mchatMessageList)

                    } else {
                        Log.d("roomResponse", "null")
                    }
                } else {
                    messageShowResult.value = MessageShowResult(error = R.string.getRooms_failed)
                }
            } catch (e: Exception) {
                Log.e("loadProject:Failed", e.toString())
            }
        }

    }


//    fun showMessage() {
//
//        timer = Timer()
//        timer.schedule(0, 30000, timerTask)
//    }


//    private val mHandler = Handler()
//
//
//    val rnb0=object : Runnable{
//        override fun run() {
//
//            viewModelScope.launch {
//
//
//                try {
//                    val response = messageRepository.messageShow(cookie, roomId)
//                    if (response.isSuccessful) {
//
//                        Log.d("messageShow try", "reached")
//
//                        if (response.body() != null) {
//
//                            val mchatMessageList : MchatMessageList = messageConvert(response.body()!!)
//
//                            messageShowResult.value = MessageShowResult(success = mchatMessageList)
//
//                        } else {
//                            Log.d("roomResponse", "null")
//                        }
//                    } else {
//                        messageShowResult.value = MessageShowResult(error = R.string.getRooms_failed)
//                    }
//                } catch (e: Exception) {
//                    Log.e("loadProject:Failed", e.toString())
//                }
//            }
//
//            mHandler.postDelayed(this,30000)
//        }
//    }
//
//    fun messageShow() {
//
//        mHandler.post(rnb0)
//    }




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







    class Factory(
        private val application: Application,
        private val cookie: String,
        private val roomId: Int
    ) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MessageViewModel(application, cookie, roomId) as T
        }
    }

}


