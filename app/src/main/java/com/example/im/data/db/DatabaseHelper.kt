package com.example.im.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.im.app.IMApplication
import org.jetbrains.anko.db.*

/**
 * 黑马程序员
 */
class DatabaseHelper(ctx: Context = IMApplication.instance)
    : ManagedSQLiteOpenHelper(ctx, NAME, null, VERSION) {


    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.createTable(ContactTable.NAME, true,
                ContactTable.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                ContactTable.CONTACT to TEXT)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.dropTable(ContactTable.NAME, true)
        onCreate(p0)
    }

    companion object {
        val NAME = "im.db"
        val VERSION = 1
    }

}