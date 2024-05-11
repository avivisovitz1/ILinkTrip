package com.example.ilinktrip

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import interfaces.TripFeedItemClickListener
import modules.tripsFeed.TripsFeedFragment

class MainActivity : AppCompatActivity() {
    private var navControler: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment: NavHostFragment? =
            supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        navControler = navHostFragment?.navController

        val appBarConf = navControler?.let { AppBarConfiguration(it.graph) }
        navControler?.let {
            if (appBarConf != null) {
                findViewById<Toolbar>(R.id.main_tool_bar).setupWithNavController(
                    it,
                    appBarConf
                )
            }
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.main_bottom_nav)
        navControler?.let { NavigationUI.setupWithNavController(bottomNavigationView, it) }

    }
}