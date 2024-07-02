package com.example.ilinktrip

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.ilinktrip.application.GlobalConst
import com.example.ilinktrip.application.ILinkTripApplication
import com.example.ilinktrip.models.UserModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ilinktrip.R

class MainActivity : AppCompatActivity() {
    private var navControler: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment: NavHostFragment? =
            supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        navControler = navHostFragment?.navController

        val toolbar = findViewById<Toolbar>(R.id.main_tool_bar)
        setSupportActionBar(toolbar)
        navControler?.let { NavigationUI.setupActionBarWithNavController(this, it) }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.main_bottom_nav)
        navControler?.let { NavigationUI.setupWithNavController(bottomNavigationView, it) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (navControler != null) {
            if (item.itemId == android.R.id.home) {
                navControler!!.popBackStack()
            } else if (item.itemId == R.id.profileFragment) {
                navControler!!.navigate(R.id.action_global_profileFragment)
            } else if (item.itemId === R.id.logoutBtn) {
                signOut()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun signOut() {
        UserModel.instance().signOut()
        val sharedPreferences = ILinkTripApplication.Globals.appContext?.getSharedPreferences(
            GlobalConst.SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )
        val editor = sharedPreferences?.edit()
        editor?.putString(GlobalConst.AUTHENTICATED_USER, "")
        editor?.apply()
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)

        finish()
    }
}