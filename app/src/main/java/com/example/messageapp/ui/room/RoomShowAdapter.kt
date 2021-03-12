package com.example.messageapp.ui.room

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.messageapp.R
import com.example.messageapp.data.model.Room
import com.example.messageapp.databinding.RoomRecyclerViewBinding

class RoomShowAdapter(private val roomClickCallback: RoomClickCallback?) : RecyclerView.Adapter<RoomShowAdapter.RoomViewHolder>() {


    private var roomList: List<Room>? = null


    open class RoomViewHolder(val binding: RoomRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root)


    fun setProjectList(roomList: List<Room>) {

        if (this.roomList == null) {
            this.roomList = roomList
            notifyItemRangeInserted(0, roomList.size)
        } else {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return requireNotNull(this@RoomShowAdapter.roomList).size
                }

                override fun getNewListSize(): Int {
                    return roomList.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val oldList = this@RoomShowAdapter.roomList
                    return oldList?.get(oldItemPosition)?.roomId == roomList[newItemPosition].roomId
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val project = roomList[newItemPosition]
                    val old = roomList[oldItemPosition]
                    return project.roomId == old.roomId
                }
            })
            this.roomList = roomList
            result.dispatchUpdatesTo(this)
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {

        val binding =
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.room_recycler_view, parent,
                        false) as RoomRecyclerViewBinding

        binding.callback = roomClickCallback

        return RoomViewHolder(binding)

    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.binding.room = roomList?.get(position)
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return roomList?.size ?: 0
    }
}