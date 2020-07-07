package com.example.android.pandugithubuser.activities

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.android.pandugithubuser.R
import com.example.android.pandugithubuser.adapters.SectionsPagerAdapter
import com.example.android.pandugithubuser.data.Favorite
import com.example.android.pandugithubuser.data.Users
import com.example.android.pandugithubuser.db.DatabaseContract
import com.example.android.pandugithubuser.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.example.android.pandugithubuser.db.FavoriteHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_data_user.*

class DataUserActivity : AppCompatActivity() {
    private lateinit var fab: FloatingActionButton
    private var favorite: Favorite? = null
    private lateinit var favoriteHelper: FavoriteHelper
    private lateinit var uriWithLogin: Uri
    private lateinit var uriWithId: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_user)
        setLayout()
    }


    private fun setLayout() {
        // Call instance of database
        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()

        // Set the status for favorited user
        var isFavorite: Boolean

        val user = intent.getParcelableExtra(EXTRA_USER) as Users

        // Get user login infos
        val userLogin: TextView = findViewById(R.id.user_login)
        userLogin.text = user.login

        // Set the floating action button
        fab = findViewById(R.id.fab)

        // Get and set user avatar image in each user details
        val imgAvatarReceived: ImageView = findViewById(R.id.user_img_avatar)
        Glide.with(applicationContext).load(user.avatar_url)
            .into(imgAvatarReceived)      // Using Glide to load user image

        // Current data of each user clicked
        val curFavorite = Favorite(
            user.id,
            user.avatar_url,
            user.login
        )

        // set actionbar title
        supportActionBar?.title = "${curFavorite.login} detail"
        // set back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // uriWithLogin = Uri.parse("$CONTENT_URI/$curFavorite.login")
        uriWithId = Uri.parse("$CONTENT_URI/$curFavorite.id")   // id of user


        // Set fab based on its availability on db
        isFavorite = contentResolver.getType(uriWithId)?.toBoolean() ?: false

        // Set floating action button status
        setFab(isFavorite)

        fab.setOnClickListener {
            // If current user clicked is one of the favorites, when the fab is clicked, unfavorite the user
            isFavorite = if (isFavorite) {
                contentResolver.delete(uriWithId, null, null)
                Snackbar.make(activity_user, "Favorite removed", Snackbar.LENGTH_LONG).show()

                // DO NOT DELETE THIS
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

                // DO NOT DELETE THIS
                true
            }
            setFab(isFavorite)
        }

        setTab(user)
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

    override fun onDestroy() {
        super.onDestroy()
        favoriteHelper.close()  // Prevent memory leaks
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_REPLY = "com.example.android.pandugithubuser.REPLY"
    }
}