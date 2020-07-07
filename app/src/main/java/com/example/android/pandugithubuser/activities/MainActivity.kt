package com.example.android.pandugithubuser.activities

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.pandugithubuser.R
import com.example.android.pandugithubuser.adapters.UserAdapter
import com.example.android.pandugithubuser.data.Users
import com.example.android.pandugithubuser.model.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var searchBar: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)


        // Inisialisasi WeatherAdapter & Widget
        val adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        /* Set Adapter */
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        /* Menyambungkan kelas MainViewModel dgn MainActivity. */
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)

        /* Mendapatkan value dari LiveData yg ada pada
         * kelas ViewModel, dengan metode subscribe. */
        mainViewModel.getUsers().observe(this, Observer { users ->
            if (users != null) {
                adapter.setData(users)
                showLoading(false)
            } else {
                Toast.makeText(this@MainActivity, "Error parsing", Toast.LENGTH_SHORT).show()
                showLoading(false)
            }
        })

        // Set SearchView
        searchBar = findViewById<SearchView>(R.id.search)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        //Turn iconified to false:
        searchBar.isIconified = false
        //The above line will expand it to fit the area as well as throw up the keyboard

        //To remove the keyboard, but make sure you keep the expanded version:
        searchBar.clearFocus()

        searchBar.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // searchBar.clearFocus();
                if (query.isEmpty()) {
                    Snackbar.make(root_layout, "No user found", Snackbar.LENGTH_SHORT).show()
                    return false
                }

                // Show loading when searching
                showLoading(true)
                // Set user to be searched
                mainViewModel.setUsers(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Users) {
                /* Make intent to Detail User Page */
                val intent = Intent(this@MainActivity, DataUserActivity::class.java)  // ActivitytoActivity

                intent.putExtra(DataUserActivity.EXTRA_USER, data)
                startActivity(intent)
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
//            R.id.action_change_language -> {
//                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
//                startActivity(mIntent)
//                true
//            }
            R.id.settings -> {
                val settingsIntent = Intent(this, SettingsActivity::class.java)
                startActivity(settingsIntent)
                true
            }
            R.id.list_favorites -> {
                val favsIntent = Intent(this, UserFavoriteActivity::class.java)
                startActivity(favsIntent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    // Hide and show progressBar
    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_MESSAGE = "extra_message"
    }
}