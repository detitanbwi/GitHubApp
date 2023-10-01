package com.example.githubapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.githubapp.R
import com.example.githubapp.SectionsPagerAdapter
import com.example.githubapp.database.Favourite
import com.example.githubapp.database.SettingPreferences
import com.example.githubapp.database.dataStore
import com.example.githubapp.databinding.ActivityUserDetailBinding
import com.example.githubapp.repository.FavouriteRepository
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserDetailActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityUserDetailBinding
    private val UserDetailViewModel by viewModels<UserDetailViewModel>(){
        val pref = SettingPreferences.getInstance(application.dataStore)
        ViewModelFactory.getInstance(application,pref)
    }
    private lateinit var favouriteRepository: FavouriteRepository
    private lateinit var data: Favourite

    companion object {
        var Username = ""
        const val USERLOGIN = "userlogin"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username  = intent.getStringExtra(USERLOGIN)
        if (username != null) {
            Username = username
        }

        UserDetailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
        UserDetailViewModel.getUserDetail(username.toString())
        UserDetailViewModel.error.observe(this){
            binding.tvDisplayName.text = it
        }
        UserDetailViewModel.userDetail.observe(this){

            if (it != null) {
                Glide.with(binding.root)
                    .load(it.avatarUrl) // URL Gambar
                    .apply(RequestOptions.bitmapTransform(CircleCrop()))
                    .into(binding.ivDisplayAvatar)
                binding.tvDisplayName.text = it.name.toString()
                binding.tvDisplayUsername.text = it.login
                data = Favourite(null,it.login,it.avatarUrl)
                tabs.getTabAt(1)?.text = "Follower (${it.followers})"
                tabs.getTabAt(0)?.text = "Follower (${it.following})"
            }
        }

        UserDetailViewModel.isUsernameExist(username.toString()).observe(this){
            if (it){
                binding.floatingActionButton.setOnClickListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        UserDetailViewModel.delete(data.username.toString())
                        withContext(Dispatchers.Main) {
                            binding.floatingActionButton.setImageResource(R.drawable.ic_favourite_outlined)
                            Toast.makeText(this@UserDetailActivity, "Dihapus dari favorit", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }else {
                binding.floatingActionButton.setOnClickListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        val data = Favourite(null, data.username, data.avatarUrl)
                        UserDetailViewModel.insert(data)
                        withContext(Dispatchers.Main) {
                            binding.floatingActionButton.setImageResource(R.drawable.ic_favourite_filled)
                            Toast.makeText(this@UserDetailActivity, "Ditambahkan ke favorit", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }


        UserDetailViewModel.getFollowers(username.toString())

        UserDetailViewModel.getFollowing(username.toString())
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.tvDisplayUsername.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.tvDisplayName.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.ivDisplayAvatar.visibility = if (isLoading) View.GONE else View.VISIBLE
        Log.d("ok","halo")
    }

}