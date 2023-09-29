package com.example.githubapp.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubapp.data.response.ItemsItem
import com.example.githubapp.data.response.ResponseUserList
import com.example.githubapp.data.retrofit.ApiConfig
import com.example.githubapp.database.FavouriteDao
import com.example.githubapp.database.FavouriteRoomDatabase
import com.example.githubapp.repository.FavouriteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val application: Application) : ViewModel() {

    private val repository: FavouriteRepository = FavouriteRepository(application)

    private val _listUser = MutableLiveData<List<ItemsItem>>()
    val listUser : LiveData<List<ItemsItem>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    init {
        getUsers("arif")
    }
    fun getUsers(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserList(query)
        client.enqueue(object : Callback<ResponseUserList> {
            override fun onResponse(
                call: Call<ResponseUserList>,
                response: Response<ResponseUserList>
            ) {

                if(response.isSuccessful){
                    Log.d("githubViewModel", "${response.body()}")
                    val responsebody = response.body()
                    (if (responsebody != null) {
                            _listUser.value = responsebody.items
                        }
                    )
                } else {
                    Log.e("githubViewModel", response.message())
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<ResponseUserList>, t: Throwable) {
                Log.e("githubViewModel", "${t.message}")
                _isLoading.value = false
            }

        })

    }
    fun isUsernameInFavourites(username: String): LiveData<Boolean> {
        return repository.isUsernameExists(username)
    }
}