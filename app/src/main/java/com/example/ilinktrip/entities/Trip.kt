package com.example.ilinktrip.entities

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ilinktrip.application.ILinkTripApplication
import com.example.ilinktrip.utils.LocalDateTypeConverter
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import org.threeten.bp.LocalDate
import java.util.UUID

//
@Entity
//    tableName = "trip", foreignKeys = [
//        ForeignKey(
//            entity = User::class,
//            parentColumns = arrayOf("id"),
//            childColumns = arrayOf("userId"),
//            onDelete = ForeignKey.CASCADE,
//        ),
//    ]
//)
data class Trip(
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),
    var userId: String,
    var country: String,
    var place: String,
    var startsAt: LocalDate,
    var durationInWeeks: Int,
    var avatarUrl: String,
    var isDone: Boolean,
    var lastUpdated: Long? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        LocalDate.parse(parcel.readString().toString() ?: ""),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(country)
        parcel.writeString(place)
        parcel.writeInt(durationInWeeks)
        parcel.writeString(avatarUrl)
        parcel.writeByte(if (isDone) 1 else 0)
        parcel.writeString(id)
        lastUpdated?.let { parcel.writeLong(it) }
    }

    fun getTripLastUpdated(): Long {
        return this.lastUpdated ?: 0L
    }

    fun setTripLastUpdated(lastUpdated: Long) {
        this.lastUpdated = lastUpdated
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Trip> {
        const val ID = "id"
        const val USER_ID = "userId"
        const val COUNTRY = "country"
        const val PLACE = "place"
        const val STARTS_AT = "startsAt"
        const val DURATION = "durationInWeeks"
        const val AVATAR_URL = "avatarUrl"
        const val IS_DONE = "isDone"
        const val TRIP_LAST_UPDATED = "lastUpdated"
        const val TRIPS_LOCAL_LAST_UPDATE = "tripsLocalLastUpdate"

        var localLastUpdate: Long
            get() {
                return ILinkTripApplication.Globals.appContext?.getSharedPreferences(
                    "TAG",
                    Context.MODE_PRIVATE
                )?.getLong(TRIPS_LOCAL_LAST_UPDATE, 0L) ?: 0L
            }
            set(value) {
                ILinkTripApplication.Globals.appContext?.getSharedPreferences(
                    "TAG",
                    Context.MODE_PRIVATE
                )?.edit()?.putLong(TRIPS_LOCAL_LAST_UPDATE, value)?.apply()
            }

        fun fromJson(id: String, json: Map<String, Any>): Trip {
            val id = id ?: ""
            val userId = json[USER_ID] as? String ?: ""
            val country = json[COUNTRY] as? String ?: ""
            val place = json[PLACE] as? String ?: ""
            val startsAt = json[STARTS_AT].toString() ?: ""
            val durationInWeeks = json[DURATION].toString().toInt() as? Int ?: 0
            val isDone = json[IS_DONE] as? Boolean ?: false
            val avatarUrl = json[AVATAR_URL] as? String ?: ""
            val time: Timestamp? = json[TRIP_LAST_UPDATED] as? Timestamp

            val date =
                if (startsAt != "") LocalDateTypeConverter().dateToLocalDate(startsAt) else LocalDate.now()

            val trip = Trip(
                id,
                userId,
                country,
                place,
                date,
                durationInWeeks,
                avatarUrl,
                isDone
            )

            time?.let {
                trip.lastUpdated = time.seconds
            }

            return trip
        }

        override fun createFromParcel(parcel: Parcel): Trip {
            return Trip(parcel)
        }

        override fun newArray(size: Int): Array<Trip?> {
            return arrayOfNulls(size)
        }
    }

    val json: Map<String, Any>
        get() {
            return hashMapOf(
                USER_ID to userId,
                COUNTRY to country,
                PLACE to place,
                STARTS_AT to LocalDateTypeConverter().fromLocalDate(startsAt),
                DURATION to durationInWeeks,
                AVATAR_URL to avatarUrl,
                IS_DONE to isDone,
                TRIP_LAST_UPDATED to FieldValue.serverTimestamp()
            )
        }
}
