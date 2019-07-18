package com.example.im.contract

import android.os.Handler
import android.os.Looper

/**
 * 黑马程序员
 */
interface BasePresenter {

    companion object {
        val handler by lazy {
            Handler(Looper.getMainLooper())
        }
    }

    fun uiThread(f: () -> Unit) {
        handler.post { f() }
    }

}