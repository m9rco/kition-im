package com.example.im.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.im.data.AddFriendItem
import com.example.im.widget.AddFriendListItemView


class AddFriendListAdapter(val context: Context, val addFrienditems: MutableList<AddFriendItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val addFriendListItemView = holder?.itemView as AddFriendListItemView
        addFriendListItemView.bindView(addFrienditems[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AddFriendListItemViewHolder(AddFriendListItemView(context))
    }

    override fun getItemCount(): Int = addFrienditems.size

    class AddFriendListItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

    }

}