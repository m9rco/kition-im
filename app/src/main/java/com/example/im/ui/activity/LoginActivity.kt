package com.example.im.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.example.im.R
import com.example.im.contract.LoginContract
import com.example.im.presenter.LoginPresenter
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class LoginActivity : BaseActivity(), LoginContract.View {

    val presenter = LoginPresenter(this)

    override fun init() {
        super.init()
        newUser.setOnClickListener { startActivity<RegisterActivity>() }
        login.setOnClickListener { login() }
        password.setOnEditorActionListener { p0, p1, p2 ->
            login()
            true
        }
    }

    private fun login() {
        //隐藏软键盘
        hideSoftKeyboard()
        if (hasWriteExternalStoragePermission()) {
            val userNameString = userName.text.trim().toString()
            val passwordString = password.text.trim().toString()
            presenter.login(userNameString, passwordString)
        } else applyWriteExteranlStoragePermissino()
    }

    private fun applyWriteExteranlStoragePermissino() {
        val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(this, permissions, 0)
    }

    //检查是否有写磁盘的权限
    private fun hasWriteExternalStoragePermission(): Boolean {
        val result = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //用户同意权限,开始登陆
            login()
        } else toast(R.string.permission_denied)
    }

    override fun onUserNameError() {
        userName.error = getString(R.string.user_name_error)
    }

    override fun onPasswordError() {
        password.error = getString(R.string.password_error)
    }

    override fun onStartLogin() {
        //弹出进度条
        showProgress(getString(R.string.logging))

    }

    override fun onLoggedInSuccess() {
        //隐藏进度条
        dismissProgress()
        //进入主界面
        startActivity<MainActivity>()
        //退出LoginActivity
        finish()
    }

    override fun onLoggedInFailed() {
        //隐藏进度条
        dismissProgress()
        //弹出toast
        toast(R.string.login_failed)
    }

    override fun getLayoutResId(): Int = R.layout.activity_login

}