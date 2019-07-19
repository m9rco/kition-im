package com.example.im.ui.activity

import android.os.Handler
import com.example.im.R
import com.example.im.contract.SplashContract
import com.example.im.presenter.SplashPresenter
import org.jetbrains.anko.startActivity

class SplashActivity : BaseActivity(), SplashContract.View {

    val presenter = SplashPresenter(this)

    companion object {
        val DELAY = 1000L
    }

    val handler by lazy {
        Handler()
    }

    override fun init() {
        super.init()
        presenter.checkLoginStatus()
    }

    override fun getLayoutResId(): Int = R.layout.activity_splash

    //延时2s, 跳转到设备不支持
    override fun onNotLoggedIn() {
        handler.postDelayed({
            startActivity<LoginActivity>()
            finish()
        }, DELAY)
    }

    //跳转到主界面
    override fun onLoggedIn() {
        startActivity<MainActivity>()
        finish()
    }

    //延时1s, 跳转到设备不支持
    override fun onNotSupportIn() {
        handler.postDelayed({
            startActivity<NotSupportActivity>()
            finish()
        }, DELAY)
    }
}