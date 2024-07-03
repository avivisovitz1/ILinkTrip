package com.example.ilinktrip.entities

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ilinktrip.application.ILinkTripApplication
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import java.util.UUID

@Entity(
    tableName = "favorite_traveler"
)
data class FavoriteTraveler(
    @ColumnInfo(name = "userId")
    var userId: String,
    @ColumnInfo(name = "favoriteUserId")
    var favoriteUserId: String,
    var lastUpdated: Long? = null
) {
    @PrimaryKey
    var id: String = UUID.randomUUID().toString()

    fun getFavoriteLastUpdated(): Long {
        return this.lastUpdated ?: 0L
    }

    companion object {
        private const val FAVORITES_LOCAL_LAST_UPDATE_DATE = "favoritesLastUpdateDate"
        const val USER_ID = "userId"
        const val TRAVELER_ID = "favoriteUserId"
        const val FAVORITE_LAST_UPDATED = "lastUpdated"

        var localLastUpdate: Long
            get() {
                return ILinkTripApplication.Globals.appContext?.getSharedPreferences(
                    "TAG",
                    Context.MODE_PRIVATE
                )?.getLong(FAVORITES_LOCAL_LAST_UPDATE_DATE, 0L) ?: 0L
            }
            set(value) {
                ILinkTripApplication.Globals.appContext?.getSharedPreferences(
                    "TAG",
                    Context.MODE_PRIVATE
                )?.edit()?.putLong(FAVORITES_LOCAL_LAST_UPDATE_DATE, value)?.apply()
            }


        fun fromJson(
            userId: String,
            favoriteUserId: String,
            json: Map<String, Any>
        ): FavoriteTraveler {
            val time: Timestamp? = json[FAVORITE_LAST_UPDATED] as? Timestamp

            val favorite = FavoriteTraveler(
                userId,
                favoriteUserId,
            )

            time?.let {
                favorite.lastUpdated = time.seconds
            }

            return favorite
        }


    }

    val json: Map<String, Any>
        get() {
            return hashMapOf(
                FAVORITE_LAST_UPDATED to FieldValue.serverTimestamp(),
            )
        }
}