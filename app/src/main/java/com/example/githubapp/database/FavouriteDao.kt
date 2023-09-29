package com.example.githubapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavouriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favourite: Favourite)
    @Update
    fun update(favourite: Favourite)
    @Delete
    fun delete(favourite: Favourite)
    @Query("DELETE FROM favourite WHERE username = :username")
    fun deleteByUsername(username: String)
    @Query("SELECT * from favourite ORDER BY id ASC")
    fun getAllFavourite(): LiveData<List<Favourite>>
    @Query("SELECT EXISTS (SELECT 1 FROM favourite WHERE username = :username LIMIT 1)")
    fun isUsernameExists(username: String): LiveData<Boolean>
}