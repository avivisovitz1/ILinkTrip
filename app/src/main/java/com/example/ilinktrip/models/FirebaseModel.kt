package com.example.ilinktrip.models

import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.memoryCacheSettings
import com.google.firebase.ktx.Firebase
import org.threeten.bp.LocalDate

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

    fun getUserDataByEmail(email: String, callback: (user: User?) -> Unit) {
        db.collection(USERS_COLLECTION).whereEqualTo("email", email).get().addOnCompleteListener {
            when (it.isSuccessful) {
                true -> {
                    val userRes = it.result.documents[0]
                    val id = userRes.getString("id") ?: ""
                    val email = userRes.getString("email") ?: ""
                    val firstName = userRes.getString("firstName") ?: ""
                    val lastName = userRes.getString("lastName") ?: ""
                    val age = userRes.getLong("age")?.toInt() ?: 0
                    val gender = userRes.getString("gender") ?: ""
                    val phoneNumber = userRes.getString("phoneNumber") ?: ""
                    val avatarUrl = userRes.getString("avatarUrl") ?: ""
                    val password = userRes.getString("password") ?: ""
                    val user = User(
                        id, email, firstName, lastName, age, gender,
                        phoneNumber, avatarUrl, password
                    )

                    callback(user)
                }

                false -> callback(null)
            }
        }
    }

    fun getUsers(usersIds: List<String>, callback: (MutableMap<String, User>) -> Unit) {
        val getUsersQuery = if (usersIds.isEmpty()) {
            db.collection(USERS_COLLECTION)
        } else {
            db.collection(USERS_COLLECTION).whereIn("id", usersIds)
        }

        getUsersQuery.get()
            .addOnCompleteListener {
                when (it.isSuccessful) {
                    true -> {
                        val users: MutableMap<String, User> = mutableMapOf()
                        for (userRes in it.result) {
                            val id = userRes.getString("id") ?: ""
                            val email = userRes.getString("email") ?: ""
                            val firstName = userRes.getString("firstName") ?: ""
                            val lastName = userRes.getString("lastName") ?: ""
                            val age = userRes.getLong("age")?.toInt() ?: 0
                            val gender = userRes.getString("gender") ?: ""
                            val phoneNumber = userRes.getString("phoneNumber") ?: ""
                            val avatarUrl = userRes.getString("avatarUrl") ?: ""
                            val password = userRes.getString("password") ?: ""
                            val user = User(
                                id, email, firstName, lastName, age, gender,
                                phoneNumber, avatarUrl, password
                            )

                            users[id] = user
                        }
                        callback(users)
                    }

                    false -> callback(mutableMapOf())
                }
            }
    }

    fun upsertUser(user: User, callback: (isSuccessful: Boolean) -> Unit) {
        val userDto = hashMapOf(
            "id" to user.id,
            "email" to user.email,
            "firstName" to user.firstName,
            "lastName" to user.lastName,
            "age" to user.age,
            "gender" to user.gender,
            "phoneNumber" to user.phoneNumber,
            "avatarUrl" to user.avatarUrl,
            "password" to user.password
        )

        db.collection(USERS_COLLECTION).document(user.id).set(userDto).addOnSuccessListener {
            callback(true)
        }.addOnFailureListener {
            callback(false)
        }
    }

    fun getAllTrips(callback: (List<TripWithUserDetails>) -> Unit) {
        db.collection(TRIPS_COLLECTION).get().addOnCompleteListener {
            when (it.isSuccessful) {
                true -> {
                    val trips: MutableList<Trip> = mutableListOf()
                    val usersIds: MutableList<String> = mutableListOf()
                    for (tripRes in it.result) {
                        val userId = tripRes.getString("userId") ?: ""
                        val country = tripRes.getString("country") ?: ""
                        val place = tripRes.getString("place") ?: ""
                        val startsAt = tripRes.getString("startsAt") ?: ""
                        val durationInWeeks = tripRes.getLong("durationInWeeks")?.toInt() ?: 0
                        val isDone = tripRes.getBoolean("isDone") ?: false


                        val trip = Trip(
                            userId,
                            country,
                            place,
                            LocalDateTypeConverter().dateToLocalDate(startsAt) ?: LocalDate.now(),
                            durationInWeeks,
                            isDone
                        )

                        usersIds.add(userId)
                        trips.add(trip)
                    }

                    this.getUsers(usersIds.distinct()) {
                        val tripsWithUsers = trips.mapNotNull { trip ->
                            val user = it[trip.userId]
                            user?.let { TripWithUserDetails(trip, user) }
                        }
                        callback(tripsWithUsers)
                    }
                }

                false -> callback(mutableListOf())
            }
        }
    }

    fun addTrip(trip: Trip, callback: () -> Unit) {
        val startsAtDate = LocalDateTypeConverter().fromLocalDate(trip.startsAt)

        val tripDto = hashMapOf(
            "userId" to trip.userId,
            "country" to trip.country,
            "place" to trip.place,
            "startsAt" to startsAtDate,
            "durationInWeeks" to trip.durationInWeeks,
            "isDone" to trip.isDone
        )

        db.collection(TRIPS_COLLECTION).document(trip.id).set(tripDto).addOnSuccessListener {
            callback()
        }
    }

    fun addUserFavoriteTraveler(userId: String, favoriteTravelerId: String, callback: () -> Unit) {
        db.collection(USERS_COLLECTION).document(userId).collection(FAVORITE_TRAVELERS_COLLECTION)
            .document(favoriteTravelerId).set({}).addOnSuccessListener {
                callback()
            }
    }

    fun deleteUserFavoriteTraveler(
        userId: String,
        favoriteTravelerId: String,
        callback: () -> Unit
    ) {
        db.collection(USERS_COLLECTION).document(userId).collection(FAVORITE_TRAVELERS_COLLECTION)
            .document(favoriteTravelerId).delete().addOnSuccessListener {
                callback()
            }
    }

    fun getUserFavoriteTravelers(userId: String, callback: (List<String>) -> Unit) {
        db.collection(USERS_COLLECTION).document(userId).collection(FAVORITE_TRAVELERS_COLLECTION)
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