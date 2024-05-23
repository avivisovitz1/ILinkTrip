package modules.favoriteTravelersList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ilinktrip.R
import interfaces.RemoveFavoriteTravelerClickListener
import models.Model
import models.User

class TravelerRecyclerViewAdapter(
    private var travelers: MutableList<User>?,
) :
    RecyclerView.Adapter<TravelerViewHolder>() {
    private var listener: RemoveFavoriteTravelerClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelerViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_traveler_row, parent, false)

        listener = object : RemoveFavoriteTravelerClickListener {
            override fun onRemoveFavoriteClick(user: User) {
                Model.instance().removeTravelerFromFavoriteList(user.id)
                travelers?.remove(user)
                notifyDataSetChanged()
            }
        }
        return TravelerViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int = travelers?.size ?: 0

    override fun onBindViewHolder(holder: TravelerViewHolder, position: Int) {
        val traveler = travelers?.get(position)
        holder.bind(traveler)
    }
}

