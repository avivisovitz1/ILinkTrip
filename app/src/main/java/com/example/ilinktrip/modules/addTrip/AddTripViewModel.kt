package com.example.ilinktrip.modules.addTrip

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ilinktrip.entities.Trip
import com.example.ilinktrip.models.TripModel
import com.example.ilinktrip.entities.User
import org.threeten.bp.LocalDate
import java.util.UUID

class AddTripViewModel : ViewModel() {
    private val toastMessage = MutableLiveData<String>()
    private val liveToastMessage: LiveData<String> get() = toastMessage


    fun getToastMessage(): LiveData<String> {
        return this.liveToastMessage
    }

    fun upsertTrip(
        user: User,
        tripId: String?, country: String, place: String,
        startsAt: LocalDate, duration: Int, isDone: Boolean, bitmap: Bitmap,
        callback: () -> Unit
    ) {
        val trip = Trip(
            tripId ?: UUID.randomUUID().toString(), user.id,
            country, place, startsAt, duration, "", isDone
        )

        TripModel.instance().uploadTripImage(trip.id, bitmap) { imageUrl ->
            if (imageUrl != null) {
                trip.avatarUrl = imageUrl
                TripModel.instance().upsertTrip(trip) { isSuccessful ->
                    if (isSuccessful) {
                        toastMessage.value = "trip details saved"
                        callback()
                    } else {
                        toastMessage.value = "error saving trip"
                    }
                }
            } else {
                toastMessage.value = "error saving trip"
            }
        }
    }
}