package com.example.ilinktrip.modules.tripsFeed.adapter

import android.content.DialogInterface
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.ilinktrip.application.GlobalConst
import com.example.ilinktrip.interfaces.TripFeedItemClickListener
import com.example.ilinktrip.entities.TripWithUser
import com.ilinktrip.R
import com.squareup.picasso.Picasso

class TripViewHolder(
    itemView: View,
    listener: TripFeedItemClickListener?,
) :
    RecyclerView.ViewHolder(itemView) {
    private var tripWithUser: TripWithUser? = null
    private var userNameTv: TextView? = null
    private var countryPlaceTv: TextView? = null
    private var userProfileIv: ImageView? = null
    private var countryFlagIv: ImageView? = null
    private var editIb: ImageButton? = null
    private var deleteIb: ImageButton? = null
    private var isOnTripIv: ImageView? = null

    init {
        userNameTv = itemView.findViewById(R.id.trip_user_name_tv)
        countryPlaceTv = itemView.findViewById(R.id.trip_country_place_tv)
        userProfileIv = itemView.findViewById(R.id.trip_user_profile_iv)
        countryFlagIv = itemView.findViewById(R.id.trip_country_flag_iv)
        isOnTripIv = itemView.findViewById(R.id.is_on_trip_Iv)

        editIb = itemView.findViewById(R.id.edit_trip_ib)
        deleteIb = itemView.findViewById(R.id.delete_trip_ib)

        itemView.setOnClickListener {
            listener?.onTripClick(adapterPosition)
        }

        editIb?.setOnClickListener {
            listener?.onTripEditClick(adapterPosition)
        }

        deleteIb?.setOnClickListener {
            val builder = AlertDialog.Builder(itemView.context)
            builder.setTitle("Are you sure you want to delete the trip?")
            builder.setMessage("this action can't be undone")
            builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                listener?.onTripDeleteClick(adapterPosition)
                dialog.cancel()
            })
            builder.setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })

            val alertDialog = builder.create()
            alertDialog.show()
        }
    }

    fun bind(tripWithUser: TripWithUser?, hasActions: Boolean, countryPhotoUrl: String) {
        this.tripWithUser = tripWithUser
        this.userNameTv?.text =
            tripWithUser?.userDetails?.firstName + " " + tripWithUser?.userDetails?.lastName
        val avatar =
            if (tripWithUser?.userDetails?.gender == GlobalConst.GENDER_MALE) R.drawable.guy_avatar else R.drawable.girl_avatar

        if (tripWithUser?.userDetails?.avatarUrl != "") {
            Picasso.get().load(tripWithUser?.userDetails?.avatarUrl).resize(80, 80)
                .placeholder(avatar)
                .into(userProfileIv)
        } else {
            this.userProfileIv?.setImageResource(avatar)
        }
        this.countryPlaceTv?.text = tripWithUser?.trip?.country + ", " + tripWithUser?.trip?.place

        if (countryPhotoUrl != "") {
            Picasso.get().load(countryPhotoUrl).resize(42, 26)
                .placeholder(R.drawable.no_trip)
                .into(countryFlagIv)
        }

        if (hasActions) {
            this.isOnTripIv?.visibility = View.GONE
            this.editIb?.visibility = View.VISIBLE
            this.deleteIb?.visibility = View.VISIBLE
        } else {
            val tripStatusAvatar =
                if (tripWithUser?.trip?.isDone == true) R.drawable.done_stamp else R.drawable.currenty_arrow
            this.isOnTripIv?.visibility = View.VISIBLE
            this.isOnTripIv?.setImageResource(tripStatusAvatar)
        }
    }
}