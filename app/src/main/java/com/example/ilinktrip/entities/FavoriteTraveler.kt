package com.example.ilinktrip.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.ilinktrip.entities.User
import java.util.UUID

@Entity(
    tableName = "favorite_traveler", foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("userId"),
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("favoriteUserId"),
            onDelete = ForeignKey.CASCADE,
        ),
    ]
)
data class FavoriteTraveler(
    @ColumnInfo(name = "userId")
    val userId: String,
    @ColumnInfo(name = "favoriteUserId")
    val favoriteUserId: String
) {
    @PrimaryKey
    var id: String = UUID.randomUUID().toString()
}