package com.example.ilinktrip.models

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ilinktrip.dao.AppLocalDatabase
import com.example.ilinktrip.entities.Trip
import com.example.ilinktrip.entities.TripWithUser
import com.example.ilinktrip.entities.User
import java.util.concurrent.Executors

class TripModel {
    private val localDb = AppLocalDatabase.db
    private val firebaseModel = FirebaseModel()
    private val mediaModel = MediaModel()
    private var tripsWithUsersList: LiveData<List<TripWithUser>>? = null
    var tripsLoadingState: MutableLiveData<LoadingState> =
        MutableLiveData(LoadingState.NOT_LOADING)

    private var executor = Executors.newSingleThreadExecutor()

    enum class LoadingState {
        LOADING,
        NOT_LOADING
    }


    companion object {
        private var _instance: TripModel = TripModel()

        fun instance(): TripModel {
            return _instance
        }
    }

    fun getAllTrips(): LiveData<List<TripWithUser>> {
        if (tripsWithUsersList == null) {
            tripsWithUsersList = this.localDb.tripWithUserDao().getAll()
            refreshAllTrips()
        }
        return tripsWithUsersList as LiveData<List<TripWithUser>>
    }

    fun refreshAllTrips() {
        tripsLoadingState.value = LoadingState.LOADING
        val tripLocalLastUpdate = Trip.localLastUpdate
        val userLocalLastUpdate = User.localLastUpdate
        firebaseModel.getAllTrips(tripLocalLastUpdate) { trips ->
             firebaseModel.getUsers(listOf(), userLocalLastUpdate) { users ->
                processSyncData(users, trips)
            }
        }
    }

    private fun processSyncData(users: MutableMap<String, User>, trips: List<Trip>) {
        executor.execute {
            localDb.runInTransaction {
                var tripTime = Trip.localLastUpdate
                var userTime = User.localLastUpdate

                for (trip in trips) {
                    localDb.tripDao().insertAll(trip)
                    if (tripTime < trip.getTripLastUpdated()) {
                        tripTime = trip.getTripLastUpdated()
                    }
                }


                for (user in users) {
                    localDb.userDao().insertAll(user.value)
                    if (userTime < user.value.getUserLastUpdated()) {
                        userTime = user.value.getUserLastUpdated()
                    }
                }

                Trip.localLastUpdate = tripTime
                User.localLastUpdate = userTime
                tripsLoadingState.postValue(LoadingState.NOT_LOADING)
            }
        }
    }

    fun upsertTrip(trip: Trip, callback: (Boolean) -> Unit) {
        firebaseModel.upsertTrip(trip) { isSuccessful ->
            if (isSuccessful) {
                refreshAllTrips()
                callback(true)
            } else {
                callback(false)
            }
        }

    }

    fun deleteTrip(trip: Trip, callback: (Boolean) -> Unit) {
        firebaseModel.deleteTrip(trip) { isSuccessful ->
            if (isSuccessful) {
                executor.execute {
                    localDb.tripDao().delete(trip)
                }
                refreshAllTrips()
                callback(true)
            }
            callback(false)
        }
    }

    fun uploadTripImage(name: String, bitmap: Bitmap, callback: (url: String?) -> Unit) {
        this.mediaModel.uploadImage(name, "trip_photos", bitmap, callback)
    }
}