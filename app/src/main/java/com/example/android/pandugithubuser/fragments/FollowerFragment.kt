package com.example.android.pandugithubuser.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.pandugithubuser.R
import com.example.android.pandugithubuser.adapters.FollowAdapter
import com.example.android.pandugithubuser.model.FollowerViewModel
import kotlinx.android.synthetic.main.fragment_follower.*



class FollowerFragment : Fragment() {
    private lateinit var adapter: FollowAdapter
    private lateinit var followerViewModel: FollowerViewModel
    private val TAG: String? = FollowingFragment::class.java.simpleName

    companion object {
        const val EXTRA_LOGIN = "login"

        fun newInstance(login: String?): FollowerFragment {
            val fragment =
                FollowerFragment()
            val bundle = Bundle()

            bundle.putString(EXTRA_LOGIN, login)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follower, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FollowAdapter()
        adapter.notifyDataSetChanged()

        follower_recyclerView.layoutManager = LinearLayoutManager(context)
        follower_recyclerView.adapter = adapter
        follower_recyclerView.setHasFixedSize(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        try {
            val username = arguments?.getString(EXTRA_LOGIN)
            if (username != null) {
                followerSet(username)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        super.onActivityCreated(savedInstanceState)
    }

    private fun followerSet(login: String?) {
        followerViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FollowerViewModel::class.java)

        if (login != null) {
            followerViewModel.setFollowers(login)
        }

        followerViewModel.getFollowers().observe(viewLifecycleOwner, Observer { followerItems ->
            if (followerItems != null) {
                adapter.setData(followerItems)
            }
        })
    }
}