package com.dicoding.picodiploma.consumerfavoriteapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Favorite(
    var id: Int? = 0,
    var avatar_url: String? = null,
    var login: String? = null
) : Parcelable