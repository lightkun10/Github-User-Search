package com.dicoding.picodiploma.consumerfavoriteapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.consumerfavoriteapp.R
import com.dicoding.picodiploma.consumerfavoriteapp.adapter.FollowAdapter
import com.example.android.pandugithubuser.model.FollowingViewModel
import kotlinx.android.synthetic.main.fragment_following.*

class FollowingFragment : Fragment() {
    private lateinit var adapter: FollowAdapter
    private lateinit var followingViewModel: FollowingViewModel

    companion object {
        private const val EXTRA_LOGIN = "extra_login"

        fun newInstance(login: String?): FollowerFragment {
            /* Di dalam fungsi newInstance, user memasukkan parameter yg dikirimkan
             * ke dlm Bundle sesuai dgn tipe datanya dgn format Key-Value, dgn
             * "EXTRA_LOGIN" bertindak sebagai key dan "login" sbg value.
             * Kemudian setArgument digunakan untuk mengirimkan data bundle tersebut ke fragment.
             * Lalu, data akan di-"transfer" ke onCreateView. */

            val fragment =
                FollowerFragment()
            val bundle = Bundle()

            bundle.putString(EXTRA_LOGIN, login)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FollowAdapter()
        adapter.notifyDataSetChanged()

        following_recyclerView.layoutManager = LinearLayoutManager(context)
        following_recyclerView.adapter = adapter
        following_recyclerView.setHasFixedSize(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val username = arguments?.getString(FollowerFragment.EXTRA_LOGIN)
        if (username != null) {
            followingSet(username)
        }
    }

    private fun followingSet(login: String) {
        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FollowingViewModel::class.java)

        followingViewModel.setFollowers(login)

        followingViewModel.getFollowers().observe(viewLifecycleOwner, Observer { followingItems ->
            if (followingItems != null) {
                adapter.setData(followingItems)
            } else {
                Toast.makeText(activity, "Error parsing", Toast.LENGTH_SHORT).show()
            }
        })
    }

}