package com.example.ilinktrip.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ilinktrip.entities.Country
import com.example.ilinktrip.interfaces.CountryApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CountryModel private constructor() {
    companion object {
        private var _instance: CountryModel = CountryModel()
        const val COUNTRIES_API_BASE_URL = "https://restcountries.com/v3.1/"
        val gson: Gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl(COUNTRIES_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
        val countryApi = retrofit.create(CountryApi::class.java)

        fun instance(): CountryModel {
            return _instance
        }
    }

    fun getAllCountries(callback: (List<Country>?) -> Unit) {
        val call: Call<List<Country>> = countryApi.getAllCountries()
        call.enqueue(object : Callback<List<Country>> {
            override fun onResponse(
                call: Call<List<Country>>?,
                response: Response<List<Country>>?
            ) {
                if (response != null && response.isSuccessful) {
                    val result = response.body()
                    callback(result)
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<Country>>?, t: Throwable?) {
                callback(null)
            }
        })
    }

    fun fetchCountryFlags(countries: List<String>): LiveData<Map<String, String>> {
        val resultLiveData = MutableLiveData<Map<String, String>>()
        val resultMap = mutableMapOf<String, String>()
        val iterator = countries.iterator()

        getCountryByName(iterator, resultMap, resultLiveData)

        return resultLiveData
    }

    fun getCountryByName(
        iterator: Iterator<String>, results: MutableMap<String, String>,
        liveData: MutableLiveData<Map<String, String>>
    ) {
        if (!iterator.hasNext()) {
            liveData.postValue(results)
            return
        }

        val countryName = iterator.next()
        val call: Call<List<Country>> = countryApi.getCountryByName(countryName)
        call.enqueue(object : Callback<List<Country>> {
            override fun onResponse(
                call: Call<List<Country>>?,
                response: Response<List<Country>>?
            ) {
                if (response != null && response.isSuccessful) {
                    val result = response.body()[0]
                    results[result.name.common] = result.flags.png
                }

                getCountryByName(iterator, results, liveData)
            }

            override fun onFailure(call: Call<List<Country>>?, t: Throwable?) {
                Log.e("error country", t?.message ?: "")
                getCountryByName(iterator, results, liveData)
            }
        })
    }
}