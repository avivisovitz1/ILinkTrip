package com.example.ilinktrip.models

import android.os.Looper
import androidx.core.os.HandlerCompat
import com.example.ilinktrip.dao.AppLocalDatabase
import java.util.concurrent.Executors

class Model private constructor() {
    private val database = AppLocalDatabase.db
    private val firebaseModel = FirebaseModel()
    private var executor = Executors.newSingleThreadExecutor()
    private var mainHandler = HandlerCompat.createAsync(Looper.getMainLooper())

    companion object {
        private var _instance: Model = Model()

        fun instance(): Model {
            return _instance
        }
    }

    fun getAllUsers(callback: (List<User>) -> Unit) {
        firebaseModel.getAllUsers(callback)
//        executor.execute {
//            val users = database.userDao().getAll()
//
//
//            mainHandler.post {
//                callback(users)
//            }
//        }
    }

    fun addUser(user: User, callback: () -> Unit) {
        firebaseModel.addUser(user, callback)
    }

    fun getAllTrips(callback: (List<Trip>) -> Unit) {
        firebaseModel.getAllTrips(callback)
//        executor.execute {
//            val trips = database.tripDao().getAll()
//
//            try {
//                Thread.sleep(5000)
//            } catch (e: InterruptedException) {
//                e.printStackTrace()
//            }
//
//            mainHandler.post {
//                callback(trips)
//            }
//        }
    }

    fun addTrip(trip: Trip, callback: () -> Unit) {
        firebaseModel.addTrip(trip, callback)
//        executor.execute {
//            database.tripDao().insertAll(trip)
//
//            try {
//                Thread.sleep(5000)
//            } catch (e: InterruptedException) {
//                e.printStackTrace()
//            }
//
//            mainHandler.post {
//                callback()
//            }
//        }
    }

    fun addFavoriteTraveler(userId: String, favoriteTravelerId: String, callback: () -> Unit) {
        firebaseModel.addUserFavoriteTraveler(userId, favoriteTravelerId, callback)
    }

    fun deleteFavoriteTraveler(userId: String, favoriteTravelerId: String, callback: () -> Unit) {
        firebaseModel.deleteUserFavoriteTraveler(userId, favoriteTravelerId, callback)
    }

    fun getUserFavoriteTravelers(userId: String, callback: (List<String>) -> Unit) {
        firebaseModel.getUserFavoriteTravelers(userId, callback)
    }

    fun updateTrip(trip: Trip, callback: () -> Unit) {
        executor.execute {
            database.tripDao().update(trip)

            try {
                Thread.sleep(5000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            mainHandler.post {
                callback()
            }
        }
    }


}