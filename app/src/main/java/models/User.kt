package models

data class User(
    val email: String,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val gender: String,
    val phoneNumber: String
)