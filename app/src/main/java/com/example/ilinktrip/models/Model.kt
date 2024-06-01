package com.example.ilinktrip.models

import android.os.Looper
import androidx.core.os.HandlerCompat
import com.example.ilinktrip.dao.AppLocalDatabase
import java.util.concurrent.Executors

class Model private constructor() {
    private val database = AppLocalDatabase.db
    private var executor = Executors.newSingleThreadExecutor()
    private var mainHandler = HandlerCompat.createAsync(Looper.getMainLooper())

    companion object {
        private var _instance: Model = Model()

        fun instance(): Model {
            return _instance
        }
    }

    fun getAllUsers(callback: (List<User>) -> Unit) {
        executor.execute {
            val users = database.userDao().getAll()


            mainHandler.post {
                callback(users)
            }
        }
    }

    fun getAllTrips(callback: (List<Trip>) -> Unit) {
        executor.execute {
            val trips = database.tripDao().getAll()

            try {
                Thread.sleep(5000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            mainHandler.post {
                callback(trips)
            }
        }
    }

    fun addTrip(trip: Trip, callback: () -> Unit) {
        executor.execute {
            database.tripDao().insertAll(trip)

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