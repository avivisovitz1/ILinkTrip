package com.example.ilinktrip.interfaces

import com.example.ilinktrip.entities.Country
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CountryApi {
    @GET("all?:fields=name,flags")
    fun getAllCountries(): Call<List<Country>>

    @GET("name/{name}?:fields=name,flags")
    fun getCountryByName(@Path("name") name: String): Call<List<Country>>
}