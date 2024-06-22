package com.example.ilinktrip

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import com.example.ilinktrip.modules.profile.ProfileFragmentDirections
import com.ilinktrip.R

class LandingPageFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_landing_page, container, false)
        val getStartedBtn = view.findViewById<Button>(R.id.get_started_btn)
        val signInBtn = view.findViewById<Button>(R.id.sign_in_btn)

        getStartedBtn.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(
                    LandingPageFragmentDirections.actionLandingPageFragmentToRegisterFragment()
                )
//            listener?.onGetStartedClick()
        }

        signInBtn.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(
                    LandingPageFragmentDirections.actionLandingPageFragmentToLoginFragment()
                )
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LandingPageFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}