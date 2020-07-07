package com.example.android.pandugithubuser.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.pandugithubuser.data.Users
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainViewModel : ViewModel() {
    // Global variable
    val listUsers = MutableLiveData<ArrayList<Users>>()

    // Setter
    fun setUsers(users: String) {
        // Set an array list to store the weather
        val listItems = ArrayList<Users>()

        val url = "https://api.github.com/search/users?q=${users}"

        // Create a new AsyncHttpClient instance and make a request
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 5eb366ad353df35991c2dd4615a416bdf80d21ad")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                try {
                    // Parsing Json
                    val result = String(responseBody)
                    val resObject = JSONObject(result)
                    val listUserSearch = resObject.getJSONArray("items")

                    // Iterate through list object json
                    for (i in 0 until listUserSearch.length()) {
                        val user = listUserSearch.getJSONObject(i)
                        val userItems =
                            Users(
                                user.getString("login"),
                                user.getInt("id"),
                                user.getString("avatar_url"),
                                user.getString("html_url"),
                                user.getString("followers_url"),
                                user.getString("following_url"),
                                user.getString("type")
                            ) // Initialize class & store in variable

                        // Add object to ArrayList
                        listItems.add(userItems)
                    }
                    listUsers.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.e("OnFailure", error?.message.toString())
            }

        })
    }



    // Getter
    fun getUsers(): LiveData<ArrayList<Users>> {    // LiveData is read-only
        return listUsers
    }
}