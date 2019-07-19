package com.example.im.presenter

import com.hyphenate.chat.EMClient
import com.hyphenate.exceptions.HyphenateException
import com.example.im.contract.ContactContract
import com.example.im.data.ContactListItem
import com.example.im.data.db.Contact
import com.example.im.data.db.IMDatabase
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class ContactPresenter(val view: ContactContract.View): ContactContract.Presenter{

    val contactListItems = mutableListOf<ContactListItem>()

    override fun loadContacts() {
        doAsync {
            //再次加载数据，先清空集合
            contactListItems.clear()
            //清空数据库
            IMDatabase.instance.deleteAllContacts()
            try {
                val usernames = EMClient.getInstance().contactManager().allContactsFromServer
                //根据首字符排序
                usernames.sortBy { it[0] }
                usernames.forEachIndexed { index, s ->
                    //判断是否显示首字符
                    val showFirstLetter = index == 0 || s[0] != usernames[index-1][0]
                    val contactListItem = ContactListItem(s, s[0].toUpperCase(), showFirstLetter)
                    contactListItems.add(contactListItem)

                    val contact = Contact(mutableMapOf("name" to s))
                    IMDatabase.instance.saveContact(contact)
                }
                uiThread { view.onLoadContactsSuccess() }
            }catch (e: HyphenateException) {
                uiThread { view.onLoadContactsFailed() }
            }
        }

    }

}