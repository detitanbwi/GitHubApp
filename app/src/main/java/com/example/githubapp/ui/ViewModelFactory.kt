package com.example.githubapp.ui

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.location.SettingInjectorService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubapp.database.FavouriteDao
import com.example.githubapp.database.SettingPreferences
import com.example.githubapp.repository.FavouriteRepository

class ViewModelFactory private constructor(
    private val mApplication: Application,
    private val pref: SettingPreferences
): ViewModelProvider.NewInstanceFactory() {
    companion object{
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        fun getInstance(application: Application, pref: SettingPreferences): ViewModelFactory =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: ViewModelFactory(application, pref)
            }.also { INSTANCE = it }
    }
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(mApplication) as T
        }
        if(modelClass.isAssignableFrom(UserDetailViewModel::class.java)){
            return UserDetailViewModel(mApplication) as T
        }
        if(modelClass.isAssignableFrom(SettingsViewModel::class.java)){
            return SettingsViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class ${modelClass.name}")
    }

}