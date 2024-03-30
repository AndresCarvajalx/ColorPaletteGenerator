package com.andrescarvajald.colorpalettegenerator.domain.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseAssist(context: Context): SQLiteOpenHelper(context, "database", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        // query
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}