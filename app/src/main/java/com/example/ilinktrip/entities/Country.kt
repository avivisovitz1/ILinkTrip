package com.example.ilinktrip.entities

class Country(
    val name: CountryName,
    val flags: CountryFlag
)

data class CountryName(
    val common: String
)

data class CountryFlag(
    val png: String,
    val svg: String
)