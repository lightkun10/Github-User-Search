package com.dicoding.picodiploma.consumerfavoriteapp.adapter

import android.content.Context
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dicoding.picodiploma.consumerfavoriteapp.R
import com.dicoding.picodiploma.consumerfavoriteapp.fragments.FollowerFragment
import com.dicoding.picodiploma.consumerfavoriteapp.fragments.FollowerFragment.Companion.EXTRA_LOGIN
import com.dicoding.picodiploma.consumerfavoriteapp.fragments.FollowingFragment


class SectionsPagerAdapter(private val mContext: Context, fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
{
    // BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT: hanya fragment yang ditampilkan saja
    // yg akan masuk ke lifecycle RESUMED, sedangkan yg lainnya masuk ke STARTED.
    private var myString: String = "user"

    /* Menampilkan fragment sesuai dgn posisi tab-nya. */
    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null

        when (position) {
            0 -> {
                fragment =
                    FollowerFragment()
                val bundle = Bundle()
                bundle.putString(EXTRA_LOGIN, customData(myString))
                fragment.arguments = bundle
            }
            1 -> {
                fragment =
                    FollowingFragment()
                val bundle = Bundle()
                bundle.putString(EXTRA_LOGIN, customData(myString))
                fragment.arguments = bundle
            }
        }
        return fragment as Fragment
//        val fragment = FollowerFragment.newInstance(position + 1)
//        return fragment
    }

    /* Memberikan judul pd masing2 tab. */
    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {

        /* @StringRes indicates that the integer to be passed
           is a String Resource (from values/strings.xml). */
        @StringRes
        val tabTitles = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )

        return mContext.resources.getString(tabTitles[position])
    }

    /* Menentukan jumlah tab yg ingin ditampilkan. */
    override fun getCount(): Int {
        return 2
    }

    fun customData(data: String): String {
        myString = data
        return myString
    }
}