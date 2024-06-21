package com.example.ilinktrip.modules.tripsFeed.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ilinktrip.interfaces.TripFeedItemClickListener
import com.example.ilinktrip.models.Trip
import com.ilinktrip.R

class TripViewHolder(
    itemView: View,
    listener: TripFeedItemClickListener?,
) :
    RecyclerView.ViewHolder(itemView) {
    var trip: Trip? = null
    var userNameTv: TextView? = null
    var countryPlaceTv: TextView? = null
    var userProfileIb: ImageView? = null

    init {
        userNameTv = itemView.findViewById(R.id.trip_user_name_tv)
        countryPlaceTv = itemView.findViewById(R.id.trip_country_place_tv)
        userProfileIb = itemView.findViewById(R.id.trip_user_profile_iv)

        itemView.setOnClickListener {
            listener?.onTripClick(adapterPosition)
        }
    }

    fun bind(trip: Trip?) {
        this.trip = trip
        this.userNameTv?.text = "Shilshul"
        //TODO: this.userNameTv?.text = trip?.userName
        this.userProfileIb?.setImageResource(R.drawable.girl_avatar)
        this.countryPlaceTv?.text = trip?.country + ", " + trip?.place
    }
}