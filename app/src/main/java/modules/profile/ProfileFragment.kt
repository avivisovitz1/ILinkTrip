package modules.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation
import com.example.ilinktrip.R
import models.Model
import models.User

class ProfileFragment : Fragment() {
    val user: User = Model.instance().getUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val usernameTv = view.findViewById<TextView>(R.id.profile_username_tv)
        val emailTv = view.findViewById<TextView>(R.id.profile_email_tv)
        val myTripsBtn = view.findViewById<Button>(R.id.profile_my_trips_btn)
        val myFavoritesBtn = view.findViewById<Button>(R.id.profile_my_favorites_btn)
        val editProfileBtn = view.findViewById<Button>(R.id.profile_edit_btn)

        usernameTv.text = user.firstName + " " + user.lastName
        emailTv.text = user.email

        myTripsBtn.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(ProfileFragmentDirections.actionProfileFragmentToTripsFeedFragment(true))
        }

        myFavoritesBtn.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(ProfileFragmentDirections.actionProfileFragmentToFavoriteTravelersListFragment())
        }

        editProfileBtn.setOnClickListener {
            Navigation.findNavController(view).navigate(
                ProfileFragmentDirections.actionProfileFragmentToRegisterFragment(
                    Model.instance().getUser()
                )
            )
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}