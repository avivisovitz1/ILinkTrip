package com.example.ilinktrip.modules.tripsFeed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ilinktrip.models.CountryModel
import com.example.ilinktrip.entities.Trip
import com.example.ilinktrip.models.TripModel
import com.example.ilinktrip.entities.TripWithUser
import com.example.ilinktrip.viewModels.UserViewModel

class TripsFeedFragmentViewModel(private val userViewModel: UserViewModel) : ViewModel(),
    ViewModelProvider.Factory {
    private val toastMessage = MutableLiveData<String>()
    private var tripsWithUsers: LiveData<List<TripWithUser>> = TripModel.instance().getAllTrips()
    private val userTrips = MediatorLiveData<List<TripWithUser>>()
    private val _countryFlagsLiveData = MutableLiveData<Map<String, String>>()
    private val liveToastMessage: LiveData<String> get() = toastMessage
    private val countryFlagsLiveData: LiveData<Map<String, String>>
        get() = _countryFlagsLiveData

    init {
        userTrips.addSource(userViewModel.getCurrentUser()) { filterTrips() }
        userTrips.addSource(tripsWithUsers) { filterTrips() }
    }

    private fun filterTrips() {
        val user = userViewModel.getCurrentUser().value
        val allTrips = tripsWithUsers.value

        if (allTrips != null && user != null) {
            val filteredTrips = allTrips.filter { it.userDetails.id == user.id }
            userTrips.value = filteredTrips
        }
    }

    fun getData(onlyUserTrips: Boolean): LiveData<List<TripWithUser>> {
        if (onlyUserTrips) {
            return userTrips
        } else {
            return tripsWithUsers
        }
    }

    fun getToastMessage(): LiveData<String> {
        return this.liveToastMessage
    }

    fun getCountriesFlags(): LiveData<Map<String, String>> {
        return this.countryFlagsLiveData
    }

    fun deleteTrip(trip: Trip) {
        TripModel.instance().deleteTrip(trip) { isSuccessful ->
            if (isSuccessful) {
                toastMessage.value = "trip deleted"
            } else {
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

class TripsFeedFragmentViewModelFactory(
    private val userViewModel: UserViewModel
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TripsFeedFragmentViewModel(userViewModel) as T
    }
}