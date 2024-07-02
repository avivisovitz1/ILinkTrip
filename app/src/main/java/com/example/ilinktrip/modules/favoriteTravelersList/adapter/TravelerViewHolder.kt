package com.example.ilinktrip.modules.favoriteTravelersList.adapter

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ilinktrip.entities.User
import com.example.ilinktrip.interfaces.FavoriteTravelerListListeners
import com.ilinktrip.R

class TravelerViewHolder(
    itemView: View,
    listener: FavoriteTravelerListListeners?
) :
    RecyclerView.ViewHolder(itemView) {
    var user: User? = null
    private var userNameTv: TextView? = null
    private val userProfileIV: ImageView? = null

    init {
        userNameTv = itemView.findViewById(R.id.traveler_username_tv)
        val linkTravelerBtn = itemView.findViewById<Button>(R.id.link_traveler_btn)
        val markedFavoriteIb = itemView.findViewById<ImageButton>(R.id.mark_favorite_traveler_btn)

        linkTravelerBtn.setOnClickListener {
            user?.let { it1 -> listener?.onLinkWithUserClick(it1, adapterPosition) }
        }

        markedFavoriteIb.setOnClickListener {
            markedFavoriteIb.setImageResource(R.drawable.check_star)
            user?.let { it1 -> listener?.onRemoveFavoriteClick(it1, adapterPosition) }
        }
    }

    fun bind(user: User?) {
        this.user = user
        this.userNameTv?.text = user?.firstName + " " + user?.lastName
        this.userProfileIV?.setImageResource(if (user?.gender == "male") R.drawable.guy_avatar else R.drawable.girl_avatar)
    }
}