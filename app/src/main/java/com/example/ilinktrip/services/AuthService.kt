package com.example.ilinktrip.services

import android.util.Log
import com.example.ilinktrip.models.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthService {
    private val auth = Firebase.auth

    fun getCurrentUser(callback: (user: FirebaseUser?) -> Unit) {
        val user = auth.currentUser
        callback(user)
    }

    fun signIn(email: String, password: String, callback: (user: FirebaseUser?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                callback(user)
            } else {
                callback(null)
            }
        }
    }

    fun signUp(email: String, password: String, callback: (user: FirebaseUser?) -> Unit) {
        var user: FirebaseUser? = null

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                user = auth.currentUser
                callback(user)
            } else {
//                Log.e("sign up error", it.message ?: "")
                callback(null)
            }
        }
    }

    fun signOut() {
        auth.signOut()
    }
}
