package com.example.ilinktrip.modules.landing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import com.ilinktrip.R
import com.ilinktrip.databinding.FragmentAddTripBinding
import com.ilinktrip.databinding.FragmentLandingPageBinding

class LandingPageFragment : Fragment() {
    private var binding: FragmentLandingPageBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLandingPageBinding.inflate(
            inflater,
            container,
            false
        )

        val view = binding!!.root
        val getStartedBtn = binding!!.getStartedBtn
        val signInBtn = binding!!.signInBtn

        getStartedBtn.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(
                    LandingPageFragmentDirections.actionLandingPageFragmentToRegisterFragment()
                )
        }

        signInBtn.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(
                    LandingPageFragmentDirections.actionLandingPageFragmentToLoginFragment()
                )
        }

        return view
    }
}