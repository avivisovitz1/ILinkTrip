package com.example.ilinktrip.models

import android.graphics.Bitmap
import android.os.Looper
import androidx.core.os.HandlerCompat
import androidx.lifecycle.MutableLiveData
import com.example.ilinktrip.dao.AppLocalDatabase
import com.example.ilinktrip.entities.User
import com.example.ilinktrip.services.AuthService
import java.util.concurrent.Executors

class UserModel {
    private val localDb = AppLocalDatabase.db
    private val firebaseModel = FirebaseModel()
    private val mediaModel = MediaModel()
    private val authenticationService = AuthService()
    private var executor = Executors.newSingleThreadExecutor()
    private var mainHandler = HandlerCompat.createAsync(Looper.getMainLooper())
    private var currentUser: MutableLiveData<User?> = MutableLiveData()

    companion object {
        const val PROFILE_FOLDER_NAME = "profile_photos"
        private var _instance: UserModel = UserModel()

        fun instance(): UserModel {
            return _instance
        }
    }

    fun getCurrentUser(): MutableLiveData<User?> {
        return this.currentUser
    }

    fun refetchCurrentUser(callback: (Boolean) -> Unit) {
        authenticationService.getCurrentUser { firebaseUser ->
            if (firebaseUser != null) {
                firebaseModel.getUserDataByEmail(
                    firebaseUser.email!!,
                    User.localLastUpdate
                ) { user ->
                    executor.execute {
                        if (user == null) {
                            val user = localDb.userDao().getByEmail(firebaseUser.email!!)
                            currentUser?.postValue(user)
                            callback(true)
                        } else {
                            currentUser.postValue(user)
                            callback(true)
                        }
                    }
                }
            } else {
                callback(false)
            }
        }
    }

    fun signIn(email: String, password: String, callback: (user: User?) -> Unit) {
        authenticationService.signIn(email, password) {
            if (it != null) {
                val localLastUpdate = User.localLastUpdate

                firebaseModel.getUserDataByEmail(it.email!!, localLastUpdate) { user ->
                    var time = localLastUpdate
                    var userDetails: User
                    executor.execute {
                        if (user != null) {
                            localDb.userDao().insertAll(user)

                            if (time < user.getUserLastUpdated()) {
                                time = user.getUserLastUpdated()
                            }

                            User.localLastUpdate = time
                            userDetails = localDb.userDao().getById(user.id)
                        } else {
                            userDetails = localDb.userDao().getByEmail(email)
                        }

                        mainHandler.post {
                            callback(userDetails)
                        }
                    }
                }
            } else {
                callback(null)
            }
        }

    }

    fun signUp(userData: User, callback: (Boolean) -> Unit) {
        authenticationService.signUp(userData.email, userData.password) {
            if (it != null) {
                callback(true)
            } else {
                callback(false)
            }
        }
    }

    fun signOut() {
        authenticationService.signOut()
    }


    fun getAllUsers(callback: (List<User>) -> Unit) {
        val localLastUpdate = User.localLastUpdate
        firebaseModel.getUsers(listOf(), localLastUpdate) { users ->
            var time = localLastUpdate
            executor.execute {
                for (user in users) {
                    localDb.userDao().insertAll(user.value)
                    if (time < user.value.getUserLastUpdated()) {
                        time = user.value.getUserLastUpdated()
                    }
                }
                User.localLastUpdate = time
                val users = localDb.userDao().getAll()
                mainHandler.post {
                    callback(users)
                }
            }
        }
    }

    fun upsertUserDetails(user: User, callback: (Boolean) -> Unit) {
        firebaseModel.upsertUser(user) {
            if (it) {
                refetchCurrentUser { isSuccessful ->
                    mainHandler.post {
                        if (isSuccessful) {
                            callback(true)
                        }
                    }
                }
            }

        }
    }

    fun uploadProfileImage(name: String, bitmap: Bitmap, callback: (url: String?) -> Unit) {
        this.mediaModel.uploadImage(name, PROFILE_FOLDER_NAME, bitmap, callback)
    }
}