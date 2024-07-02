package com.example.ilinktrip.modules.mainNavigator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.ilinktrip.R

class MainNavigatorFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val action =
            MainNavigatorFragmentDirections.actionMainNavigatorFragmentToTripsFeedFragment(false)
        Navigation.findNavController(view).navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_main_navigator, container, false)

        return view
    }
}