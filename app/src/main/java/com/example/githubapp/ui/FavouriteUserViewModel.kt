package com.example.githubapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.githubapp.database.Favourite
import com.example.githubapp.repository.FavouriteRepository

class FavouriteUserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FavouriteRepository = FavouriteRepository(application)
    private val allFavourites: LiveData<List<Favourite>> = repository.getAllFavourite()
    //private val isUsernameExist: LiveData<Boolean> = checkIsUsernameExist()
    private val _isUsernameExist = MutableLiveData<Boolean>()
    val isUsernameExist : LiveData<Boolean> = _isUsernameExist


    fun getAllFavourite(): LiveData<List<Favourite>> {
        return allFavourites
    }

    fun checkIsUsernameExist(username: String): LiveData<Boolean> {
        return _isUsernameExist
    }

    fun insert(favourite: Favourite) {
        repository.insert(favourite)
    }

    fun delete(favourite: Favourite) {
        repository.delete(favourite)
    }

    fun update(favourite: Favourite) {
        repository.update(favourite)
    }
}




