package com.example.ilinktrip

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.ilinktrip.models.Model
import com.example.ilinktrip.models.User
import com.ilinktrip.R

class RegisterFragment : Fragment() {
    private val args by navArgs<RegisterFragmentArgs>()
    private val userDetails by lazy {
        args.userDetails
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)
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

            progressBar.visibility = View.VISIBLE

            if (userDetails == null) {
                Model.instance().signUp(user) { isSuccessful ->
                    progressBar.visibility = View.GONE
                    if (isSuccessful) {

                        val intent = Intent(this.context, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this.context,
                            "error while trying to sign up",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
            } else {
                Model.instance().updateUserDetails(user) {
                    progressBar.visibility = View.GONE
                    Navigation.findNavController(view).popBackStack()
                }
            }

        }

        return view
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