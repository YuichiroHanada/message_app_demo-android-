package com.example.messageapp.ui.room

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.messageapp.R
import com.example.messageapp.data.model.Room
import com.example.messageapp.databinding.FragmentRoomListBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

const val TAG_OF_Room_LIST_FRAGMENT = "RoomListFragment"

class RoomListFragment : Fragment() {



//    private val viewModel by lazy { ViewModelProviders.of(this, factory).get(RoomViewModel::class.java) }

    private lateinit var binding: FragmentRoomListBinding
    private lateinit var roomAdapter: RoomShowAdapter



    private val projectClickCallback = object : RoomClickCallback {
        override fun onClick(room: Room) {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED) && activity is RoomActivity) {
//                (activity as RoomActivity).show(project)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_room_list, container, false)

        roomAdapter = RoomShowAdapter(projectClickCallback)

        binding.apply {
            roomList.adapter = roomAdapter
            isLoading = true
        }

        // Inflate the layout for this fragment
        return binding.root
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        val data = activity?.getSharedPreferences("Data", Context.MODE_PRIVATE)


        val cookie = data?.getString("cookie", null)

        if (cookie == null) {
            Log.e("onActivityCreated cookie", "null")
        }


        val factory = RoomViewModel.Factory(
            requireActivity().application, cookie ?: ""
        )

        val viewModel by lazy { ViewModelProviders.of(this, factory).get(RoomViewModel::class.java) }



        observeViewModel(viewModel)
    }

    //observe開始
    private fun observeViewModel(viewModel: RoomViewModel) {

        //データをSTARTED かRESUMED状態である場合にのみ、アップデートするように、LifecycleOwnerを紐付け、ライフサイクル内にオブザーバを追加
        viewModel.roomShowResult.observe(viewLifecycleOwner, Observer { roomShowResult ->
            if (roomShowResult.success != null) {
                binding.isLoading = false
                roomAdapter.setProjectList(roomShowResult.success)
            }
        })
    }






    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RoomListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RoomListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}