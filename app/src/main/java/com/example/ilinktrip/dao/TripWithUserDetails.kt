package com.example.ilinktrip.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.ilinktrip.entities.TripWithUser

@Dao
interface TripWithUserDao {
    @Transaction
    @Query("SELECT * from trip")
    fun getAll(): LiveData<List<TripWithUser>>
}