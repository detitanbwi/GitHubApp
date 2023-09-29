package com.example.githubapp.data.retrofit
import com.example.githubapp.data.response.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getUserList(
        @Query("q") query: String
    ): Call<ResponseUserList>

    @GET("search/users")
    fun getUserCount(
        @Query("q") query: String
    ): Call<ResponseUserList>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<FollowersResponseItem>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<FollowingResponseItem>>

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<DetailResponse>

}