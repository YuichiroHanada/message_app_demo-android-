package com.example.messageapp.ui.room

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.messageapp.R

class FriendActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend)

        val username = findViewById<EditText>(R.id.account)
        val add = findViewById<Button>(R.id.add)
        val loading = findViewById<ProgressBar>(R.id.loading)


        val data = getSharedPreferences("Data", Context.MODE_PRIVATE)

        val cookie = data?.getString("cookie", null)

        if (cookie == null) {
            Log.e("onActivityCreated cookie", "null")
        }

        val factory = RoomViewModel.Factory(
            application, cookie ?: ""
        )

        val viewModel by lazy { ViewModelProviders.of(this, factory).get(RoomViewModel::class.java) }


        viewModel.addFriendResult.observe(this@FriendActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)

            }
            if (loginResult.success != null) {


                addFriendSuccessful(username.text.toString())


            }

            val intent = Intent(this, RoomActivity::class.java)
            startActivity(intent)

            setResult(RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })



        add.setOnClickListener {

            if (cookie != null) {
                viewModel.addFriend(cookie, username.text.toString())
            }

            val intent = Intent(this, RoomActivity::class.java)
            startActivity(intent)

        }
    }


    private fun addFriendSuccessful(name: String) {
        val text = name + R.string.add_success

        Toast.makeText(
            applicationContext,
            text,
            Toast.LENGTH_LONG
        ).show()

    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}