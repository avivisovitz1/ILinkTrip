package com.example.ilinktrip.models

import android.graphics.Bitmap
import com.example.ilinktrip.services.FirebaseStorageService

class MediaModel {
    private val storageService = FirebaseStorageService()

    fun uploadImage(
        name: String,
        folderName: String,
        bitmap: Bitmap,
        callback: (url: String?) -> Unit
    ) {
        storageService.uploadImage(name, folderName, bitmap, { url ->
            if (url != null) {
                callback(url)
            } else {
                callback(null)
            }
        }, {
            callback(null)
        })
    }
}