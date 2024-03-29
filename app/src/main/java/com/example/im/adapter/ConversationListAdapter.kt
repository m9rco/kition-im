package com.example.im.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hyphenate.chat.EMConversation
import com.example.im.ui.activity.ChatActivity
import com.example.im.widget.ConversationListItemView
import org.jetbrains.anko.startActivity


class ConversationListAdapter(val context: Context, val conversations: MutableList<EMConversation>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val conversationListItemView = holder?.itemView as ConversationListItemView
        conversationListItemView.bindView(conversations[position])
        conversationListItemView.setOnClickListener {
            context.startActivity<ChatActivity>(
                "username" to conversations[position].conversationId()
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ConversationListItemViewHolder(ConversationListItemView(context))
    }

    override fun getItemCount(): Int = conversations.size

    class ConversationListItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    }

}