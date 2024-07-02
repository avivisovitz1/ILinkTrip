package com.example.ilinktrip.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast

object LoginUtils {
    fun loginUser(
        context: Context,
        targetActivity: Class<*>,
        onLoginSuccess: () -> Unit
    ) {
        Toast.makeText(context, "Login Successfully", Toast.LENGTH_SHORT).show()
        val intent = Intent(context, targetActivity)
        context.startActivity(intent)
        onLoginSuccess()
    }
}