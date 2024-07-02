package com.example.ilinktrip.modules.favoriteTravelersList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ilinktrip.interfaces.RemoveFavoriteTravelerClickListener
import com.example.ilinktrip.models.UserModel
import com.ilinktrip.R

class TravelerRecyclerViewAdapter(
    private var travelersIds: MutableList<String>?,
    var listener: RemoveFavoriteTravelerClickListener?,
) :
    RecyclerView.Adapter<TravelerViewHolder>() {
//    private var listener: RemoveFavoriteTravelerClickListener? = null

    fun setData(data: MutableList<String>) {
        this.travelersIds = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelerViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_traveler_row, parent, false)

        return TravelerViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int = travelersIds?.size ?: 0

    override fun onBindViewHolder(holder: TravelerViewHolder, position: Int) {
        val travelerId = travelersIds?.get(position)

        if (travelerId != "" && travelerId != null) {
            UserModel.instance().getAllUsers { users ->
                holder.bind(users.find { u -> u.id == travelerId })
            }
        }
    }
}

