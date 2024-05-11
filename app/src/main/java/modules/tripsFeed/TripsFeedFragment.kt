package modules.tripsFeed

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.example.ilinktrip.LandingPageFragment
import com.example.ilinktrip.R
import com.example.ilinktrip.placeholder.PlaceholderContent
import interfaces.TripFeedItemClickListener
import models.Model
import models.Trip
import modules.tripsFeed.adapter.TripRecyclerViewAdapter

class TripsFeedFragment : Fragment() {
    var tripsRecyclerView: RecyclerView? = null
    var trips: MutableList<Trip>? = null
    var listener: TripFeedItemClickListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_trips_feed_list, container, false)

        trips = Model.instance().getAllTrips()
        tripsRecyclerView = view.findViewById(R.id.trips_feed_recycler_view)
        tripsRecyclerView?.setHasFixedSize(true)
        tripsRecyclerView?.layoutManager = LinearLayoutManager(context)

        val adapter = TripRecyclerViewAdapter(trips, listener)
        tripsRecyclerView?.adapter = adapter

        adapter.listener = object : TripFeedItemClickListener {
            override fun onTripClick(position: Int) {
                val trip = trips?.get(position)

                if (trip != null) {
                    Navigation.findNavController(view)
                        .navigate(
                            TripsFeedFragmentDirections.actionTripsFeedFragmentToTripDetailsFragment(
                                trip.userName,
                                trip.country,
                                trip.place,
                                trip.startsAt.toString(),
                                trip.durationInWeeks
                            )
                        )
                }
            }

        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TripsFeedFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    fun setOnTripClickListener(listener: TripFeedItemClickListener) {
        this.listener = listener
    }
}