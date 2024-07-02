package com.example.ilinktrip.modules.registerUser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.RadioButton
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
import com.example.ilinktrip.entities.User
import com.ilinktrip.R
import com.ilinktrip.databinding.FragmentRegisterBinding
import com.squareup.picasso.Picasso


class RegisterFragment : Fragment() {
    private val args by navArgs<RegisterFragmentArgs>()
    private val userDetails by lazy {
        args.userDetails
    }
    private var binding: FragmentRegisterBinding? = null
    private var photosPicker: ActivityResultLauncher<PickVisualMediaRequest>? = null
    private var viewModel: RegisterFragmentViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        photosPicker =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Picasso.get()
                        .load(uri)
                        .resize(150, 150) // Resize to desired dimensions
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
        val idEt = view.findViewById<EditText>(R.id.register_id_et)
        val emailEt = view.findViewById<EditText>(R.id.register_email_et)
        val firstNameEt = view.findViewById<EditText>(R.id.register_first_name_et)
        val lastNameEt = view.findViewById<EditText>(R.id.register_last_name_et)
        val ageEt = view.findViewById<EditText>(R.id.register_age_et)
        val maleGenderRb = view.findViewById<RadioButton>(R.id.register_gender_male_rb)
        val femaleGenderRb = view.findViewById<RadioButton>(R.id.register_gender_female_rb)
        val phoneNumberEt = view.findViewById<EditText>(R.id.register_phone_et)
        val passwordEt = view.findViewById<EditText>(R.id.register_password_et)
        val passwordConfEt = view.findViewById<EditText>(R.id.register_confirm_password_et)
        val choosePhotoIb = view.findViewById<ImageButton>(R.id.register_photo_ib)
        val registerBtn = view.findViewById<Button>(R.id.register_btn)
        val progressBar = view.findViewById<ProgressBar>(R.id.register_progress_bar)

        if (userDetails != null) {
            idEt.setText(userDetails!!.id)
            emailEt.setText(userDetails!!.email)
            firstNameEt.setText(userDetails!!.firstName)
            lastNameEt.setText(userDetails!!.lastName)
            ageEt.setText(userDetails!!.age.toString())

            if (userDetails!!.gender == "male") maleGenderRb.isChecked = true
            else if (userDetails!!.gender == "female") femaleGenderRb.isChecked = true

            phoneNumberEt.setText(userDetails!!.phoneNumber)
            passwordEt.setText(userDetails!!.password)
            passwordConfEt.setText(userDetails!!.password)

            if (userDetails!!.avatarUrl != "") {
                Picasso.get().load(userDetails!!.avatarUrl).resize(150, 150)
                    .into(choosePhotoIb)
            }
        }

        choosePhotoIb.setOnClickListener {
            photosPicker?.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }

        registerBtn.setOnClickListener {
            val gender = if (maleGenderRb.isChecked) "male" else "female"

            val user = User(
                idEt.text.toString(),
                emailEt.text.toString(),
                firstNameEt.text.toString(),
                lastNameEt.text.toString(),
                ageEt.text.toString().toInt(),
                gender,
                phoneNumberEt.text.toString(),
                "",
                passwordEt.text.toString()
            )

            binding!!.registerPhotoIb.isDrawingCacheEnabled = true
            binding!!.registerPhotoIb.buildDrawingCache()
            val bitmap = (binding!!.registerPhotoIb.drawable).toBitmap()

            progressBar.visibility = View.VISIBLE

            if (userDetails == null) {
                viewModel?.signUp(user, bitmap) { successful ->
                    progressBar.visibility = View.GONE
                    if (successful) {
                        val intent = Intent(this.context, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(context, "error signing up", Toast.LENGTH_LONG)
                    }
                }
            } else {
                viewModel?.updateUserData(user, bitmap) { isSuccessful ->
                    progressBar.visibility = View.GONE
                    if (isSuccessful) {
                        Navigation.findNavController(view).popBackStack()
                    } else {
                        Toast.makeText(context, "error updating user data", Toast.LENGTH_LONG)
                    }
                }
            }
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this)[RegisterFragmentViewModel::class.java]
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}