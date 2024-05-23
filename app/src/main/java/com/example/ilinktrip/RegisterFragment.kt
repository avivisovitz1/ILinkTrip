package com.example.ilinktrip

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import androidx.navigation.fragment.navArgs

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