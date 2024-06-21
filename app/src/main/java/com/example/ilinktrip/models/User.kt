package com.example.ilinktrip.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val gender: String,
    val phoneNumber: String,
    val avatarUrl: String,
    val password: String
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
        parcel.readString() ?: throw IllegalStateException("Error happened, do something")
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

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}