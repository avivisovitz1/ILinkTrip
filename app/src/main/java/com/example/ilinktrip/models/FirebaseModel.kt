package com.example.ilinktrip.models

import android.util.Log
import com.example.ilinktrip.entities.Trip
import com.example.ilinktrip.entities.User
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.memoryCacheSettings
import com.google.firebase.ktx.Firebase

class FirebaseModel {
    private val db = Firebase.firestore

    companion object {
        const val USERS_COLLECTION = "users"
        const val TRIPS_COLLECTION = "trips"
        const val FAVORITE_TRAVELERS_COLLECTION = "favorite_travelers"
    }

    // WE USE ROOM SO WE DON'T USE FIREBASE'S PERSISTENT CACHE
    init {
        val settings = firestoreSettings {
            setLocalCacheSettings(memoryCacheSettings { })
        }

        db.firestoreSettings = settings
    }

    fun getUserDataByEmail(email: String, since: Long, callback: (user: User?) -> Unit) {
        db.collection(USERS_COLLECTION).whereEqualTo("email", email).whereGreaterThanOrEqualTo(
            User.USER_LAST_UPDATED,
            Timestamp(since, 0)
        ).get().addOnCompleteListener {
            when (it.isSuccessful) {
                true -> {
                    val usersRes = it.result.documents

                    if (usersRes.size > 0) {
                        callback(usersRes[0].data?.let { it1 -> User.fromJson(it1) })
                    } else {
                        callback(null)
                    }
                }

                false -> {
                    Log.e("TAG", it.exception?.message.toString())
                    callback(null)
                }
            }
        }.addOnFailureListener {
            Log.e("Error getting user", it.printStackTrace().toString())
        }
    }

    fun getUsers(
        usersIds: List<String>,
        since: Long,
        callback: (MutableMap<String, User>) -> Unit
    ) {
        val getUsersQuery = if (usersIds.isEmpty()) {
            db.collection(USERS_COLLECTION).whereGreaterThanOrEqualTo(
                User.USER_LAST_UPDATED,
                Timestamp(since, 0)
            )
        } else {
            db.collection(USERS_COLLECTION).whereIn("id", usersIds).whereGreaterThanOrEqualTo(
                User.USER_LAST_UPDATED,
                Timestamp(since, 0)
            )
        }

        getUsersQuery.get()
            .addOnCompleteListener {
                when (it.isSuccessful) {
                    true -> {
                        val users: MutableMap<String, User> = mutableMapOf()
                        for (userRes in it.result) {
                            val id = userRes.getString("id") ?: ""
                            users[id] = User.fromJson(userRes.data)
                        }
                        callback(users)
                    }

                    false -> callback(mutableMapOf())
                }
            }
    }

    fun upsertUser(user: User, callback: (isSuccessful: Boolean) -> Unit) {
        val userDto = user.json
        try {
            db.collection(USERS_COLLECTION).document(user.id).set(userDto).addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)

                }
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun deleteTrip(trip: Trip, callback: (Boolean) -> Unit) {
        db.collection(TRIPS_COLLECTION).document(trip.id).delete().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true)
            } else {
                callback(false)
            }
        }
    }

    fun getAllTrips(since: Long, callback: (List<Trip>) -> Unit) {
        db.collection(TRIPS_COLLECTION).whereGreaterThanOrEqualTo(
            Trip.TRIP_LAST_UPDATED,
            Timestamp(since, 0)
        ).get().addOnCompleteListener {
            when (it.isSuccessful) {
                true -> {
                    val trips: MutableList<Trip> = mutableListOf()
                    for (tripRes in it.result) {
                        trips.add(Trip.fromJson(tripRes.id, tripRes.data))
                    }

                    callback(trips)
                }

                false -> callback(mutableListOf())
            }
        }
    }

    fun upsertTrip(trip: Trip, callback: (Boolean) -> Unit) {
        db.collection(TRIPS_COLLECTION).document(trip.id).set(trip.json).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true)
            } else {
                callback(false)
            }
        }
    }

    fun addUserFavoriteTraveler(
        userId: String,
        favoriteTravelerId: String,
        callback: () -> Unit
    ) {
        db.collection(USERS_COLLECTION).document(userId)
            .collection(FAVORITE_TRAVELERS_COLLECTION)
            .document(favoriteTravelerId).set({}).addOnSuccessListener {
                callback()
            }
    }

    fun deleteUserFavoriteTraveler(
        userId: String,
        favoriteTravelerId: String,
        callback: () -> Unit
    ) {
        db.collection(USERS_COLLECTION).document(userId)
            .collection(FAVORITE_TRAVELERS_COLLECTION)
            .document(favoriteTravelerId).delete().addOnSuccessListener {
                callback()
            }
    }

    fun getUserFavoriteTravelers(userId: String, callback: (List<String>) -> Unit) {
        db.collection(USERS_COLLECTION).document(userId)
            .collection(FAVORITE_TRAVELERS_COLLECTION)
            .get().addOnCompleteListener {
                when (it.isSuccessful) {
                    true -> {
                        val userFavorites: MutableList<String> = mutableListOf()

                        for (row in it.result) {
                            val favoriteTravelerId = row.id ?: ""
                            userFavorites.add(favoriteTravelerId)
                        }

                        callback(userFavorites)
                    }

                    false -> callback(mutableListOf())
                }
            }
    }
}