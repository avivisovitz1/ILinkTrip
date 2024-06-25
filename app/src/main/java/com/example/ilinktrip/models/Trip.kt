package com.example.ilinktrip.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDate
import java.util.UUID


@Entity(
    tableName = "trip", foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("userId"),
            onDelete = ForeignKey.CASCADE,
        ),
    ]
)
data class Trip(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val country: String,
    val place: String,
    val startsAt: LocalDate,
    val durationInWeeks: Int,
    var avatarUrl: String,
    val isDone: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        LocalDate.parse(parcel.readString().toString() ?: ""),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(country)
        parcel.writeString(place)
        parcel.writeInt(durationInWeeks)
        parcel.writeString(avatarUrl)
        parcel.writeByte(if (isDone) 1 else 0)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Trip> {
        override fun createFromParcel(parcel: Parcel): Trip {
            return Trip(parcel)
        }

        override fun newArray(size: Int): Array<Trip?> {
            return arrayOfNulls(size)
        }
    }
}
