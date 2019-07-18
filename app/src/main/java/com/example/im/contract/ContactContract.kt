package com.example.im.contract

/**
 * 黑马程序员
 */
interface ContactContract{

    interface Presenter: BasePresenter{
        fun loadContacts()
    }

    interface View {
        fun onLoadContactsSuccess()
        fun onLoadContactsFailed()
    }
}