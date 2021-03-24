package com.example.messageapp.ui.message

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.messageapp.R
import com.example.messageapp.data.model.MchatMessageList
import com.example.messageapp.data.model.User
import com.example.messageapp.ui.room.RoomActivity
import com.github.bassaer.chatmessageview.model.Message
import com.github.bassaer.chatmessageview.model.Message.*
import com.github.bassaer.chatmessageview.model.Message.Companion.STATUS_ICON
import com.github.bassaer.chatmessageview.util.ChatBot
import com.github.bassaer.chatmessageview.view.ChatView
import com.github.bassaer.chatmessageview.view.MessageView
import java.io.IOException
import java.util.*
import kotlin.concurrent.schedule


class MessageActivity : AppCompatActivity() {


    val RIGHT_BUBBLE_COLOR = R.color.colorPrimaryDark
    val LEFT_BUBBLE_COLOR = R.color.gray300
    val BACKGROUND_COLOR: Int = R.color.blueGray300
    val SEND_BUTTON_COLOR = R.color.blueGray500
    val SEND_ICON: Int = R.drawable.ic_action_send
    val OPTION_BUTTON_COLOR = R.color.teal500
    val RIGHT_MESSAGE_TEXT_COLOR = Color.WHITE
    val LEFT_MESSAGE_TEXT_COLOR = Color.BLACK
    val USERNAME_TEXT_COLOR = Color.WHITE
    val SEND_TIME_TEXT_COLOR = Color.WHITE
    val DATA_SEPARATOR_COLOR = Color.WHITE
    val MESSAGE_STATUS_TEXT_COLOR = Color.WHITE
    val INPUT_TEXT_HINT = "New message.."
    val MESSAGE_MARGIN = 5


    private var mChatView: ChatView? = null
    private var mMessageList: MchatMessageList? = null
    private var mUsers: ArrayList<User>? = null
    private var myUserId: Int? = null
    private var yourUserId: Int? = null
    private var roomName: String? = null
    private var messages: MutableList<Message>? = null
    private var mReplyDelay = -1
    private val READ_REQUEST_CODE = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        val roomId = intent.getIntExtra("roomId", 0)
        roomName = intent.getStringExtra("roomName")
        myUserId = intent.getIntExtra("myId", 0)
        yourUserId = intent.getIntExtra("yourId", 0)

        val mChatView = findViewById<ChatView>(R.id.chat_view)
        val nameBarView = findViewById<TextView>(R.id.nameBar)
        val backRooms = findViewById<Button>(R.id.backRooms)
        val reload = findViewById<Button>(R.id.reload)
        nameBarView.text = roomName

        val data = getSharedPreferences("Data", Context.MODE_PRIVATE)

        val cookie = data?.getString("cookie", null)

        if (cookie == null) {
            Log.e("MessageActivityOnCreated cookie", "null")
        }


        val factory = cookie?.let {
            MessageViewModel.Factory(
                application, it, (roomId ?: "") as Int
            )
        }

        val viewModel by lazy { ViewModelProviders.of(this, factory).get(MessageViewModel::class.java) }

//        Timer().schedule(30000, 30000) {
//            Log.d("timer", "callback")
//            if (cookie != null) {
//                viewModel.messageShow(cookie, roomId)
//            }
//
//        }


        viewModel.messageShowResult.observe(this@MessageActivity, Observer {
            val messageShowResult = it ?: return@Observer


            if (messageShowResult.success != null) {

                val resp = messageShowResult.success
                Log.d("messageshowreult", "success reached")
                resp[1].text?.let { it1 -> Log.d("resp 0 text", it1) }

                mMessageList = resp

                messages = loadMessages(mMessageList!!)

                val messageView: MessageView? = mChatView?.getMessageView()


                if (messages == null) {
                    Log.d("messages null", "yes")
                }

                messages?.let { messageView?.init(it) }
                messageView?.setSelection(messageView.count - 1)




                if (mMessageList == null) {
                    Log.d("mMessageessagelist", "null")
                }
            }
        })





        initUsers()





        //Load saved messages

        //Load saved messages
//        loadMessages()

        //Set UI parameters if you need

        //Set UI parameters if you need
        mChatView.setRightBubbleColor(
            ContextCompat.getColor(
                this,
                RIGHT_BUBBLE_COLOR
            )
        )
        mChatView.setLeftBubbleColor(
            ContextCompat.getColor(
                this,
                LEFT_BUBBLE_COLOR
            )
        )
        mChatView.setBackgroundColor(
            ContextCompat.getColor(
                this,
                BACKGROUND_COLOR
            )
        )
        mChatView.setSendButtonColor(
            ContextCompat.getColor(
                this,
                SEND_BUTTON_COLOR
            )
        )
        mChatView.setSendIcon(SEND_ICON)
        mChatView.setOptionIcon(R.drawable.ic_account_circle)
        mChatView.setOptionButtonColor(OPTION_BUTTON_COLOR)
        mChatView.setRightMessageTextColor(RIGHT_MESSAGE_TEXT_COLOR)
        mChatView.setLeftMessageTextColor(LEFT_MESSAGE_TEXT_COLOR)
        mChatView.setUsernameTextColor(USERNAME_TEXT_COLOR)
        mChatView.setSendTimeTextColor(SEND_TIME_TEXT_COLOR)
        mChatView.setDateSeparatorColor(DATA_SEPARATOR_COLOR)
        mChatView.setMessageStatusTextColor(MESSAGE_STATUS_TEXT_COLOR)
        mChatView.setInputTextHint(INPUT_TEXT_HINT)
        mChatView.setMessageMarginTop(MESSAGE_MARGIN)
        mChatView.setMessageMarginBottom(MESSAGE_MARGIN)
        mChatView.setMaxInputLine(5)
        mChatView.setUsernameFontSize(resources.getDimension(R.dimen.font_small))
        mChatView.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        mChatView.inputTextColor = ContextCompat.getColor(this, R.color.red500)
        mChatView.setInputTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)


        mChatView.setOnBubbleClickListener(object : OnBubbleClickListener {
            override fun onClick(message: Message) {
                mChatView.updateMessageStatus(message, MyMessageStatusFormatter.STATUS_SEEN)
                Toast.makeText(
                    this@MessageActivity,
                    "click : " + message.user.getName() + " - " + message.text,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        mChatView.setOnIconClickListener(object : OnIconClickListener {
            override fun onIconClick(message: Message) {
                Toast.makeText(
                    this@MessageActivity,
                    "click : icon " + message.user.getName(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        mChatView.setOnIconLongClickListener(object : OnIconLongClickListener {
            override fun onIconLongClick(message: Message) {
                Toast.makeText(
                    this@MessageActivity,
                    """
                Removed this message 
                ${message.text}
                """.trimIndent(),
                    Toast.LENGTH_SHORT
                ).show()
                mChatView.getMessageView().remove(message)
            }
        })

        //Click Send Button

        //Click Send Button
        mChatView.setOnClickSendButtonListener(View.OnClickListener {
            initUsers()
            //new message
            val message = Builder()
                .setUser(mUsers!![0])
                .setRight(true)
                .setText(mChatView.inputText)
                .hideIcon(true)
                .setStatusIconFormatter(MyMessageStatusFormatter(this@MessageActivity))
                .setStatusTextFormatter(MyMessageStatusFormatter(this@MessageActivity))
                .setStatusStyle(STATUS_ICON)
                .setStatus(MyMessageStatusFormatter.STATUS_DELIVERED)
                .build()

            //Set to chat view
            mChatView.send(message)
            //Add message list
            mMessageList?.add(message)

            if (cookie != null) {
                viewModel.messageSend(cookie, roomId, mChatView.inputText)
            }

            //Reset edit text
            mChatView.inputText = ""
//            message.text?.let { it1 -> receiveMessage(it1) }
        })

        //Click option button

        //Click option button
        mChatView.setOnClickOptionButtonListener(View.OnClickListener { showDialog() })

        backRooms.setOnClickListener {

            val intent = Intent(this, RoomActivity::class.java)
            startActivity(intent)

        }

        reload.setOnClickListener {

            finish()
            startActivity(intent)
        }




    }




    private fun openGallery() {
        val intent: Intent
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            intent = Intent(Intent.ACTION_GET_CONTENT)
        } else {
            intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
        }
        intent.type = "image/*"
        startActivityForResult(
            intent,
            READ_REQUEST_CODE
        )
    }

    private fun receiveMessage(sendText: String) {
        //Ignore hey
        if (!sendText.contains("hey")) {

            //Receive message
            val receivedMessage = mUsers?.get(1)?.let {
                Message.Builder()
                    .setUser(it)
                    .setRight(false)
                    .setText(ChatBot.talk(mUsers?.get(0)?.getName(), sendText))
                    .setStatusIconFormatter(MyMessageStatusFormatter(this@MessageActivity))
                    .setStatusTextFormatter(MyMessageStatusFormatter(this@MessageActivity))
                    .setStatusStyle(STATUS_ICON)
                    .setStatus(MyMessageStatusFormatter.STATUS_DELIVERED)
                    .build()
            }
            if (sendText == Message.Type.PICTURE.name) {
                if (receivedMessage != null) {
                    receivedMessage.text = "Nice!"
                }
            }

            // This is a demo bot
            // Return within 3 seconds
            if (mReplyDelay < 0) {
                mReplyDelay = (Random().nextInt(4) + 1) * 1000
            }
            Handler().postDelayed({
                if (receivedMessage != null) {
                    mChatView?.receive(receivedMessage)
                }
                //Add message list
                if (receivedMessage != null) {
                    mMessageList?.add(receivedMessage)
                }
            }, mReplyDelay.toLong())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != READ_REQUEST_CODE || resultCode != Activity.RESULT_OK || data == null) {
            return
        }
        val uri = data.data
        try {
            val picture = MediaStore.Images.Media.getBitmap(getContentResolver(), uri)
            val message = mUsers?.get(0)?.let {
                Message.Builder()
                    .setRight(true)
                    .setText(Message.Type.PICTURE.name)
                    .setUser(it)
                    .hideIcon(true)
                    .setPicture(picture)
                    .setType(Message.Type.PICTURE)
                    .setStatusIconFormatter(MyMessageStatusFormatter(this@MessageActivity))
                    .setStatusStyle(STATUS_ICON)
                    .setStatus(MyMessageStatusFormatter.STATUS_DELIVERED)
                    .build()
            }
            if (message != null) {
                mChatView?.send(message)
            }
            //Add message list
            if (message != null) {
                mMessageList?.add(message)
            }
            receiveMessage(Message.Type.PICTURE.name)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
        }
    }

    private fun initUsers() {
        mUsers = ArrayList<User>()
        //User id
        val myId = myUserId

        if (myId == 0) {
            Log.d("initUsers myId", "0")
        }
        //User icon
        val myIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_account_circle)
        //User name
        val myName = "you"
        val yourId = yourUserId

        if (yourId == 0) {
            Log.d("initUsers yourId", "0")
        }

        val yourIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_account_circle)
        val yourName = roomName
        val me = myId?.let { User(it, myName, myIcon) }
        val you = yourId?.let { User(it, yourName, yourIcon) }
        if (me != null) {
            mUsers!!.add(me)
        }
        if (you != null) {
            mUsers!!.add(you)
        }
    }

    /**
     * Load saved messages
     */
    private fun loadMessages(messageList: MchatMessageList) : MutableList<Message>{
        val messages: MutableList<Message> = ArrayList()
//        mMessageList = AppData.getMessageList(this)
        if (mMessageList == null) {
            Log.d("loadMessages messagelist", "null")
            mMessageList = MchatMessageList()
        } else {
            for (i in 0 until mMessageList!!.size()) {
                val message: Message = mMessageList!!.get(i)
                //Set extra info because they were removed before save messages.
                for (user in mUsers!!) {
                    if (message.user.getId() == user.getId()) {
                        message.user.setIcon(user.getIcon()!!)
                    }
                }

                if (message.user.getId() == mUsers!![0].getId()) {
                    message.isRight = true
                }

                if (!message.isDateCell && message.isRight) {
                    message.hideIcon(true)
                }
                message.statusStyle = Companion.STATUS_ICON_RIGHT_ONLY
                message.statusIconFormatter = MyMessageStatusFormatter(this)
                message.status = MyMessageStatusFormatter.STATUS_DELIVERED
                messages.add(message)
            }
        }
        val messageView: MessageView? = mChatView?.getMessageView()
        messageView?.init(messages)
        messageView?.setSelection(messageView.count - 1)
        return messages
    }

    override fun onResume() {
        super.onResume()
        initUsers()
    }

    override fun onPause() {
        super.onPause()
        //Save message
//        mMessageList = MchatMessageList()
//        mMessageList!!.messages = mChatView?.getMessageView()?.messageList!!
//        AppData.putMessageList(this, mMessageList)
    }

    fun getUsers(): ArrayList<User>? {
        return mUsers
    }


    fun setReplyDelay(replyDelay: Int) {
        mReplyDelay = replyDelay
    }

    private fun showDialog() {
        val items = arrayOf<String>(
            getString(R.string.send_picture),
            getString(R.string.clear_messages)
        )
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.options))
            .setItems(items, DialogInterface.OnClickListener { dialogInterface, position ->
                when (position) {
                    0 -> openGallery()
                    1 -> mChatView?.getMessageView()?.removeAll()
                }
            })
            .show()
    }

}