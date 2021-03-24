package com.example.messageapp.ui.room


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.messageapp.R
import com.example.messageapp.ui.login.LoginActivity
import com.example.messageapp.ui.message.MessageActivity

class RoomActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)


        if (savedInstanceState == null) {
            //プロジェクト一覧のFragment
            val fragment = RoomListFragment()

            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment, TAG_OF_Room_LIST_FRAGMENT)
                    .commit()
        }
    }


    fun showMessage(roomId: Int, roomName: String, myId: Int, yourId: Int) {

        val intent = Intent(this, MessageActivity::class.java)
        intent.putExtra("roomId", roomId)
        intent.putExtra("roomName", roomName)
        intent.putExtra("myId", myId)
        intent.putExtra("yourId", yourId)
        startActivity(intent)
    }

    fun changeAddFriend() {

        val intent = Intent(this, FriendActivity::class.java)
        startActivity(intent)
    }

    fun logout() {

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

    }
}