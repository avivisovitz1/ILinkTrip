package com.example.ilinktrip.entities

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ilinktrip.application.ILinkTripApplication
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue

@Entity(tableName = "user")
data class User(
    @PrimaryKey val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val gender: String,
    val phoneNumber: String,
    var avatarUrl: String,
    val password: String,
    var lastUpdated: Long? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: throw IllegalStateException("Error happened, do something"),
        parcel.readString() ?: throw IllegalStateException("Error happened, do something"),
        parcel.readString() ?: throw IllegalStateException("Error happened, do something"),
        parcel.readString() ?: throw IllegalStateException("Error happened, do something"),
        parcel.readInt() ?: throw IllegalStateException("Error happened, do something"),
        parcel.readString() ?: throw IllegalStateException("Error happened, do something"),
        parcel.readString() ?: throw IllegalStateException("Error happened, do something"),
        parcel.readString() ?: throw IllegalStateException("Error happened, do something"),
        parcel.readString() ?: throw IllegalStateException("Error happened, do something"),
        parcel.readLong() ?: throw IllegalStateException("Error happened, do something")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(email)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeInt(age)
        parcel.writeString(gender)
        parcel.writeString(phoneNumber)
        parcel.writeString(avatarUrl)
        parcel.writeString(password)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun getUserLastUpdated(): Long {
        return this.lastUpdated ?: 0L
    }

    fun setUserLastUpdated(lastUpdated: Long) {
        this.lastUpdated = lastUpdated
    }

    companion object CREATOR : Parcelable.Creator<User> {
        private const val USERS_LOCAL_LAST_UPDATE = "usersLocalLastUpdate"
        const val ID = "id"
        const val EMAIL = "email"
        const val FIRST_NAME = "firstName"
        const val LAST_NAME = "lastName"
        const val AGE = "age"
        const val GENDER = "gender"
        const val PHONE_NUMBER = "phoneNumber"
        const val AVATAR_URL = "avatarUrl"
        const val PASSWORD = "password"
        const val USER_LAST_UPDATED = "lastUpdated"

        fun fromJson(json: Map<String, Any>): User {
            val id = json[ID] as? String ?: ""
            val email = json[EMAIL] as? String ?: ""
            val firstName = json[FIRST_NAME] as? String ?: ""
            val lastName = json[LAST_NAME] as? String ?: ""
            val age = json[AGE].toString().toInt() as? Int ?: 0
            val gender = json[GENDER] as? String ?: "male"
            val phoneNumber = json[PHONE_NUMBER] as? String ?: ""
            val avatarUrl = json[AVATAR_URL] as? String ?: ""
            val password = json[PASSWORD] as? String ?: ""
            val time: Timestamp? = json[USER_LAST_UPDATED] as? Timestamp

            val user = User(
                id,
                email,
                firstName,
                lastName,
                age,
                gender,
                phoneNumber,
                avatarUrl,
                password
            )

            time?.let {
                user.lastUpdated = time.seconds
            }

            return user
        }

        var localLastUpdate: Long
            get() {
                return ILinkTripApplication.Globals.appContext?.getSharedPreferences(
                    "TAG",
                    Context.MODE_PRIVATE
                )?.getLong(USERS_LOCAL_LAST_UPDATE, 0L) ?: 0L
            }
            set(value) {
                ILinkTripApplication.Globals.appContext?.getSharedPreferences(
                    "TAG",
                    Context.MODE_PRIVATE
                )?.edit()?.putLong(USERS_LOCAL_LAST_UPDATE, value)?.apply()
            }

        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

    val json: Map<String, Any>
        get() {
            return hashMapOf(
                ID to id,
                EMAIL to email,
                FIRST_NAME to firstName,
                LAST_NAME to lastName,
                AGE to age,
                GENDER to gender,
                PHONE_NUMBER to phoneNumber,
                AVATAR_URL to avatarUrl,
                PASSWORD to password,
                USER_LAST_UPDATED to FieldValue.serverTimestamp()
            )
        }
}