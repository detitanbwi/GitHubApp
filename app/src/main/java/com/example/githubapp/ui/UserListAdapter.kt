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
import com.example.githubapp.data.response.ItemsItem
import com.example.githubapp.database.Favourite
import com.example.githubapp.database.FavouriteDao
import com.example.githubapp.databinding.ItemUserBinding
import com.example.githubapp.repository.FavouriteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UserListAdapter(private val dao: FavouriteDao) : ListAdapter<ItemsItem, UserListAdapter.MyViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val review = getItem(position)
        holder.binding.cvUser.setOnClickListener {
            val moveIntent = Intent(holder.binding.root.context,UserDetailActivity::class.java)
            moveIntent.putExtra(UserDetailActivity.USERLOGIN,review.login)
            holder.binding.root.context.startActivity(moveIntent) }

        holder.bind(review)
        holder.binding.cbFavourite.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                CoroutineScope(Dispatchers.IO).launch {
                    val data = Favourite(null, review.login, review.avatarUrl)
                    dao.insert(data)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(holder.binding.root.context, "Ditambahkan ke favorit", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    dao.deleteByUsername(review.login)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(holder.binding.root.context, "Dihapus dari favorit", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    class MyViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(detail: ItemsItem){
            binding.tvUsername.text = detail.login
            Glide.with(binding.root)
                .load(detail.avatarUrl) // URL Gambar
                .into(binding.ivAvatar)
            binding.cbFavourite.isChecked = detail.isFavourite == true
        }
    }

}