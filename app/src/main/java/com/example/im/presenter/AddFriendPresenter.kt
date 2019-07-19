package com.example.im.presenter

import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.hyphenate.chat.EMClient
import com.example.im.contract.AddFriendContract
import com.example.im.data.AddFriendItem
import com.example.im.data.db.IMDatabase
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread



class AddFriendPresenter(val view: AddFriendContract.View): AddFriendContract.Presenter{


    val addFrienditems = mutableListOf<AddFriendItem>()

    override fun search(key: String) {
        val query = BmobQuery<BmobUser>()
        query.addWhereContains("username", key)
                .addWhereNotEqualTo("username", EMClient.getInstance().currentUser)
        query.findObjects(object : FindListener<BmobUser>() {
            override fun done(p0: MutableList<BmobUser>?, p1: BmobException?) {
                if (p1 == null) {
                    //处理数据
                    //创建AddFriendItem的集合
                    val allContacts = IMDatabase.instance.getAllContacts()
                    doAsync {
                        p0?.forEach {
                            //比对是否已经添加过好友
                            var isAdded = false
                            for (contact in allContacts){
                                if (contact.name == it.username){
                                    isAdded = true
                                }
                            }

                            val addFriendItem = AddFriendItem(it.username, it.createdAt, isAdded)
                            addFrienditems.add(addFriendItem)

                        }
                        uiThread { view.onSearchSuccess() }
                    }
                }
                else view.onSearchFailed()
            }

        })
    }

}