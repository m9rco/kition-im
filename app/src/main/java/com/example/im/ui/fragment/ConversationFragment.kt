package com.example.im.ui.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMMessage
import com.example.im.R
import com.example.im.adapter.ConversationListAdapter
import com.example.im.adapter.EMMessageListenerAdapter
import kotlinx.android.synthetic.main.fragment_conversation.*
import kotlinx.android.synthetic.main.header.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class ConversationFragment : BaseFragment() {

    val conversations = mutableListOf<EMConversation>()

    override fun getLayoutResId(): Int = R.layout.fragment_conversation

    val messageListener = object : EMMessageListenerAdapter() {
        override fun onMessageReceived(p0: MutableList<EMMessage>?) {
            loadConversations()
        }
    }

    override fun init() {
        super.init()
        headerTitle.text = getString(R.string.message)

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = ConversationListAdapter(context, conversations)
        }

        EMClient.getInstance().chatManager().addMessageListener(messageListener)

//        loadConversations()
    }

    private fun loadConversations() {
        doAsync {
            //清空会话列表
            conversations.clear()
            val allConversations = EMClient.getInstance().chatManager().allConversations
            conversations.addAll(allConversations.values)
            uiThread { recyclerView.adapter?.notifyDataSetChanged() }
        }
    }

    override fun onResume() {
        super.onResume()
        loadConversations()
    }

    override fun onDestroy() {
        super.onDestroy()
        EMClient.getInstance().chatManager().removeMessageListener(messageListener)
    }
}