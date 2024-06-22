package com.example.ilinktrip.modules.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import com.example.ilinktrip.models.Model
import com.example.ilinktrip.models.User
import com.ilinktrip.R

class ProfileFragment : Fragment() {
    private var user: User? = null

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

        Model.instance().getCurrentUser {
            if (it != null) {
                user = it
                val usernameTv = view.findViewById<TextView>(R.id.profile_username_tv)
                val emailTv = view.findViewById<TextView>(R.id.profile_email_tv)
                val profileIv = view.findViewById<ImageView>(R.id.profile_iv)
                usernameTv.text = it.firstName + " " + it.lastName
                emailTv.text = it.email
                val avatar =
                    if (user!!.gender == "male") R.drawable.guy_avatar else R.drawable.girl_avatar
                profileIv.setImageResource(avatar)
            } else {
//                raise error
            }
        }

        val myTripsBtn = view.findViewById<Button>(R.id.profile_my_trips_btn)
        val myFavoritesBtn = view.findViewById<Button>(R.id.profile_my_favorites_btn)
        val editProfileBtn = view.findViewById<Button>(R.id.profile_edit_btn)

        myTripsBtn.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(
                    ProfileFragmentDirections.actionProfileFragmentToTripsFeedFragment(
                        true
                    )
                )
        }

        myFavoritesBtn.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(ProfileFragmentDirections.actionProfileFragmentToFavoriteTravelersListFragment())
        }

        editProfileBtn.setOnClickListener {
            Navigation.findNavController(view).navigate(
                ProfileFragmentDirections.actionProfileFragmentToRegisterFragment(
                    user
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