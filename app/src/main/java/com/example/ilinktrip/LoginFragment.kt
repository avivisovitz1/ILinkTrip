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
import android.widget.Toast
import com.example.ilinktrip.models.Model
import com.ilinktrip.R

class LoginFragment : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val emailEt = view.findViewById<EditText>(R.id.login_email_et)
        val passwordEt = view.findViewById<EditText>(R.id.login_password_et)
        val loginBtn = view.findViewById<Button>(R.id.login_btn)
        val progressBar = view.findViewById<ProgressBar>(R.id.login_progress_bar)

        loginBtn.setOnClickListener {
            val email = emailEt.text.toString()
            val pass = passwordEt.text.toString()

            if (email != "" && pass != "") {
                progressBar.visibility = View.VISIBLE
                Model.instance().signIn(email, pass) { user ->
                    progressBar.visibility = View.GONE
                    if (user != null) {
                        val intent = Intent(this.context, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this.context,
                            "email or password are incorrect",
                            Toast.LENGTH_LONG
                        ).show()
//                    TODO: log error
                    }
                }
            } else {
                Toast.makeText(this.context, "enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}