package com.example.ilinktrip

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class RegisterActivity : AppCompatActivity(), LandingPageFragment.OnGetStartedClickListener {
    private var displayedFragment: Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val landingPageFragment = LandingPageFragment.newInstance()
        landingPageFragment.setOnGetStartedClickListener(this)
        displayFragment(landingPageFragment)
    }

    private fun displayFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if (displayedFragment != null) {
            fragmentTransaction.replace(R.id.register_main_fragment, fragment)
        } else {
            fragmentTransaction.add(R.id.register_main_fragment, fragment)
        }
        fragmentTransaction.addToBackStack("ADD_TRIP")
        fragmentTransaction.commit()
        displayedFragment = fragment
    }

    override fun onGetStartedClick() {
        val registerFragment = RegisterFragment.newInstance()
        displayFragment(registerFragment)
    }
}