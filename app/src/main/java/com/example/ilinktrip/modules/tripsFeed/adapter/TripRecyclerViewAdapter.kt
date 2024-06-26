package com.example.ilinktrip.modules.tripsFeed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ilinktrip.interfaces.TripFeedItemClickListener
import com.example.ilinktrip.models.CountryModel
import com.example.ilinktrip.models.Model
import com.example.ilinktrip.models.Trip
import com.example.ilinktrip.models.TripWithUserDetails
import com.example.ilinktrip.models.User
import com.ilinktrip.R

class TripRecyclerViewAdapter(
    private var showOnlyUserTrips: Boolean,
    private var tripsWithUsers: MutableList<TripWithUserDetails>?,
    var listener: TripFeedItemClickListener?
) :
    RecyclerView.Adapter<TripViewHolder>() {

    //when data in fragment was updated, we need to update also data in adapter
    fun setData(data: MutableList<TripWithUserDetails>, showOnlyUserTrips: Boolean) {
        this.tripsWithUsers = data
        this.showOnlyUserTrips = showOnlyUserTrips
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
            CountryModel.instance().getCountryByName(tripWithUser.trip.country) { country ->
                if (country != null) {
                    countryPhotoUrl = country.flags.png
                }
            }
        }

        holder.bind(tripWithUser, showOnlyUserTrips, countryPhotoUrl)
    }
}

