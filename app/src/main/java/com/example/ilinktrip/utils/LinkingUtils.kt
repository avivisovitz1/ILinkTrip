package com.example.ilinktrip.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.ActivityCompat

object LinkingUtils {
    fun checkSendSMSPermissions(phoneNumber: String, context: Context, activity: Activity) {
        context?.let {
            if (ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.SEND_SMS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.SEND_SMS),
                    10
                )
            } else {
                context?.let { this.sendSMS(phoneNumber, it) }
            }
        }
    }
    fun sendSMS(phoneNumber: String, context: Context) {
        if (phoneNumber.isNotEmpty()) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("sms:$phoneNumber")
            context.startActivity(intent)
        }
    }
}