package com.example.im.contract

interface LoginContract {

    interface Presenter: BasePresenter {

        fun login(userName: String, password: String)

    }

    interface View  {
        fun onUserNameError()
        fun onPasswordError()
        fun onStartLogin()
        fun onLoggedInSuccess()
        fun onLoggedInFailed()
    }
}