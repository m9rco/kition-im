package com.example.im.contract

/**
 * 黑马程序员
 */
interface AddFriendContract{

    interface Presenter: BasePresenter{
        fun search(key: String)
    }

    interface View{
        fun onSearchSuccess()
        fun onSearchFailed()
    }
}