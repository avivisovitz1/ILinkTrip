package com.example.ilinktrip.modules.profile

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.ilinktrip.viewModels.UserViewModel
import com.ilinktrip.R
import com.ilinktrip.databinding.FragmentProfileBinding
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {
    private var binding: FragmentProfileBinding? = null
    private var viewModel: UserViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(
            inflater,
            container,
            false
        )
        val view = binding!!.root

        val user = viewModel?.getCurrentUser()?.value
        if (user != null) {
            val profileIv = binding!!.profileIv
            binding!!.profileUsernameTv.text = user!!.firstName + " " + user!!.lastName
            binding!!.profileEmailTv.text = user!!.email

            val avatar =
                if (user!!.gender == "male") R.drawable.guy_avatar else R.drawable.girl_avatar

            if (user!!.avatarUrl != "") {
                Picasso.get().load(user!!.avatarUrl).resize(160, 160).placeholder(avatar)
                    .into(profileIv)
            } else {
                profileIv.setImageResource(avatar)
            }
        } else {
//                raise error
        }

        val myTripsBtn = binding!!.profileMyTripsBtn
        val myFavoritesBtn = binding!!.profileMyFavoritesBtn
        val editProfileBtn = binding!!.profileEditBtn

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

        viewModel?.getToastMessage()?.observe(viewLifecycleOwner) { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT)

        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
    }
}