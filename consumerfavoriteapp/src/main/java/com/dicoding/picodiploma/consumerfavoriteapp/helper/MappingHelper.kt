package com.dicoding.picodiploma.consumerfavoriteapp.helper

import android.database.Cursor
import com.dicoding.picodiploma.consumerfavoriteapp.data.Favorite
import com.dicoding.picodiploma.consumerfavoriteapp.db.DatabaseContract

object MappingHelper {

    fun mapCursorToArrayList(favoritesCursor: Cursor?): ArrayList<Favorite> {
        val favoritesList = ArrayList<Favorite>()

        favoritesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns._ID))
                val avatar_url = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR_URL))
                val login = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.LOGIN))
                favoritesList.add(Favorite(id, avatar_url, login))
            }
        }

        return favoritesList
    }

    fun mapCursorToObject(favoritesCursor: Cursor?): Favorite {
        var favorite = Favorite()
        favoritesCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns._ID))
            val avatar_url = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR_URL))
            val login = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.LOGIN))
            favorite = Favorite(id, avatar_url, login)
        }

        return favorite
    }


}