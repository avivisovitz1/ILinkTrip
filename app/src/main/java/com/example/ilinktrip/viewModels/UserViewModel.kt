package com.example.ilinktrip.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ilinktrip.models.FavoriteTravelersModel
import com.example.ilinktrip.entities.User
import com.example.ilinktrip.models.UserModel

class UserViewModel : ViewModel() {
    private val _currentUserDetails = UserModel.instance().getCurrentUser()
    private val _favoriteUserIds = MutableLiveData<List<String>>(listOf())
    private val toastMessage = MutableLiveData<String>()
    private val favoriteUserIds: LiveData<List<String>>
        get() = _favoriteUserIds

    private val currentUserDetails: LiveData<User?> get() = _currentUserDetails
    private val liveToastMessage: LiveData<String> get() = toastMessage


    init {
        refetchCurrentUser()

        currentUserDetails.observeForever { currentUserDetails ->
            FavoriteTravelersModel.instance()
                .getUserFavoriteTravelers(currentUserDetails?.id ?: "")
                .observeForever { ids ->
                    _favoriteUserIds.value = ids
                }
        }
    }

    fun getCurrentUser(): LiveData<User?> {
        return currentUserDetails
    }

    fun getUserFavoriteUsersIds(): LiveData<List<String>> {
        return favoriteUserIds
    }

    fun getToastMessage(): LiveData<String> {
        return liveToastMessage
    }

    private fun refetchCurrentUser() {
        UserModel.instance().refetchCurrentUser { isSuccessful ->
            if (!isSuccessful) {
                toastMessage.postValue("error getting user details")
            }
        }
    }
}