package com.example.android.pandugithubuser.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.example.android.pandugithubuser.BuildConfig
import com.example.android.pandugithubuser.data.FollowItems
import org.json.JSONArray

class FollowerViewModel : ViewModel() {

    private val listFollowers = MutableLiveData<ArrayList<FollowItems>>()

    fun setFollowers(login: String) {
        val listItems = ArrayList<FollowItems>()
        val url = "https://api.github.com/users/$login/followers"

        AndroidNetworking.get(url)
            .addHeaders("Authorization",
                BuildConfig.API_KEY
            )
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray?) {
                    if (response != null) {
                        Log.d("RESPONSE_2", response.toString())

                        for (i in 0 until response.length()) {
                            val item = response.getJSONObject(i)
                            val user =
                                FollowItems()
                            user.avatarUrl = item.getString("avatar_url")
                            user.login = item.getString("login")

                            listItems.add(user)
                        }
                        listFollowers.postValue(listItems)
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.d("Error_2", anError!!.message.toString())

                }

            })
    }

    fun getFollowers(): LiveData<ArrayList<FollowItems>> {
        return listFollowers
    }
}