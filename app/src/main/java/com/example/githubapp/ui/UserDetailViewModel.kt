package com.example.githubapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubapp.data.response.DetailResponse
import com.example.githubapp.data.response.FollowersResponseItem
import com.example.githubapp.data.response.FollowingResponseItem
import com.example.githubapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel : ViewModel() {

    private val _userDetail = MutableLiveData<DetailResponse?>()
    val userDetail : MutableLiveData<DetailResponse?> = _userDetail

    private val _followers = MutableLiveData<List<FollowersResponseItem>?>()
    val followers : MutableLiveData<List<FollowersResponseItem>?> = _followers

    private val _followings = MutableLiveData<List<FollowingResponseItem>?>()
    val followings : MutableLiveData<List<FollowingResponseItem>?> = _followings

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error : LiveData<String> = _error

    fun getUserDetail(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                if(response.isSuccessful){
                    Log.d("githubViewModel", "${response.body()}")
                    val responsebody = response.body()
                    (if (responsebody != null) {
                        _userDetail.value = responsebody
                    }
                            )
                } else {
                    Log.e("githubViewModel", response.message())
                    _error.value = response.message()
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                _isLoading.value = false
                _error.value = username
            }
        })
    }

    fun getFollowers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<FollowersResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowersResponseItem>>,
                response: Response<List<FollowersResponseItem>>
            ) {
                if (response.isSuccessful) {
                    _followers.value = arrayListOf()
                    val responsebody = response.body()
                    if (responsebody != null) {
                        _followers.value = responsebody
                    }
                } else {
                    Log.e("githubViewModel", "here")
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<List<FollowersResponseItem>>, t: Throwable) {
                Log.e("githubViewModel", "${t.cause}")
                _isLoading.value = false
            }

        })
    }

    fun getFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<FollowingResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowingResponseItem>>,
                response: Response<List<FollowingResponseItem>>
            ) {
                if (response.isSuccessful) {
                    _followings.value = arrayListOf()
                    val responsebody = response.body()
                    if (responsebody != null) {
                        _followings.value = responsebody
                    }
                } else {
                    Log.e("githubViewModel", "here")

                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<List<FollowingResponseItem>>, t: Throwable) {
                Log.e("githubViewModel", "${t.message}")
                _isLoading.value = false
            }
        })
    }

}