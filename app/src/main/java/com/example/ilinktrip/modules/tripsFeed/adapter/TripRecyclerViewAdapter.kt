package com.example.ilinktrip.modules.tripsFeed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ilinktrip.interfaces.TripFeedItemClickListener
import com.example.ilinktrip.entities.TripWithUser
import com.ilinktrip.R

class TripRecyclerViewAdapter(
    private var showOnlyUserTrips: Boolean,
    private var tripsWithUsers: List<TripWithUser>?,
    private var countriesFlags: Map<String, String>?,
    var listener: TripFeedItemClickListener?
) :
    RecyclerView.Adapter<TripViewHolder>() {

    fun setData(data: List<TripWithUser>, showOnlyUserTrips: Boolean) {
        this.tripsWithUsers = data
        this.showOnlyUserTrips = showOnlyUserTrips
        notifyDataSetChanged()
    }

    fun setCountriesFlags(data: Map<String, String>) {
        this.countriesFlags = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_trip_row, parent, false)
        return TripViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int = tripsWithUsers?.size ?: 0

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val tripWithUser = tripsWithUsers?.get(position)
        var countryPhotoUrl = ""

        if (tripWithUser != null) {
            if (tripWithUser.trip.country != "") {
                countryPhotoUrl = countriesFlags?.get(tripWithUser.trip.country) ?: ""
            }
        }

        holder.bind(tripWithUser, showOnlyUserTrips, countryPhotoUrl)
    }
}

