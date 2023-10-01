package com.example.githubapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.R
import com.example.githubapp.data.response.ItemsItem
import com.example.githubapp.database.FavouriteRoomDatabase
import com.example.githubapp.database.SettingPreferences
import com.example.githubapp.database.dataStore
import com.example.githubapp.databinding.ActivityMainBinding
import kotlinx.coroutines.channels.Channel

class MainActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingsViewModel =
            ViewModelProvider(this,ViewModelFactory.getInstance(application,pref)).get(
                SettingsViewModel::class.java
            )

        settingsViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.searchBar.inflateMenu(R.menu.option_menu)
        binding.searchBar.setOnMenuItemClickListener { menuItem ->
            // Handle menuItem click.
            when (menuItem.itemId) {
                R.id.menu1 -> {
                    val intent = Intent(this, FavouriteUserActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu2 -> {
                    val intent = Intent(this,SettingsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }

        }

        mainViewModel = obtainViewModel(this@MainActivity)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

        binding.fab.setOnClickListener{

        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.text = searchView.text
                    searchView.hide()
                    mainViewModel.getUsers(searchView.text.toString())
                    mainViewModel.listUser.observe(this@MainActivity){
                        showUsers(it)
                    }
                    false
                }
        }

        mainViewModel.listUser.observe(this){
            showUsers(it)
        }

    }

    private fun obtainViewModel(mainActivity: MainActivity): MainViewModel {
        val pref = SettingPreferences.getInstance(application.dataStore)
        val factory = ViewModelFactory.getInstance(mainActivity.application,pref)
        return ViewModelProvider(mainActivity,factory).get(MainViewModel::class.java)
    }

    private fun showUsers(listUser: List<ItemsItem>) {
        val favouriteDao = FavouriteRoomDatabase.getDatabase(applicationContext).favouriteDao()
        val adapter = UserListAdapter(favouriteDao)
        adapter.submitList(listUser)
        binding.rvUsers.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvUsers.visibility = if (isLoading) View.GONE else View.VISIBLE
        Log.d("ok","halo")
    }

}