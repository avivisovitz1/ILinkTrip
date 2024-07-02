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
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.ilinktrip.interfaces.TripFeedItemClickListener
import com.example.ilinktrip.models.TripModel
import com.example.ilinktrip.modules.tripsFeed.adapter.TripRecyclerViewAdapter
import com.example.ilinktrip.viewModels.UserViewModel
import com.ilinktrip.databinding.FragmentTripsFeedListBinding


class TripsFeedFragment : Fragment() {
    private var tripsRecyclerView: RecyclerView? = null
    private var listener: TripFeedItemClickListener? = null
    private var adapter: TripRecyclerViewAdapter? = null
    private var binding: FragmentTripsFeedListBinding? = null
    private var isUserTrips: Boolean = false
    private var viewModel: TripsFeedFragmentViewModel? = null
    private var userViewModel: UserViewModel? = null
    private val args by navArgs<TripsFeedFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isUserTrips = args.showOnlyUserTrips
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTripsFeedListBinding.inflate(inflater, container, false)

        if (isUserTrips) {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = "My Trips"
        } else {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = "Trips Nearby"
            (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(
                false
            )
        }

        val view = binding!!.root;
        binding!!.tripsFeedRecyclerView.setHasFixedSize(true)
        binding!!.tripsFeedRecyclerView.layoutManager = LinearLayoutManager(context)
        tripsRecyclerView = binding!!.tripsFeedRecyclerView

        val data =
            if (isUserTrips) viewModel?.getData(isUserTrips)?.value else
                viewModel?.getData(isUserTrips)?.value

        adapter = TripRecyclerViewAdapter(
            isUserTrips,
            data,
            viewModel?.getCountriesFlags()?.value,
            listener,
        )
        tripsRecyclerView?.adapter = adapter

        adapter?.listener = object : TripFeedItemClickListener {
            override fun onTripClick(position: Int) {
                onTripClick(view, position)
            }

            override fun onTripEditClick(position: Int) {
                editTrip(view, position)
            }

            override fun onTripDeleteClick(position: Int) {
                deleteTrip(position)
            }
        }

        viewModel?.getData(isUserTrips)
            ?.observe(viewLifecycleOwner) { trips ->
                adapter?.setData(trips, isUserTrips)
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
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        viewModel = ViewModelProvider(this, TripsFeedFragmentViewModelFactory(userViewModel!!)).get(
            TripsFeedFragmentViewModel::class.java
        )
    }

    fun onTripClick(view: View, position: Int) {
        val tripWithUser = viewModel?.getData(isUserTrips)?.value?.get(position)

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

    private fun editTrip(view: View, position: Int) {
        val tripWithUser = viewModel?.getData(isUserTrips)?.value?.get(position)

        if (tripWithUser != null) {
            Navigation.findNavController(view).navigate(
                TripsFeedFragmentDirections.actionTripsFeedFragmentToAddTripFragment(
                    tripWithUser.trip
                )
            )
        }
    }

    private fun deleteTrip(position: Int) {
        val tripWithUser = viewModel?.getData(isUserTrips)?.value?.get(position)

        if (tripWithUser != null) {
            viewModel?.deleteTrip(tripWithUser.trip)
        }
    }
}