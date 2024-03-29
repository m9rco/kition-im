package com.example.im.ui.fragment


import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.hyphenate.chat.EMClient
import com.example.im.R
import com.example.im.adapter.ContactListAdapter
import com.example.im.adapter.EMContactListenerAdapter
import com.example.im.contract.ContactContract
import com.example.im.presenter.ContactPresenter
import com.example.im.ui.activity.AddFriendActivity
import com.example.im.widget.SlideBar
import kotlinx.android.synthetic.main.fragment_contacts.*
import kotlinx.android.synthetic.main.header.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class ContactFragment : BaseFragment(), ContactContract.View {

    override fun getLayoutResId(): Int = R.layout.fragment_contacts

    val presenter = ContactPresenter(this)

    val contactListener = object : EMContactListenerAdapter() {

        override fun onContactDeleted(p0: String?) {
            //重新获取联系人数据
            presenter.loadContacts()
        }

        override fun onContactAdded(p0: String?) {
            //重新获取联系人数据
            presenter.loadContacts()
        }
    }


    override fun init() {
        super.init()
        initHeader()
        initSwipeRefreshLayout()
        initRecyclerView()
        EMClient.getInstance().contactManager().setContactListener(contactListener)
        initSlideBar()
        presenter.loadContacts()
    }

    private fun initSlideBar() {
        slideBar.onSectionChangeListener = object : SlideBar.OnSectionChangeListener {

            override fun onSectionChange(firstLetter: String) {
                section.visibility = View.VISIBLE
                section.text = firstLetter
                recyclerView.smoothScrollToPosition(getPosition(firstLetter))
            }

            override fun onSlideFinish() {
                section.visibility = View.GONE
            }

        }
    }

    private fun initRecyclerView() {
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = ContactListAdapter(context, presenter.contactListItems)
        }
    }

    private fun initSwipeRefreshLayout() {
        swipeRefreshLayout.apply {
            setColorSchemeResources(R.color.qq_blue)
            isRefreshing = true
            setOnRefreshListener { presenter.loadContacts() }
        }
    }

    private fun initHeader() {
        headerTitle.text = getString(R.string.contact)
        add.visibility = View.VISIBLE
        add.setOnClickListener { context?.startActivity<AddFriendActivity>() }
    }

    private fun getPosition(firstLetter: String): Int =
        presenter.contactListItems.binarySearch { contactListItem ->
            contactListItem.firstLetter.minus(firstLetter[0])
        }


    override fun onLoadContactsFailed() {
        swipeRefreshLayout.isRefreshing = false
        context?.toast(R.string.load_contacts_failed)
    }

    override fun onLoadContactsSuccess() {
        swipeRefreshLayout.isRefreshing = false
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        EMClient.getInstance().contactManager().removeContactListener(contactListener)
    }
}