package com.example.ilinktrip.modules.registerUser

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.example.ilinktrip.entities.User
import com.example.ilinktrip.models.TripModel
import com.example.ilinktrip.models.UserModel

class RegisterFragmentViewModel : ViewModel() {
    fun signUp(user: User, bitmap: Bitmap, callback: (Boolean) -> Unit) {
        UserModel.instance().signUp(user) { isSuccessful ->
            if (isSuccessful) {
                updateUserData(user, bitmap) {
                    if (it) {
                        callback(true)
                    } else {
                        callback(false)
                    }
                }
            } else {
                callback(false)
            }
        }
    }

    fun updateUserData(user: User, bitmap: Bitmap, callback: (Boolean) -> Unit) {
        UserModel.instance().uploadProfileImage(user.id, bitmap) { imageUrl ->
            if (imageUrl != null) {
                user.avatarUrl = imageUrl
                UserModel.instance().upsertUserDetails(user) { isSuccessful ->
                    if (isSuccessful) {
                        TripModel.instance().refreshAllTrips()
                        callback(true)
                    }
                }
            } else {
                callback(false)
            }
        }
    }
}