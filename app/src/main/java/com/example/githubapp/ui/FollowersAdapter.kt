package com.example.githubapp.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubapp.data.response.FollowersResponseItem
import com.example.githubapp.database.Favourite
import com.example.githubapp.database.FavouriteDao
import com.example.githubapp.databinding.ItemUserBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FollowersAdapter(private val dao: FavouriteDao) : ListAdapter<FollowersResponseItem, FollowersAdapter.MyViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FollowersResponseItem>() {
            override fun areItemsTheSame(oldItem: FollowersResponseItem, newItem: FollowersResponseItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: FollowersResponseItem, newItem: FollowersResponseItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowersAdapter.MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val review = getItem(position)
        holder.binding.cvUser.setOnClickListener {
            val moveIntent = Intent(holder.binding.root.context,UserDetailActivity::class.java)
            moveIntent.putExtra(UserDetailActivity.USERLOGIN,review.login)
            holder.binding.root.context.startActivity(moveIntent) }

        holder.bind(review)
    }

    class MyViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(detail: FollowersResponseItem){

            binding.tvUsername.text = detail.login
            Glide.with(binding.root)
                .load(detail.avatarUrl)
                .into(binding.ivAvatar)
            binding.cvUser.cardElevation = "2.0".toFloat()
        }
    }
}
