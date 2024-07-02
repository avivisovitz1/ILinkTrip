package com.example.ilinktrip.modules.tripsFeed.tripDetails

import androidx.lifecycle.ViewModel
import com.example.ilinktrip.models.Model

class TripDetailsFragmentViewModel : ViewModel() {
    fun addToFavorites(currentUserId: String, travelerId: String, callback: () -> Unit) {
        Model.instance().addFavoriteTraveler(currentUserId, travelerId) {
            callback()
        }
    }

    fun deleteFromFavorites(currentUserId: String, travelerId: String, callback: () -> Unit) {
        Model.instance().deleteFavoriteTraveler(currentUserId, travelerId) {
            callback()
        }
    }
}