package com.example.ilinktrip.services

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class FirebaseStorageService {
    private val storage: FirebaseStorage = Firebase.storage

    fun uploadImage(
        name: String, folderName: String, bitmap: Bitmap, onSuccess: (url: String) -> Unit,
        onFailure: () -> Unit
    ) {
        val ref = storage.getReference()
        val imageRef = ref.child("$folderName$name.jpg")

        // Get the data from an ImageView as bytes
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener { error ->
            Log.e("error uploading image", error.message.toString())
            onFailure()
        }.addOnSuccessListener { taskSnapshot ->
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                onSuccess(uri.toString())
            }
        }
    }
}