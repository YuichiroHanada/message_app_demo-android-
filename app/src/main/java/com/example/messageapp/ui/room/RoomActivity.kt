package com.example.messageapp.ui.room

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.messageapp.R
import androidx.recyclerview.widget.RecyclerView

class RoomActivity : AppCompatActivity() {

//    private lateinit var viewModel: RoomViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

//        val data = getSharedPreferences("Data", Context.MODE_PRIVATE)
//
//        val cookie = data.getString("cookie", null)

//        val recyclerView = findViewById<RecyclerView>(R.id.rooms)

//        viewModel = ViewModelProvider(this, RoomViewModelFactory())
//            .get(RoomViewModel::class.java)
//
//        if (cookie != null) {
//            viewModel.roomShow(cookie)
//
//        } else {
//            Log.d("roomActivity cookie", "null")
//        }


        if (savedInstanceState == null) {
            //プロジェクト一覧のFragment
            val fragment = RoomListFragment()

            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment, TAG_OF_Room_LIST_FRAGMENT)
                    .commit()
        }
    }





//        val binding = DataBindingUtil.setContentView<ActivityRoomBinding>(this, R.layout.activity_room)
//        binding.viewModel = viewModel
//        binding.lifecycleOwner = this
//
//
//        recyclerView.layoutManager = LinearLayoutManager(this)
////ここでadapterを作成するときにlifecycleOwnerを渡す！
//        val adapter = RoomAdapter(viewModel, this)
//
//        recyclerView.adapter = adapter

}