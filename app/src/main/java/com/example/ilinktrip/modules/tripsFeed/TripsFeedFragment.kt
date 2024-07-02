package com.example.ilinktrip.modules.tripsFeed

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.ilinktrip.interfaces.TripFeedItemClickListener
import com.example.ilinktrip.models.TripModel
import com.example.ilinktrip.modules.tripsFeed.adapter.TripRecyclerViewAdapter
import com.example.ilinktrip.viewModels.UserViewModel
import com.ilinktrip.R
import com.ilinktrip.databinding.FragmentTripsFeedListBinding


class TripsFeedFragment : Fragment() {
    private var tripsRecyclerView: RecyclerView? = null
    private var listener: TripFeedItemClickListener? = null
    private var adapter: TripRecyclerViewAdapter? = null
    private var binding: FragmentTripsFeedListBinding? = null
    var viewModel: TripsFeedFragmentViewModel? = null
    var userViewModel: UserViewModel? = null
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

        val view = binding!!.root;
        binding!!.tripsFeedRecyclerView.setHasFixedSize(true)
        binding!!.tripsFeedRecyclerView.layoutManager = LinearLayoutManager(context)
        tripsRecyclerView = view.findViewById(R.id.trips_feed_recycler_view)

        adapter = TripRecyclerViewAdapter(
            showOnlyUserTrips,
            viewModel?.getData()?.value,
            viewModel?.getCountriesFlags()?.value,
            listener,
        )
        tripsRecyclerView?.adapter = adapter

        adapter?.listener = object : TripFeedItemClickListener {
            override fun onTripClick(position: Int) {
                val tripWithUser = viewModel?.getData()?.value?.get(position)

                if (tripWithUser != null) {
                    val favoriteIds = userViewModel?.getUserFavoriteUsersIds()?.value
                    Navigation.findNavController(view).navigate(
                        TripsFeedFragmentDirections.actionTripsFeedFragmentToTripDetailsFragment(
                            tripWithUser.userDetails,
                            tripWithUser.trip,
                            favoriteIds?.contains(tripWithUser.userDetails.id) ?: false
                        )
                    )
                } else {
                    Toast.makeText(
                        context,
                        "error getting trip or user details",
                        Toast.LENGTH_SHORT
                    )
                }
            }

            override fun onTripEditClick(position: Int) {
                val tripWithUser = viewModel?.getData()?.value?.get(position)

                if (tripWithUser != null) {
                    Navigation.findNavController(view).navigate(
                        TripsFeedFragmentDirections.actionTripsFeedFragmentToAddTripFragment(
                            tripWithUser.trip
                        )
                    )
                }
            }

            override fun onTripDeleteClick(position: Int) {
                val tripWithUser = viewModel?.getData()?.value?.get(position)

                if (tripWithUser != null) {
                    viewModel?.deleteTrip(tripWithUser.trip)
                }
            }
        }


        viewModel?.getData()?.observe(viewLifecycleOwner) { trips ->
            adapter?.setData(trips, showOnlyUserTrips)
            viewModel?.fetchTripsCountriesFlags(trips)
        }

        viewModel?.getCountriesFlags()?.observe(viewLifecycleOwner) { countriesFlags ->
            adapter?.setCountriesFlags(countriesFlags)

        }

        viewModel?.getLoadingState()?.observe(viewLifecycleOwner) { loadingStatus ->
            binding!!.tripsFeedSwipeRefresh.isRefreshing =
                loadingStatus == TripModel.LoadingState.LOADING
        }

        viewModel?.getToastMessage()?.observe(viewLifecycleOwner) { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        binding!!.tripsFeedSwipeRefresh.setOnRefreshListener {
            reloadTrips()
        }

        return view
    }

    private fun reloadTrips() {
        viewModel?.refreshData()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this)[TripsFeedFragmentViewModel::class.java]
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
    }

    companion object {
        @JvmStatic
        fun newInstance(showOnlyUserTrips: Boolean) = TripsFeedFragment().apply {
            arguments = Bundle().apply {}
        }
    }
}