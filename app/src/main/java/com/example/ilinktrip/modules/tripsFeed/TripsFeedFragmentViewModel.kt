package com.example.ilinktrip.modules.tripsFeed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ilinktrip.models.CountryModel
import com.example.ilinktrip.entities.Trip
import com.example.ilinktrip.models.TripModel
import com.example.ilinktrip.entities.TripWithUser

class TripsFeedFragmentViewModel : ViewModel() {
    private val toastMessage = MutableLiveData<String>()
    private var tripsWithUsers: LiveData<List<TripWithUser>> = TripModel.instance().getAllTrips()
    private val _countryFlagsLiveData = MutableLiveData<Map<String, String>>()
    private val liveToastMessage: LiveData<String> get() = toastMessage
    private val countryFlagsLiveData: LiveData<Map<String, String>>
        get() = _countryFlagsLiveData

    fun getData(): LiveData<List<TripWithUser>> {
        return this.tripsWithUsers
    }

    fun getToastMessage(): LiveData<String> {
        return this.liveToastMessage
    }

    fun getCountriesFlags(): LiveData<Map<String, String>> {
        return this.countryFlagsLiveData
    }

    fun deleteTrip(trip: Trip) {
        TripModel.instance().deleteTrip(trip) { isSuccessful ->
            if (!isSuccessful) {
                toastMessage.value = "error occurred trying deleting the trip"
            }
        }
    }

    fun refreshData() {
        TripModel.instance().refreshAllTrips()
    }

    fun getLoadingState(): MutableLiveData<TripModel.LoadingState> {
        return TripModel.instance().tripsLoadingState
    }

    fun fetchTripsCountriesFlags(tripsWithUsers: List<TripWithUser>) {
        val countries = tripsWithUsers.map { tripWithUser ->
            tripWithUser.trip.country
        }.distinct()

        CountryModel.instance().fetchCountryFlags(countries).observeForever { flags ->
            _countryFlagsLiveData.postValue(flags)
        }
    }
}