package com.example.android.pandugithubuser.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.android.pandugithubuser.db.DatabaseContract.AUTHORITY
import com.example.android.pandugithubuser.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.example.android.pandugithubuser.db.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.example.android.pandugithubuser.db.FavoriteHelper

class FavoritesContentProvider : ContentProvider() {

    companion object {
        /**
         * Integer here is for identifier between select all or select by id
         */
        private const val FAVORITE = 1
        private const val FAVORITE_LOGIN = 2
        private const val FAVORITE_ID = 3
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favoriteHelper: FavoriteHelper

        init {
            // content://com.example.android.pandugithubuser/favorite
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAVORITE)

            // content://com.example.android.pandugithubuser/favorite/login
            sUriMatcher.addURI(AUTHORITY,
                    "$TABLE_NAME/#",
                    FAVORITE_LOGIN)

            // content://com.example.android.pandugithubuser/favorite/id
            sUriMatcher.addURI(AUTHORITY,
                "$TABLE_NAME/#",
                FAVORITE_ID)
        }
    }

    override fun onCreate(): Boolean {
        favoriteHelper = FavoriteHelper.getInstance(context as Context)
        favoriteHelper.open()
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        return when (sUriMatcher.match(uri)) {
            FAVORITE -> {
                favoriteHelper.queryAll()
            }
            FAVORITE_LOGIN -> {
                favoriteHelper.queryByLogin(uri.lastPathSegment.toString())
            }
            FAVORITE_ID -> {
                favoriteHelper.queryById(uri.lastPathSegment.toString())
            }
            else -> {
                null
            }
        }
    }


    override fun getType(uri: Uri): String {
        // val result = uri.lastPathSegment?.let { favoriteHelper.isFavsWithId(it) }
        val result = favoriteHelper.isFavsWithId(uri.lastPathSegment)
        return result.toString()
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (FAVORITE) {
            sUriMatcher.match(uri) -> favoriteHelper.insert(values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val updated: Int = when (FAVORITE_LOGIN) {
            sUriMatcher.match(uri) -> favoriteHelper.update(uri.lastPathSegment.toString(), values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return updated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = favoriteHelper.deleteById(uri.lastPathSegment.toString())

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return deleted
    }
}
