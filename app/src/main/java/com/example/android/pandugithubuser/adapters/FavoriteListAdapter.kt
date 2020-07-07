package com.example.android.pandugithubuser.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.android.pandugithubuser.R
import com.example.android.pandugithubuser.data.Favorite
import kotlinx.android.synthetic.main.item_row_favorite.view.*

class FavoriteListAdapter internal constructor(context: Context) : RecyclerView.Adapter<FavoriteListAdapter.FavoriteViewHolder>()  {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val favoritesData = ArrayList<Favorite>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(favorite: Favorite) {
            with(itemView) {
                tv_user_favorite.text = favorite.login

                favorite.avatar_url?.let {
                    Glide.with(itemView.context)
                        .load(it)
                        .apply(RequestOptions().override(56, 56))
                        .into(avatar_favorite)
                }

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(favorite) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteListAdapter.FavoriteViewHolder {
        val itemView = inflater.inflate(R.layout.item_row_favorite, parent, false)
        return FavoriteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FavoriteListAdapter.FavoriteViewHolder, position: Int) {
        holder.bind(favoritesData[position])
    }

    fun setData(favorites: ArrayList<Favorite>) {
        favoritesData.clear()
        favoritesData.addAll(favorites)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = favoritesData.size

    interface OnItemClickCallback {
        fun onItemClicked(data: Favorite)
    }
}