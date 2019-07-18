package com.example.im.contract

/**
 * 黑马程序员
 */
interface RegisterContract {

    interface Presenter : BasePresenter {
        fun register(userName: String, password: String, confirmPassword: String)
    }

    interface View {
        fun onUserNameError()
        fun onPasswordError()
        fun onConfirmPasswordError()
        fun onStartRegister()
        fun onRegisterSuccess()
        fun onRegisterFailed()
        fun onUserExist()
    }
}