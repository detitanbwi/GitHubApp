package com.example.githubapp.ui

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubapp.database.Favourite
import com.example.githubapp.database.FavouriteDao
import com.example.githubapp.databinding.ItemUserBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouriteUserAdapter(private val dao: FavouriteDao) : ListAdapter<Favourite, FavouriteUserAdapter.FavouriteViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Favourite>() {
            override fun areItemsTheSame(oldItem: Favourite, newItem: Favourite): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Favourite, newItem: Favourite): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavouriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val favourite = getItem(position)
        holder.binding.cvUser.setOnClickListener {
            val moveIntent = Intent(holder.binding.root.context,UserDetailActivity::class.java)
            moveIntent.putExtra(UserDetailActivity.USERLOGIN,favourite.username)
            holder.binding.root.context.startActivity(moveIntent) }
        holder.binding.cbFavourite.setOnCheckedChangeListener { checkBox, isChecked ->
            if (!isChecked) {
                CoroutineScope(Dispatchers.IO).launch {
                    favourite.username?.let { dao.deleteByUsername(it) }
                }
            }
        }
        holder.bind(favourite)
    }

    class FavouriteViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(favourite: Favourite) {
            binding.apply {
                tvUsername.text = favourite.username
                Glide.with(binding.root)
                    .load(favourite.avatarUrl)
                    .into(ivAvatar)
                val favouriteDao: FavouriteDao

                cbFavourite.isChecked=true
            }
        }
    }
}