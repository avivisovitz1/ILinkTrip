package com.example.ilinktrip.modules.tripsFeed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.ilinktrip.models.Model
import com.ilinktrip.R


class TripDetailsFragment : Fragment() {
    private val args by navArgs<TripDetailsFragmentArgs>()
    private val userDetails by lazy {
        args.userDetails
    }
    val country by lazy {
        args.country
    }
    val place by lazy {
        args.place
    }
    private val startsAt by lazy {
        args.startsAt
    }
    private val duration by lazy {
        args.duration
    }
    private val isFavorite by lazy {
        args.isFavorite
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_trip_details, container, false)
        val userNameTv = view.findViewById<TextView>(R.id.trip_user_name_tv)
        val countryPlaceTv = view.findViewById<TextView>(R.id.trip_place_tv)
//        val userProfileImg = view.findViewById<ImageView>(R.id.trip_user_profile_iv)
        val phoneNumberTv = view.findViewById<TextView>(R.id.trip_phone_number_tv)
        val startsAtTv = view.findViewById<TextView>(R.id.trip_starts_at_tv)
        val durationTv = view.findViewById<TextView>(R.id.trip_duration_tv)
        val linkWithTravelerBtn = view.findViewWithTag<Button>(R.id.trip_link_with_traveler_btn)
        val markFavoriteTraveler = view.findViewById<ImageButton>(R.id.trip_mark_favorite_traveler)

        userNameTv.text = userDetails.firstName + " " + userDetails.lastName
        countryPlaceTv.text = country + ", " + place
        startsAtTv.text = startsAt
        durationTv.text = duration.toString() + " Weeks"
        phoneNumberTv.text = userDetails.phoneNumber

        markFavoriteTraveler.setImageResource(if (isFavorite) R.drawable.star else R.drawable.check_star)
        markFavoriteTraveler.setOnClickListener {
            if (!isFavorite) {
                Model.instance().getCurrentUser { currentUser ->
                    if (currentUser != null) {
                        Model.instance().addFavoriteTraveler(currentUser.id, userDetails.id) {
                            markFavoriteTraveler.setImageResource(R.drawable.star)
                        }
                    } else {
                        Toast.makeText(
                            this.context,
                            "error adding to favorites",
                            Toast.LENGTH_SHORT
                        )
                    }
                }
            } else {
                Model.instance().getCurrentUser { currentUser ->
                    if (currentUser != null) {
                        Model.instance().deleteFavoriteTraveler(currentUser.id, userDetails.id) {
                            markFavoriteTraveler.setImageResource(R.drawable.check_star)
                        }
                    } else {
                        Toast.makeText(
                            this.context,
                            "error deleting from favorites",
                            Toast.LENGTH_SHORT
                        )
                    }
                }
            }
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TripDetailsFragment()
    }
}