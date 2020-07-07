package com.dicoding.picodiploma.consumerfavoriteapp.activities

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.consumerfavoriteapp.R
import com.dicoding.picodiploma.consumerfavoriteapp.adapter.FavoriteListAdapter
import com.dicoding.picodiploma.consumerfavoriteapp.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.dicoding.picodiploma.consumerfavoriteapp.data.Favorite
import com.dicoding.picodiploma.consumerfavoriteapp.data.Users
import com.dicoding.picodiploma.consumerfavoriteapp.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_user_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserFavoriteActivity : AppCompatActivity() {

    private lateinit var adapter: FavoriteListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_favorite)

        // Prepare the recyclerview and adapter to display the data
        val favsRecyclerView = findViewById<RecyclerView>(R.id.favs_recyclerView)
        adapter = FavoriteListAdapter(this)
        adapter.notifyDataSetChanged()
        favsRecyclerView.adapter = adapter
        favsRecyclerView.layoutManager = LinearLayoutManager(this)

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val customObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadFavoritesAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, customObserver)

        loadFavoritesAsync()
    }

    private fun loadFavoritesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBar.visibility = View.VISIBLE
            val deferredFavorites = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progressBar.visibility = View.INVISIBLE
            val favorites = deferredFavorites.await()
            if (favorites.size > 0) {
                adapter.setData(favorites)
                adapter.setOnItemClickCallback(object : FavoriteListAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: Favorite) {
                        // Make intent to user detail page
                        val intent = Intent(this@UserFavoriteActivity, DataUserActivity::class.java)
                        val newUser = Users(login = data.login, id = data.id, avatar_url =  data.avatar_url)
                        intent.putExtra(DataUserActivity.EXTRA_USER, newUser)
                        startActivity(intent)
                    }

                })
            } else {
                adapter.setData(ArrayList())
                showSnackbarMessage("Tidak ada data saat ini")
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /**
     * Tampilkan snackbar
     * @param message inputan message
     */
    private fun showSnackbarMessage(message: String) {
        Snackbar.make(favs_recyclerView, message, Snackbar.LENGTH_LONG).show()
    }
}