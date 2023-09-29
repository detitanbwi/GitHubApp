package com.example.githubapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.database.Favourite
import com.example.githubapp.database.FavouriteRoomDatabase
import com.example.githubapp.databinding.ActivityFavouriteUserBinding

class FavouriteUserActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFavouriteUserBinding
    private val FavouriteUserViewModel by viewModels<FavouriteUserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvFavUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvFavUsers.addItemDecoration(itemDecoration)
        FavouriteUserViewModel.getAllFavourite().observe(this){
            showUsers(it)
        }
    }

    private fun showUsers(listUser: List<Favourite>) {
        val favouriteDao = FavouriteRoomDatabase.getDatabase(applicationContext).favouriteDao()
        val adapter = FavouriteUserAdapter(favouriteDao)
        adapter.submitList(listUser)
        binding.rvFavUsers.adapter = adapter
        //Toast.makeText(this,listUser.size.toString(),Toast.LENGTH_SHORT).show()
    }
}