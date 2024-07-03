package com.example.ilinktrip.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ilinktrip.entities.FavoriteTraveler

@Dao
interface FavoriteTravelerDao {
    @Query("SELECT favoriteUserId FROM favorite_traveler WHERE userId = :userId")
    fun getByUserId(userId: String): LiveData<List<String>>

    @Query("SELECT userId FROM favorite_traveler WHERE userId = :favoriteUserId")
    fun getByFavoriteUserId(favoriteUserId: String): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg connections: FavoriteTraveler)

    @Query("DELETE FROM favorite_traveler WHERE userId = :userId AND favoriteUserId = :favoriteUserId")
    fun delete(userId: String, favoriteUserId: String)

    @Query("DELETE FROM favorite_traveler")
    fun deleteAll()
}