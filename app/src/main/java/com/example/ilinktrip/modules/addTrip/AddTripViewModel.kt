package com.example.ilinktrip.modules.addTrip

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ilinktrip.entities.Country
import com.example.ilinktrip.entities.Trip
import com.example.ilinktrip.models.TripModel
import com.example.ilinktrip.entities.User
import com.example.ilinktrip.models.CountryModel
import org.threeten.bp.LocalDate
import java.util.UUID

class AddTripViewModel : ViewModel() {
    private val toastMessage = MutableLiveData<String>()
    private var countriesList = MutableLiveData<List<Country>>(listOf())
    private val liveToastMessage: LiveData<String> get() = toastMessage
    private val liveCountriesList: LiveData<List<Country>>? get() = countriesList

    init {
        CountryModel.instance().getAllCountries() { isSuccessful ->
            if (!isSuccessful) {
                toastMessage.value = "error loading countries list"
            }
        }
            ?.observeForever { countriesList ->

                this.countriesList.value = countriesList
            }
    }

    fun getToastMessage(): LiveData<String> {
        return this.liveToastMessage
    }

    fun getCountriesList(): LiveData<List<Country>>? {
        return liveCountriesList
    }

    fun upsertTrip(
        user: User,
        tripId: String?, country: String, place: String,
        startsAt: LocalDate, duration: Int, isDone: Boolean, bitmap: Bitmap,
        callback: (Boolean) -> Unit
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
                        callback(true)
                    } else {
                        toastMessage.value = "error saving trip"
                        callback(false)
                    }
                }
            } else {
                toastMessage.value = "error saving trip"
                callback(false)
            }
        }
    }
}