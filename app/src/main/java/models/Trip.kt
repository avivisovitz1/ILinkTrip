package models

import org.threeten.bp.LocalDate


data class Trip(
    val userName: String,
    val country: String,
    val place: String,
    val startsAt: LocalDate,
    val durationInWeeks: Int,
    val isDone: Boolean
)
