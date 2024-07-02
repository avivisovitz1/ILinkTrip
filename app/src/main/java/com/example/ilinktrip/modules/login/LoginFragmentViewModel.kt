package com.example.ilinktrip.modules.login

import androidx.lifecycle.ViewModel
import com.example.ilinktrip.entities.User
import com.example.ilinktrip.models.UserModel

class LoginFragmentViewModel : ViewModel() {
    fun singIn(email: String, password: String, callback: (User?) -> Unit) {
        UserModel.instance().signIn(email, password) { user ->
            callback(user)
        }
    }
}