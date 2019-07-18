package com.example.im.ui.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.im.R
import com.example.im.adapter.AddFriendListAdapter
import com.example.im.contract.AddFriendContract
import com.example.im.presenter.AddFriendPresenter
import kotlinx.android.synthetic.main.activity_add_friend.*
import kotlinx.android.synthetic.main.header.*
import org.jetbrains.anko.toast

class AddFriendActivity : BaseActivity(), AddFriendContract.View {

    override fun getLayoutResId(): Int = R.layout.activity_add_friend

    val presenter = AddFriendPresenter(this)

    override fun init() {
        super.init()
        headerTitle.text = getString(R.string.add_friend)

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = AddFriendListAdapter(context, presenter.addFrienditems)
        }

        search.setOnClickListener { search() }
        userName.setOnEditorActionListener { _, _, _ ->
            search()
            true
        }
    }

    private fun search() {
        hideSoftKeyboard()
        showProgress(getString(R.string.searching))
        val key = userName.text.trim().toString()
        presenter.search(key)
    }

    override fun onSearchFailed() {
        dismissProgress()
        toast(R.string.search_failed)
    }

    override fun onSearchSuccess() {
        dismissProgress()
        toast(R.string.search_success)
        recyclerView.adapter?.notifyDataSetChanged()
    }
}

