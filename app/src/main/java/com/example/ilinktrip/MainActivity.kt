package com.example.ilinktrip

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import interfaces.TripFeedItemClickListener
import modules.tripsFeed.TripsFeedFragment

class MainActivity : AppCompatActivity(), TripFeedItemClickListener {
    private var displayedFragment: Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeBtn = findViewById<ImageButton>(R.id.home_ib)
        val addTripBtn = findViewById<ImageButton>(R.id.add_trip_ib)
        val profileBtn = findViewById<ImageButton>(R.id.profile_ib)

        val homeFragment = TripsFeedFragment.newInstance()
        homeFragment.setOnTripClickListener(this)

        val addTripFragment = AddTripFragment.newInstance()
//        val profileFragment =

        displayFragment(homeFragment)

        homeBtn.setOnClickListener {
            displayFragment(homeFragment)
        }

        addTripBtn.setOnClickListener {
            displayFragment(addTripFragment)
        }

        profileBtn.setOnClickListener {
//            displayFragment(profileFragment)
        }
    }

    private fun displayFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if (displayedFragment != null) {
            fragmentTransaction.replace(R.id.acitivty_main_fragment, fragment)
        } else {
            fragmentTransaction.add(R.id.acitivty_main_fragment, fragment)
        }
        fragmentTransaction.addToBackStack("ADD_TRIP")
        fragmentTransaction.commit()
        displayedFragment = fragment
    }

    override fun onTripClick(position: Int) {
        val fragment = AddTripFragment.newInstance()
        displayFragment(fragment)
    }
}