package com.example.im.ui.activity

import com.hyphenate.EMConnectionListener
import com.hyphenate.EMError
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import com.example.im.R
import com.example.im.adapter.EMMessageListenerAdapter
import com.example.im.factory.FragmentFactory
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : BaseActivity() {
    override fun getLayoutResId(): Int = R.layout.activity_main

    val messageListener = object : EMMessageListenerAdapter() {
        override fun onMessageReceived(p0: MutableList<EMMessage>?) {
            runOnUiThread { updateBottomBarUnReadCount() }
        }
    }

    override fun init() {
        super.init()
        println("ccccccccccc")
        bottomBar.setOnTabSelectListener { tabId ->
            val beginTransaction = supportFragmentManager.beginTransaction()
            FragmentFactory.instance.getFragment(tabId)?.let { beginTransaction.replace(R.id.fragment_frame, it) }
            beginTransaction.commit()
        }

        EMClient.getInstance().chatManager().addMessageListener(messageListener)
        EMClient.getInstance().addConnectionListener(object : EMConnectionListener {
            override fun onConnected() {

            }

            override fun onDisconnected(p0: Int) {
                if (p0 == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    //发生多设备登陆，跳转到登陆界面
                    startActivity<LoginActivity>()
                    finish()
                }
            }

        })
    }

    override fun onResume() {
        super.onResume()
        updateBottomBarUnReadCount()
    }

    private fun updateBottomBarUnReadCount() {
        //初始化bottombar未读消息的个数
        val tab = bottomBar.getTabWithId(R.id.tab_conversation)
        tab.setBadgeCount(EMClient.getInstance().chatManager().unreadMessageCount)
    }

    override fun onDestroy() {
        super.onDestroy()
        EMClient.getInstance().chatManager().removeMessageListener(messageListener)
    }

}
