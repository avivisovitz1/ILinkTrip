package com.example.ilinktrip.interfaces

import com.example.ilinktrip.models.Country
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CountryApi {
    @GET("all?:fields=name,flags")
    fun getAllCountries(): Call<List<Country>>

    @GET("name/{name}?:fields=name,flags")
    fun getCountryByName(@Query("name") name: String): Call<Country>
}