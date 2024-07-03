package com.example.ilinktrip.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ilinktrip.dao.AppLocalDatabase
import com.example.ilinktrip.entities.FavoriteTraveler
import java.util.concurrent.Executors

class FavoriteTravelersModel private constructor() {
    private val localDb = AppLocalDatabase.db
    private val firebaseModel = FirebaseModel()
    private var executor = Executors.newSingleThreadExecutor()
    private var favoriteTravelers: LiveData<List<String>>? = null

    private var favoritesLoadingState: MutableLiveData<LoadingState> =
        MutableLiveData(LoadingState.NOT_LOADING)

    enum class LoadingState {
        LOADING,
        NOT_LOADING
    }

    companion object {
        private var _instance: FavoriteTravelersModel = FavoriteTravelersModel()

        fun instance(): FavoriteTravelersModel {
            return _instance
        }
    }

    fun addFavoriteTraveler(
        userId: String,
        favoriteTravelerId: String,
        callback: (Boolean) -> Unit
    ) {
        firebaseModel.addUserFavoriteTraveler(userId, favoriteTravelerId) { isSuccessful ->
            if (isSuccessful) {
                refetchUserFavoriteTravelers(userId)
                callback(true)
            } else {
                callback(false)
            }
        }
    }

    fun deleteFavoriteTraveler(
        userId: String,
        favoriteTravelerId: String,
        callback: (Boolean) -> Unit
    ) {
        firebaseModel.deleteUserFavoriteTraveler(userId, favoriteTravelerId) { isSuccessful ->
            if (isSuccessful) {
                executor.execute {
                    localDb.favoriteTravelerDao().delete(userId, favoriteTravelerId)
                }
                refetchUserFavoriteTravelers(userId)
                callback(true)
            } else {
                callback(false)
            }
        }
    }

    fun getUserFavoriteTravelers(userId: String): LiveData<List<String>> {
        if (favoriteTravelers == null) {
            favoriteTravelers = localDb.favoriteTravelerDao().getByUserId(userId)
            refetchUserFavoriteTravelers(userId)
        }

        return favoriteTravelers as LiveData<List<String>>
    }

    fun refetchUserFavoriteTravelers(userId: String) {
        favoritesLoadingState.value = LoadingState.LOADING
        var localLastUpdate = FavoriteTraveler.localLastUpdate

        FirebaseModel().getUserFavoriteTravelers(userId, localLastUpdate) { favorites ->
            executor.execute {
                for (favorite in favorites) {
                    localDb.favoriteTravelerDao().insertAll(favorite)
                    if (localLastUpdate < favorite.getFavoriteLastUpdated()) {
                        localLastUpdate = favorite.getFavoriteLastUpdated()
                    }
                }

                FavoriteTraveler.localLastUpdate = localLastUpdate
                favoritesLoadingState.postValue(LoadingState.NOT_LOADING)
            }
        }
    }
}