package com.example.messageapp.data.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.github.bassaer.chatmessageview.model.IChatUser
import com.github.bassaer.chatmessageview.model.Message
import java.io.ByteArrayOutputStream
import java.util.*

class MchatMessageList {
    private val mMessages: MutableList<SaveMessage>
    var messages: ArrayList<Message>
        get() {
            val messages = ArrayList<Message>()
            for (saveMessage in mMessages) {
                messages.add(convertMessage(saveMessage))
            }
            return messages
        }
        set(messages) {
            for (message in messages) {
                mMessages.add(convertMessage(message))
            }
        }

    fun add(message: Message) {
        mMessages.add(convertMessage(message))
    }

    operator fun get(index: Int): Message {
        return convertMessage(mMessages[index])
    }

    fun get(): List<Message> {
        val list: MutableList<Message> = ArrayList()
        for (message in mMessages) {
            list.add(convertMessage(message))
        }
        return list
    }

    fun size(): Int {
        return mMessages.size
    }

    private fun convertMessage(message: Message): SaveMessage {
        val saveMessage = SaveMessage(
            Integer.valueOf(message.user.getId()),
            message.user.getName(),
            message.text,
            message.sendTime,
            message.isRight
        )
        saveMessage.type = message.type
        if (message.type == Message.Type.PICTURE
            && message.picture != null
        ) {
            val outputStream = ByteArrayOutputStream()
            message.picture!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            saveMessage.pictureString = Base64.encodeToString(
                outputStream.toByteArray(),
                Base64.DEFAULT
            )
        }
        return saveMessage
    }

    private fun convertMessage(saveMessage: SaveMessage): Message {
        val user: IChatUser = User(saveMessage.id, saveMessage.username, null)
        val message = Message.Builder()
            .setUser(user)
            .setText(saveMessage.content!!)
            .setRight(saveMessage.isRightMessage)
            .setSendTime(saveMessage.createdAt)
            .setType(saveMessage.type!!)
            .build()
        if (saveMessage.pictureString != null) {
            val bytes = Base64.decode(saveMessage.pictureString!!.toByteArray(), Base64.DEFAULT)
            message.picture = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }
        return message
    }

    private inner class SaveMessage(
        val id: Int,
        val username: String?,
        val content: String?,
        val createdAt: Calendar,
        val isRightMessage: Boolean
    ) {
        var pictureString: String? = null
        var type: Message.Type? = null

    }

    init {
        mMessages = ArrayList()
    }
}
