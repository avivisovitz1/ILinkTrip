package com.example.ilinktrip.models

import androidx.room.ColumnInfo
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
    val userId: String,
    val country: String,
    val place: String,
    val startsAt: LocalDate,
    val durationInWeeks: Int,
    val isDone: Boolean
) {
    @PrimaryKey
    var id: String = UUID.randomUUID().toString()
}
