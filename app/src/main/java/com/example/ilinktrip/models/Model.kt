package com.example.ilinktrip.models

import android.graphics.Bitmap
import android.os.Looper
import androidx.core.os.HandlerCompat
import com.example.ilinktrip.dao.AppLocalDatabase
import com.example.ilinktrip.services.AuthService
import com.example.ilinktrip.services.FirebaseStorageService
import java.util.concurrent.Executors

class Model private constructor() {
    private val database = AppLocalDatabase.db
    private val firebaseModel = FirebaseModel()
    private val authenticationService = AuthService()
    private val storageService = FirebaseStorageService()
    private var executor = Executors.newSingleThreadExecutor()
    private var mainHandler = HandlerCompat.createAsync(Looper.getMainLooper())

    companion object {
        private var _instance: Model = Model()

        fun instance(): Model {
            return _instance
        }
    }

    fun getCurrentUser(callback: (user: User?) -> Unit) {
        authenticationService.getCurrentUser() {
            if (it != null) {
                firebaseModel.getUserDataByEmail(it.email!!, callback)
            } else {
                callback(null)
            }
        }
    }

    fun signIn(email: String, password: String, callback: (user: User?) -> Unit) {
        authenticationService.signIn(email, password) {
            if (it != null) {
                firebaseModel.getUserDataByEmail(it.email!!, callback)
            } else {
                callback(null)
            }
        }

    }

    fun signUp(userData: User, callback: (isSuccessful: Boolean) -> Unit) {
        authenticationService.signUp(userData.email, userData.password) {
            if (it != null) {
                callback(true)
//                firebaseModel.upsertUser(userData, callback)
            } else {
                callback(false)
            }
        }
    }

    fun signOut() {
        authenticationService.signOut()
    }


    fun getAllUsers(callback: (MutableMap<String, User>) -> Unit) {
        firebaseModel.getUsers(listOf(), callback)
//        executor.execute {
//            val users = database.userDao().getAll()
//
//
//            mainHandler.post {
//                callback(users)
//            }
//        }
    }

    fun addUserDetails(user: User, callback: (Boolean) -> Unit) {
        firebaseModel.upsertUser(user, callback)
    }

    fun updateUserDetails(user: User, callback: (isSuccessful: Boolean) -> Unit) {
        firebaseModel.upsertUser(user, callback)
    }

    fun getAllTrips(callback: (List<TripWithUserDetails>) -> Unit) {
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

    fun upsertTrip(trip: Trip, callback: () -> Unit) {
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

    fun deleteTrip(trip: Trip, callback: (Boolean) -> Unit) {
        firebaseModel.deleteTrip(trip, callback)
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

    fun uploadProfileImage(name: String, bitmap: Bitmap, callback: (url: String?) -> Unit) {
        this.uploadImage(name, "profile_photos", bitmap, callback)
    }

    fun uploadTripImage(name: String, bitmap: Bitmap, callback: (url: String?) -> Unit) {
        this.uploadImage(name, "trip_photos", bitmap, callback)
    }

    private fun uploadImage(
        name: String,
        folderName: String,
        bitmap: Bitmap,
        callback: (url: String?) -> Unit
    ) {
        storageService.uploadImage(name, folderName, bitmap, { url ->
            if (url != null) {
                callback(url)
            } else {
                callback(null)
            }
        }, {
            callback(null)
        })
    }
}