package modules.tripsFeed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.example.ilinktrip.R


class TripDetailsFragment : Fragment() {
    private val args by navArgs<TripDetailsFragmentArgs>()
    val userName by lazy {
        args.userName
    }
    val country by lazy {
        args.country
    }
    val place by lazy {
        args.place
    }
    val startsAt by lazy {
        args.startsAt
    }
    val duration by lazy {
        args.duration
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
//        val phoneNumberTv = view.findViewById<TextView>(R.id.trip_phone_number_tv)
        val startsAtTv = view.findViewById<TextView>(R.id.trip_starts_at_tv)
        val durationTv = view.findViewById<TextView>(R.id.trip_duration_tv)
        val linkWithTravelerBtn = view.findViewWithTag<Button>(R.id.trip_link_with_traveler_btn)

        userNameTv.text = userName
        countryPlaceTv.text = country + place
        startsAtTv.text = startsAt
        durationTv.text = duration.toString() + " Weeks"

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TripDetailsFragment()
    }
}