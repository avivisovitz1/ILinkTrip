package com.example.ilinktrip.modules.tripsFeed.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ilinktrip.interfaces.TripFeedItemClickListener
import com.example.ilinktrip.models.Trip
import com.example.ilinktrip.models.TripWithUserDetails
import com.ilinktrip.R

class TripViewHolder(
    itemView: View,
    listener: TripFeedItemClickListener?,
) :
    RecyclerView.ViewHolder(itemView) {
    private var tripWithUser: TripWithUserDetails? = null
    private var userNameTv: TextView? = null
    private var countryPlaceTv: TextView? = null
    private var userProfileIv: ImageView? = null

    init {
        userNameTv = itemView.findViewById(R.id.trip_user_name_tv)
        countryPlaceTv = itemView.findViewById(R.id.trip_country_place_tv)
        userProfileIv = itemView.findViewById(R.id.trip_user_profile_iv)

        itemView.setOnClickListener {
            listener?.onTripClick(adapterPosition)
        }
    }

    fun bind(tripWithUser: TripWithUserDetails?) {
        this.tripWithUser = tripWithUser
        this.userNameTv?.text =
            tripWithUser?.userDetails?.firstName + " " + tripWithUser?.userDetails?.lastName

        val avatar =
            if (tripWithUser?.userDetails?.gender === "male") R.drawable.guy_avatar else R.drawable.girl_avatar
        this.userProfileIv?.setImageResource(avatar)
        this.countryPlaceTv?.text = tripWithUser?.trip?.country + ", " + tripWithUser?.trip?.place
    }
}