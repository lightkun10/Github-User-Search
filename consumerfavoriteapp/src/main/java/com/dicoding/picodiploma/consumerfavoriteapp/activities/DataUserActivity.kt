package com.dicoding.picodiploma.consumerfavoriteapp.activities

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.consumerfavoriteapp.R
import com.dicoding.picodiploma.consumerfavoriteapp.adapter.SectionsPagerAdapter
import com.dicoding.picodiploma.consumerfavoriteapp.db.DatabaseContract
import com.dicoding.picodiploma.consumerfavoriteapp.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.dicoding.picodiploma.consumerfavoriteapp.data.Favorite
import com.dicoding.picodiploma.consumerfavoriteapp.data.Users
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_data_user.*

class DataUserActivity : AppCompatActivity() {
    private lateinit var fab: FloatingActionButton
    private var favorite: Favorite? = null
    private lateinit var uriWithId: Uri
    private lateinit var getUserFirst: ArrayList<Favorite>

    var values = ContentValues()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_user)
        setLayout()
    }


    private fun setLayout() {

        // Set the status for favorited user
        var isFavorite: Boolean

        val user: Users? = intent.getParcelableExtra(EXTRA_USER)

        // Get user login infos
         val userLogin: TextView = findViewById(R.id.user_login)
         userLogin.text = user?.login

        // Set the floating action button
        fab = findViewById(R.id.fab)

        // Get and set user avatar image in each user details
        val imgAvatarReceived: ImageView = findViewById(R.id.user_img_avatar)
        Glide.with(applicationContext).load(user?.avatar_url).into(imgAvatarReceived)      // Using Glide to load user image

        // Current data of each user clicked
        val curFavorite = Favorite(
            user?.id,
            user?.avatar_url,
            user?.login
        )

        // set actionbar title
        supportActionBar?.title = "${curFavorite.login} detail"
        // set back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Uri yang di dapatkan disini akan digunakan untuk ambil data dari provider
        // content://com.dicoding.picodiploma.mynotesapp/note/id
        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + curFavorite.id)    // id of user

        // Set fab based on its availability on db
        isFavorite = contentResolver.getType(uriWithId)?.toBoolean()!!

        // Set fab based on its availability on db
        // isFavorite = favoriteHelper.isFavsWithLogin(curFavorite.login.toString())
        // Set floating action button status
        setFab(isFavorite)

        fab.setOnClickListener {
            // If current user clicked is one of the favorites, when the fab is clicked, unfavorite the user
            isFavorite = if (isFavorite) {
                // favoriteHelper.deleteByLogin(curFavorite.login.toString())
                contentResolver.delete(uriWithId, null, null)
                Snackbar.make(activity_user, "Favorite removed", Snackbar.LENGTH_SHORT).show()
                false
                // Otherwise, add user to favorite
            } else {
                val values = ContentValues()
                values.put(DatabaseContract.FavoriteColumns._ID, curFavorite.id)
                values.put(DatabaseContract.FavoriteColumns.AVATAR_URL, curFavorite.avatar_url)
                values.put(DatabaseContract.FavoriteColumns.LOGIN, curFavorite.login)

                val result = contentResolver.insert(CONTENT_URI, values)
                if (result != null) {
                    Snackbar.make(activity_user, "Added new favorite", Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(activity_user, "Failed updating data", Snackbar.LENGTH_SHORT)
                        .show()
                }

                true
            }
             setFab(isFavorite)
        }

        if (user != null) {
            setTab(user)
        }
    }


    private fun setTab(user: Users) {
        // Connect SectionsPagerAdapter(FragmentPagerAdapter) with ViewPager
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)

        // Pass data to fragment and viewPagerAdapter
        user.login?.let { sectionsPagerAdapter.customData(it) }

        view_pager.adapter = sectionsPagerAdapter

        // Connect TabLayout with ViewPager
        tabs.setupWithViewPager(view_pager)     /* tabs get from xml files. */

        supportActionBar?.elevation = 0f
    }

    private fun setFab(isFavorite: Boolean) {
        if (!isFavorite) fab.setImageResource(R.drawable.baseline_favorite_border_black_24dp)
        else fab.setImageResource(R.drawable.baseline_favorite_black_24dp)
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(activity_user, message, Snackbar.LENGTH_LONG).show()
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_REPLY = "com.example.android.pandugithubuser.REPLY"
    }
}