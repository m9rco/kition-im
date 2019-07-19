package com.example.im.contract

interface SplashContract {

    interface Presenter : BasePresenter {
        fun checkLoginStatus()//检查登陆状态
    }

    interface View {
        fun onNotLoggedIn() //没有登陆的ui处理
        fun onLoggedIn()     //已经登陆的ui的处理
        fun onNotSupportIn() // 不支持设备
    }
}