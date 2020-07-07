package com.example.android.pandugithubuser.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Users(
    var login: String? = "",
    var id: Int? = 0,
    var avatar_url: String? = "",
    var html_url: String? = "",
    var followers_url: String? = "",
    var following_url: String? = "",
    var type: String? = ""
) : Parcelable