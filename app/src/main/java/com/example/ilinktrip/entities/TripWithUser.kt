package com.example.ilinktrip.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.example.ilinktrip.entities.Trip
import com.example.ilinktrip.entities.User

data class TripWithUser(
    @Embedded
    val trip: Trip,
    @Relation(
        parentColumn = "userId",
        entityColumn = "id"
    )
    val userDetails: User
)
