package com.example.ilinktrip.models

class Model private constructor() {
    private val firebaseModel = FirebaseModel()

    companion object {
        private var _instance: Model = Model()

        fun instance(): Model {
            return _instance
        }
    }

    fun addFavoriteTraveler(userId: String, favoriteTravelerId: String, callback: () -> Unit) {
        firebaseModel.addUserFavoriteTraveler(userId, favoriteTravelerId, callback)
    }

    fun deleteFavoriteTraveler(userId: String, favoriteTravelerId: String, callback: () -> Unit) {
        firebaseModel.deleteUserFavoriteTraveler(userId, favoriteTravelerId, callback)
    }

    fun getUserFavoriteTravelers(userId: String, callback: (List<String>) -> Unit) {
        firebaseModel.getUserFavoriteTravelers(userId, callback)
    }
}