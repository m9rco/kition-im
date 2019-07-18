package com.example.im.contract

import com.hyphenate.chat.EMMessage

/**
 * 黑马程序员
 */
interface ChatContract{

    interface Presenter: BasePresenter{
        fun sendMessage(contact: String, message: String)
        fun addMessage(username: String, p0: MutableList<EMMessage>?)
        fun loadMessages(username: String)
        fun loadMoreMessages(username: String)
    }

    interface View {
        fun onStartSendMessage()
        fun onSendMessageSuccess()
        fun onSendMessageFailed()
        fun onMessageLoaded()
        fun onMoreMessageLoaded(size: Int)
    }
}