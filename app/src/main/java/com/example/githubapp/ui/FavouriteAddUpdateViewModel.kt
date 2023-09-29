package com.example.githubapp.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.githubapp.database.Favourite
import com.example.githubapp.repository.FavouriteRepository

class FavouriteAddUpdateViewModel(application: Application) : ViewModel() {
    private val mFavouriteRepository: FavouriteRepository = FavouriteRepository(application)
    fun insert(favourite: Favourite) {
        mFavouriteRepository.insert(favourite)
    }
    fun update(favourite: Favourite) {
        mFavouriteRepository.update(favourite)
    }
    fun delete(favourite: Favourite) {
        mFavouriteRepository.delete(favourite)
    }
}