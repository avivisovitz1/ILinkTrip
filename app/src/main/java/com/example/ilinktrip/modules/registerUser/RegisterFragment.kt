package com.example.ilinktrip.modules.registerUser

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.ilinktrip.MainActivity
import com.example.ilinktrip.application.GlobalConst
import com.example.ilinktrip.entities.User
import com.example.ilinktrip.viewModels.UserViewModel
import com.ilinktrip.databinding.FragmentRegisterBinding
import com.squareup.picasso.Picasso


class RegisterFragment : Fragment() {
    private val args by navArgs<RegisterFragmentArgs>()
    private val userDetails by lazy {
        args.userDetails
    }
    private var binding: FragmentRegisterBinding? = null
    private var photosPicker: ActivityResultLauncher<PickVisualMediaRequest>? = null
    private var userViewModel: UserViewModel? = null
    private var viewModel: RegisterFragmentViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        photosPicker =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Picasso.get()
                        .load(uri)
                        .resize(150, 150)
                        .centerInside()
                        .into(binding!!.registerPhotoIb)
                } else {
                    Toast.makeText(
                        this.context,
                        "an error occurred trying to get photo",
                        Toast.LENGTH_SHORT
                    )
                    Log.d("PhotoPicker", "No media selected")
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(
            inflater,
            container,
            false
        )

        val view = binding!!.root
        val choosePhotoIb = binding!!.registerPhotoIb
        val registerBtn = binding!!.registerBtn

        if (userDetails != null) {
            applyUserData()
        }

        choosePhotoIb.setOnClickListener {
            photosPicker?.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }

        registerBtn.setOnClickListener {
            handleSaveClick()
        }

        return view
    }

    private fun applyUserData() {
        val idEt = binding!!.registerIdEt
        val emailEt = binding!!.registerEmailEt
        val passEt = binding!!.registerPasswordEt
        val confirmPassEt = binding!!.registerConfirmPasswordEt

        idEt.setText(userDetails!!.id)
        emailEt.setText(userDetails!!.email)
        binding!!.registerFirstNameEt.setText(userDetails!!.firstName)
        binding!!.registerLastNameEt.setText(userDetails!!.lastName)
        binding!!.registerAgeEt.setText(userDetails!!.age.toString())

        if (userDetails!!.gender == GlobalConst.GENDER_MALE) binding!!.registerGenderMaleRb.isChecked =
            true
        else if (userDetails!!.gender == GlobalConst.GENDER_FEMALE) binding!!.registerGenderFemaleRb.isChecked =
            true


        binding!!.registerPhoneEt.setText(userDetails!!.phoneNumber)
        passEt.setText(userDetails!!.password)
        confirmPassEt.setText(userDetails!!.password)

        idEt.isEnabled = false
        emailEt.isEnabled = false
        passEt.isEnabled = false
        confirmPassEt.isEnabled = false

        if (userDetails!!.avatarUrl != "") {
            Picasso.get().load(userDetails!!.avatarUrl).resize(150, 150)
                .into(binding!!.registerPhotoIb)
        }
    }

    private fun handleSaveClick() {
        val password = binding!!.registerPasswordEt.text.toString()
        val confirmedPassword = binding!!.registerConfirmPasswordEt.text.toString()

        if (password == confirmedPassword) {
            val gender =
                if (binding!!.registerGenderMaleRb.isChecked) GlobalConst.GENDER_MALE else GlobalConst.GENDER_FEMALE

            val user = User(
                binding!!.registerIdEt.text.toString(),
                binding!!.registerEmailEt.text.toString(),
                binding!!.registerFirstNameEt.text.toString(),
                binding!!.registerLastNameEt.text.toString(),
                binding!!.registerAgeEt.text.toString().toInt(),
                gender,
                binding!!.registerPhoneEt.text.toString(),
                "",
                password
            )

            binding!!.registerPhotoIb.isDrawingCacheEnabled = true
            binding!!.registerPhotoIb.buildDrawingCache()
            val bitmap = (binding!!.registerPhotoIb.drawable).toBitmap()

            binding!!.registerProgressBar.visibility = View.VISIBLE

            if (userDetails == null) {
                registerUser(user, bitmap)
            } else {
                updateUserDetails(user, bitmap)
            }
        } else {
            Toast.makeText(
                context,
                "password and confirm password doesn't match",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun registerUser(userDetails: User, bitmap: Bitmap) {
        viewModel?.signUp(userDetails, bitmap) { successful ->
            binding!!.registerProgressBar.visibility = View.GONE
            if (successful) {
                val intent = Intent(this.context, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(context, "error signing up", Toast.LENGTH_LONG)
            }
        }
    }

    private fun updateUserDetails(userDetails: User, bitmap: Bitmap) {
        viewModel?.updateUserData(userDetails, bitmap) { isSuccessful ->
            binding!!.registerProgressBar.visibility = View.GONE
            if (isSuccessful) {
                Navigation.findNavController(binding!!.root).popBackStack()
            } else {
                Toast.makeText(context, "error updating user data", Toast.LENGTH_LONG)
            }
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        viewModel = ViewModelProvider(this)[RegisterFragmentViewModel::class.java]

    }
}