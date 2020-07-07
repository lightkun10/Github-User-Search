package com.example.android.pandugithubuser.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.android.pandugithubuser.db.DatabaseContract.FavoriteColumns.Companion.LOGIN
import com.example.android.pandugithubuser.db.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.example.android.pandugithubuser.db.DatabaseContract.FavoriteColumns.Companion._ID
import java.sql.SQLException

/**
 * This class handles the DML(Data Manipulation Language).
 */

class FavoriteHelper(context: Context) {

    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: FavoriteHelper? = null

        fun getInstance(context: Context): FavoriteHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavoriteHelper(context)
            }
    }

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()

        if (database.isOpen) { database.close() }
    }

    // Take all data
    fun queryAll(): Cursor {
        return database.query(DATABASE_TABLE, null, null, null, null, null, "$_ID ASC")
    }

    // Take data based on its id
    fun queryById(id: String): Cursor {
        return database.query(DATABASE_TABLE, null, "$_ID = ?", arrayOf(id), null, null, null, null)
    }

    // Take data based on its login
    fun queryByLogin(login: String): Cursor {
        return database.query(DATABASE_TABLE, null, "$LOGIN = ?", arrayOf(login), null, null, null, null)
    }
    // Save data
    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    // Update data
    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$_ID = ?", arrayOf(id))
    }

    // Delete based on its id
    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$_ID = '$id'", null)
    }

    // Delete based on its login
    fun deleteByLogin(login: String): Int {
        return database.delete(DATABASE_TABLE, "$LOGIN = '$login'", null)
    }

    fun isFavsWithId(id: String?): Boolean {
        val cursor = database.query(DATABASE_TABLE, null, "$_ID = ?", arrayOf(id), null, null, null, null)

        if (cursor != null && cursor.count > 0) {
            return true
        }

        return false
    }

}