package com.example.android.pandugithubuser.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.android.pandugithubuser.R
import com.example.android.pandugithubuser.data.FollowItems
import kotlinx.android.synthetic.main.item_row_followers.view.*

class FollowAdapter : RecyclerView.Adapter<FollowAdapter.FollowerViewHolder>() {

    private val mData = ArrayList<FollowItems>()

    fun setData(items: ArrayList<FollowItems>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): FollowerViewHolder {
        val mView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row_followers, parent, false)

        return FollowerViewHolder(mView)
    }

    override fun onBindViewHolder(followerViewholder: FollowerViewHolder, position: Int) {
        followerViewholder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class FollowerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(followItems: FollowItems) {
            with(itemView) {
                tv_user_follower.text = followItems.login

                followItems.avatarUrl?.let {
                    Glide.with(itemView.context)
                        .load(it)
                        .apply(RequestOptions().override(56, 56))
                        .into(follower_avatar_user)
                }
            }
        }
    }
}