package com.example.ilinktrip.modules.tripsFeed.tripDetails

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.ilinktrip.utils.LinkingUtils
import com.example.ilinktrip.viewModels.FavoriteTravelersViewModel
import com.example.ilinktrip.viewModels.UserViewModel
import com.ilinktrip.R
import com.ilinktrip.databinding.FragmentTripDetailsBinding
import com.squareup.picasso.Picasso


class TripDetailsFragment : Fragment() {
    private val args by navArgs<TripDetailsFragmentArgs>()
    private var binding: FragmentTripDetailsBinding? = null
    private var viewModel: FavoriteTravelersViewModel? = null
    private var userViewModel: UserViewModel? = null
    private var isInFavorites: Boolean = false

    private val userDetails by lazy {
        args.userDetails
    }
    private val trip by lazy {
        args.trip
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isInFavorites = args.isFavorite
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTripDetailsBinding.inflate(inflater, container, false)
        val view = binding!!.root;
        val userNameTv = view.findViewById<TextView>(R.id.trip_user_name_tv)
        val countryPlaceTv = view.findViewById<TextView>(R.id.trip_place_tv)
        val phoneNumberTv = view.findViewById<TextView>(R.id.trip_phone_number_tv)
        val startsAtTv = view.findViewById<TextView>(R.id.trip_starts_at_tv)
        val durationTv = view.findViewById<TextView>(R.id.trip_duration_tv)
        val markFavoriteTraveler = view.findViewById<ImageButton>(R.id.trip_mark_favorite_traveler)
        val linkWithTravelerBtn = view.findViewById<Button>(R.id.trip_link_with_traveler_btn)
        val currentUser = userViewModel?.getCurrentUser()?.value

        userNameTv.text = userDetails.firstName + " " + userDetails.lastName
        countryPlaceTv.text = trip.country + ", " + trip.place
        startsAtTv.text = trip.startsAt.toString()
        durationTv.text = trip.durationInWeeks.toString() + " Weeks"
        phoneNumberTv.text = userDetails.phoneNumber

        if (trip.avatarUrl != "") {
            Picasso.get()
                .load(trip.avatarUrl).placeholder(R.drawable.no_trip)
                .resize(260, 120)
                .centerInside()
                .into(binding!!.tripPhotoIv)
        }

        if (currentUser != null) {
            if (currentUser.id == userDetails.id) {
                markFavoriteTraveler.visibility = View.GONE
            }

            Picasso.get()
                .load(currentUser.avatarUrl).placeholder(R.drawable.girl_avatar)
                .resize(59, 61)
                .centerInside()
                .into(binding!!.tripUserProfileIv)
        }

        markFavoriteTraveler.setImageResource(if (isInFavorites) R.drawable.star else R.drawable.check_star)
        markFavoriteTraveler.setOnClickListener {

            if (currentUser != null) {
                if (!isInFavorites) {
                    viewModel?.addToFavorites(currentUser.id, userDetails.id)
//                    viewModel?.addToFavorites(currentUser.id, userDetails.id) {
//                        markFavoriteTraveler.setImageResource(R.drawable.star)
//                        isInFavorites = true
//                        Toast.makeText(context, "added to favorites", Toast.LENGTH_LONG).show()
//                    }
                } else {
                    viewModel?.deleteFromFavorites(currentUser.id, userDetails.id)
//                    viewModel?.deleteFromFavorites(currentUser.id, userDetails.id) {
//                        markFavoriteTraveler.setImageResource(R.drawable.check_star)
//                        isInFavorites = false
//                        Toast.makeText(context, "removed from favorites", Toast.LENGTH_LONG).show()
//                    }
                }
            }
        }

        linkWithTravelerBtn.setOnClickListener {
            context?.let { it1 ->
                LinkingUtils.checkSendSMSPermissions(
                    userDetails.phoneNumber,
                    it1,
                    requireActivity()
                )
            }
        }

        userViewModel?.getUserFavoriteUsersIds()?.observe(viewLifecycleOwner) { favoritesIds ->
            isInFavorites = favoritesIds.contains(userDetails.id)
            val avatar = if (isInFavorites) R.drawable.star else R.drawable.check_star
            markFavoriteTraveler.setImageResource(avatar)
        }

        viewModel?.getToastMessage()?.observe(viewLifecycleOwner) { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this)[FavoriteTravelersViewModel::class.java]
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                context?.let { LinkingUtils.sendSMS(userDetails.phoneNumber, it) }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TripDetailsFragment()
    }
}