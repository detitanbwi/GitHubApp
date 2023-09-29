package com.example.githubapp.repository
import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubapp.database.Favourite
import com.example.githubapp.database.FavouriteDao
import com.example.githubapp.database.FavouriteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavouriteRepository(application: Application) {
    private val mFavouritesDao: FavouriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = FavouriteRoomDatabase.getDatabase(application)
        mFavouritesDao = db.favouriteDao()
    }
    fun getAllFavourite(): LiveData<List<Favourite>> = mFavouritesDao.getAllFavourite()
    fun insert(favourite: Favourite) {
        executorService.execute { mFavouritesDao.insert(favourite) }
    }
    fun delete(favourite: Favourite) {
        executorService.execute { mFavouritesDao.delete(favourite) }
    }
    fun update(favourite: Favourite) {
        executorService.execute { mFavouritesDao.update(favourite) }
    }
    fun isUsernameExists(username: String): LiveData<Boolean> = mFavouritesDao.isUsernameExists(username)
}