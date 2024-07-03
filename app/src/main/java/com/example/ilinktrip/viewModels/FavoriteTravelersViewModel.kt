package com.example.ilinktrip.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ilinktrip.models.FavoriteTravelersModel

class FavoriteTravelersViewModel : ViewModel() {
    private val toastMessage = MutableLiveData<String>()
    private val liveToastMessage: LiveData<String> get() = toastMessage

    fun getToastMessage(): LiveData<String> {
        return this.liveToastMessage
    }

    fun addToFavorites(currentUserId: String, travelerId: String) {
        FavoriteTravelersModel.instance()
            .addFavoriteTraveler(currentUserId, travelerId) { isSuccessful ->
                if (isSuccessful) {
                    toastMessage.value = "added to favorites"
                } else {
                    toastMessage.value = "failed to add to favorites"
                }
            }
    }

    fun deleteFromFavorites(currentUserId: String, travelerId: String) {
        FavoriteTravelersModel.instance()
            .deleteFavoriteTraveler(currentUserId, travelerId) { isSuccessful ->
                if (isSuccessful) {
                    toastMessage.value = "removed from favorites"
                } else {
                    toastMessage.value = "failed to remove from favorites"
                }
            }
    }
}