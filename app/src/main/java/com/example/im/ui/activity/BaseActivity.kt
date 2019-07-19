package com.example.im.ui.activity

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import pub.devrel.easypermissions.EasyPermissions


abstract class BaseActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

    private val progressDialog by lazy {
        ProgressDialog(this)
    }

    private val inputMethodManager by lazy {
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    open fun init() {
        //初始化一些公共的功能，比如摇一摇，子类也可以覆写该方法，完成自己的初始化
//        if (!hasReadPhoneStatePermission()) {
//            applyReadPhoneStatePermissino()
//        }
    }

    private fun hasReadPhoneStatePermission(): Boolean {
        val result = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun applyReadPhoneStatePermissino() {
        val permissions = arrayOf(Manifest.permission.READ_PHONE_STATE)
        ActivityCompat.requestPermissions(this, permissions, 0)
    }

    //子类必须实现该方法返回一个布局资源的id
    abstract fun getLayoutResId(): Int

    fun showProgress(message: String) {
        progressDialog.setMessage(message)
        progressDialog.show()
    }

    fun dismissProgress() {
        progressDialog.dismiss()
    }

    fun hideSoftKeyboard() {
        inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}