package com.example.ilinktrip.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ilinktrip.models.Trip

@Dao
interface TripDao {
    @Query("SELECT * FROM trip")
    fun getAll(): List<Trip>

    @Query("SELECT * FROM trip WHERE id = :id")
    fun getById(id: String): Trip

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg trips: Trip)

    @Delete
    fun delete(trip: Trip)

    @Update
    fun update(trip: Trip)
}