package com.example.ilinktrip.dao

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ilinktrip.application.ILinkTripApplication
import com.example.ilinktrip.entities.FavoriteTraveler
import com.example.ilinktrip.utils.LocalDateTypeConverter
import com.example.ilinktrip.entities.Trip
import com.example.ilinktrip.entities.User

@Database(
    entities = [User::class, Trip::class, FavoriteTraveler::class],
    version = 2
)
@TypeConverters(LocalDateTypeConverter::class)
abstract class AppLocalDbRepository : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun tripDao(): TripDao
    abstract fun tripWithUserDao(): TripWithUserDao
    abstract fun favoriteTravelerDao(): FavoriteTravelerDao
}

object AppLocalDatabase {
    val db: AppLocalDbRepository by lazy {
        val context = ILinkTripApplication.Globals.appContext
            ?: throw IllegalStateException("Application context not available")

        Room.databaseBuilder(context, AppLocalDbRepository::class.java, "ILinkTripDbFile.db")
            .fallbackToDestructiveMigration().build()
    }
}