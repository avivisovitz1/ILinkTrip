package modules.tripsFeed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ilinktrip.R
import interfaces.TripFeedItemClickListener
import models.Trip

class TripRecyclerViewAdapter(
    var trips: MutableList<Trip>?,
    var listener: TripFeedItemClickListener?
) :
    RecyclerView.Adapter<TripViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_trip_row, parent, false)
        return TripViewHolder(itemView, listener, trips)
    }

    override fun getItemCount(): Int = trips?.size ?: 0

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trip = trips?.get(position)

        holder.bind(trip)
    }

    fun setOnItemClickListener(listener: TripFeedItemClickListener) {
        this.listener = listener
    }
}

