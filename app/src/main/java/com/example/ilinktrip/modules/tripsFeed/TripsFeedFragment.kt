package com.example.ilinktrip.modules.tripsFeed

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.ilinktrip.interfaces.TripFeedItemClickListener
import com.example.ilinktrip.models.Model
import com.example.ilinktrip.models.Trip
import com.example.ilinktrip.modules.tripsFeed.adapter.TripRecyclerViewAdapter
import com.ilinktrip.R
import com.ilinktrip.databinding.FragmentTripsFeedListBinding


class TripsFeedFragment : Fragment() {
    var tripsRecyclerView: RecyclerView? = null
    var trips: MutableList<Trip>? = null
    var listener: TripFeedItemClickListener? = null
    var adapter: TripRecyclerViewAdapter? = null
    var binding: FragmentTripsFeedListBinding? = null
    private val args by navArgs<TripsFeedFragmentArgs>()

    val showOnlyUserTrips by lazy { args.showOnlyUserTrips }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTripsFeedListBinding.inflate(inflater, container, false)
//        val view = inflater.inflate(R.layout.fragment_trips_feed_list, container, false)

        reloadTrips()

        val view = binding!!.root;
        binding!!.tripsFeedRecyclerView.setHasFixedSize(true)
        binding!!.tripsFeedRecyclerView.layoutManager = LinearLayoutManager(context)
        tripsRecyclerView = view.findViewById(R.id.trips_feed_recycler_view)
//        tripsRecyclerView?.setHasFixedSize(true)
//        tripsRecyclerView?.layoutManager =

        adapter = TripRecyclerViewAdapter(trips, listener)
        tripsRecyclerView?.adapter = adapter

        adapter?.listener = object : TripFeedItemClickListener {
            override fun onTripClick(position: Int) {
                val trip = trips?.get(position)

                if (trip != null) {
                    Navigation.findNavController(view).navigate(
                        TripsFeedFragmentDirections.actionTripsFeedFragmentToTripDetailsFragment(
//                                TODO: replace with userName
                            "shilshul",
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

    private fun reloadTrips() {
        binding!!.tripsFeedProgressBar.visibility = View.VISIBLE

        Model.instance().getAllTrips { trips ->
            val tripsList = trips.toMutableList()
            this.trips = tripsList
            adapter?.setData(tripsList)
            binding!!.tripsFeedProgressBar.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        reloadTrips()
    }

    companion object {
        @JvmStatic
        fun newInstance(showOnlyUserTrips: Boolean) = TripsFeedFragment().apply {
            arguments = Bundle().apply {}
        }
    }
}