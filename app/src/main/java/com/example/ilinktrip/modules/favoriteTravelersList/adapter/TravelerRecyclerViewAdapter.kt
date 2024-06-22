package com.example.ilinktrip.modules.favoriteTravelersList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ilinktrip.interfaces.RemoveFavoriteTravelerClickListener
import com.example.ilinktrip.models.FavoriteTraveler
import com.example.ilinktrip.models.Model
import com.example.ilinktrip.models.User
import com.ilinktrip.R

class TravelerRecyclerViewAdapter(
    private var travelersIds: MutableList<String>?,
) :
    RecyclerView.Adapter<TravelerViewHolder>() {
    private var listener: RemoveFavoriteTravelerClickListener? = null

    fun setData(data: MutableList<String>) {
        this.travelersIds = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelerViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_traveler_row, parent, false)

        listener = object : RemoveFavoriteTravelerClickListener {
            override fun onRemoveFavoriteClick(user: User, position: Int) {
                Model.instance().getCurrentUser { currentUser ->
                    if (currentUser != null) {
                        Model.instance().deleteFavoriteTraveler(currentUser.id, user.id) {
                            setData(travelersIds!!.filter { id -> id != user.id }.toMutableList())
                        }
                    } else {
                        Toast.makeText(
                            itemView.context,
                            "error removing from favorites",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }
        return TravelerViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int = travelersIds?.size ?: 0

    override fun onBindViewHolder(holder: TravelerViewHolder, position: Int) {
        val travelerId = travelersIds?.get(position)

        if (travelerId != "" && travelerId != null) {
            Model.instance().getAllUsers { users ->
                val traveler = users[travelerId]
                holder.bind(traveler)
            }
        }
    }
}

