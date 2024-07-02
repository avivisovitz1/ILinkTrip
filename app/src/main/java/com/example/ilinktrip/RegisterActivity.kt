package com.example.ilinktrip

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ilinktrip.application.GlobalConst
import com.example.ilinktrip.application.ILinkTripApplication
import com.example.ilinktrip.utils.LoginUtils
import com.ilinktrip.R

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        checkAuthentication()
    }

    private fun checkAuthentication() {
        val sharedPreferences = ILinkTripApplication.Globals.appContext?.getSharedPreferences(
            GlobalConst.SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )

        val checked = sharedPreferences?.getString(GlobalConst.AUTHENTICATED_USER, "")
        if (checked.equals("true")) {
            loginIntoApp()
        }
    }

    private fun loginIntoApp() {
        LoginUtils.loginUser(this, MainActivity::class.java) {
            finish()
        }
    }
}

