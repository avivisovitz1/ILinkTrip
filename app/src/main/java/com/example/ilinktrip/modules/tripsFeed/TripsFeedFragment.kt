package com.example.ilinktrip.modules.tripsFeed

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.ilinktrip.interfaces.TripFeedItemClickListener
import com.example.ilinktrip.models.Model
import com.example.ilinktrip.models.TripWithUserDetails
import com.example.ilinktrip.models.User
import com.example.ilinktrip.modules.tripsFeed.adapter.TripRecyclerViewAdapter
import com.ilinktrip.R
import com.ilinktrip.databinding.FragmentTripsFeedListBinding


class TripsFeedFragment : Fragment() {
    var tripsRecyclerView: RecyclerView? = null
    var tripsWithUsers: MutableList<TripWithUserDetails>? = null
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

        reloadTrips()

        val view = binding!!.root;
        binding!!.tripsFeedRecyclerView.setHasFixedSize(true)
        binding!!.tripsFeedRecyclerView.layoutManager = LinearLayoutManager(context)
        tripsRecyclerView = view.findViewById(R.id.trips_feed_recycler_view)

        adapter = TripRecyclerViewAdapter(showOnlyUserTrips, tripsWithUsers, listener)
        tripsRecyclerView?.adapter = adapter

        adapter?.listener = object : TripFeedItemClickListener {
            override fun onTripClick(position: Int) {
                val tripWithUser = tripsWithUsers?.get(position)

                if (tripWithUser != null) {
                    Model.instance()
                        .getCurrentUser { user ->
                            if (user != null) {
                                Model.instance().getUserFavoriteTravelers(user.id) { favoritesIds ->
                                    Navigation.findNavController(view).navigate(
                                        TripsFeedFragmentDirections.actionTripsFeedFragmentToTripDetailsFragment(
                                            tripWithUser.userDetails,
                                            tripWithUser.trip,
                                            favoritesIds.contains(tripWithUser.userDetails.id)
                                        )
                                    )
                                }
                            } else {
//                                todo: log
                            }
                        }
                }
            }

            override fun onTripEditClick(position: Int) {
                val tripWithUser = tripsWithUsers?.get(position)

                if (tripWithUser != null) {
                    Navigation.findNavController(view).navigate(
                        TripsFeedFragmentDirections.actionTripsFeedFragmentToAddTripFragment(
                            tripWithUser.trip
                        )
                    )
                }
            }

            override fun onTripDeleteClick(position: Int) {
                val tripWithUser = tripsWithUsers?.get(position)

                if (tripWithUser != null) {
                    Model.instance().deleteTrip(tripWithUser.trip) { isSuccessful ->
                        if (isSuccessful) {
                            reloadTrips()
                        } else {
                            Toast.makeText(
                                context,
                                "error occurred trying deleting the trip",
                                Toast.LENGTH_SHORT
                            )
                        }
                    }
                }
            }
        }

        return view
    }

    private fun reloadTrips() {
        binding!!.tripsFeedProgressBar.visibility = View.VISIBLE

        Model.instance().getCurrentUser { user ->
            if (user != null) {
                Model.instance().getAllTrips { trips ->
                    val tripsList = trips.toMutableList()
                    this.tripsWithUsers = tripsList
                    adapter?.setData(tripsList, showOnlyUserTrips)
                    binding!!.tripsFeedProgressBar.visibility = View.GONE
                }
            } else {
                Toast.makeText(this.context, "error getting user data", Toast.LENGTH_SHORT)
            }
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