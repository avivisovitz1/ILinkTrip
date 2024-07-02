package com.example.ilinktrip.modules.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.ilinktrip.MainActivity
import com.example.ilinktrip.application.GlobalConst
import com.example.ilinktrip.application.ILinkTripApplication
import com.example.ilinktrip.utils.LoginUtils
import com.ilinktrip.R

class LoginFragment : Fragment() {
    private var viewModel: LoginFragmentViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val emailEt = view.findViewById<EditText>(R.id.login_email_et)
        val passwordEt = view.findViewById<EditText>(R.id.login_password_et)
        val loginBtn = view.findViewById<Button>(R.id.login_btn)
        val progressBar = view.findViewById<ProgressBar>(R.id.login_progress_bar)
        val rememberMeCb = view.findViewById<CheckBox>(R.id.login_remember_me_cb)

        loginBtn.setOnClickListener {
            val email = emailEt.text.toString()
            val pass = passwordEt.text.toString()

            if (email != "" && pass != "") {
                progressBar.visibility = View.VISIBLE
                viewModel?.singIn(email, pass) { user ->
                    progressBar.visibility = View.GONE
                    if (user != null) {
                        if (rememberMeCb.isChecked) {
                            saveUserLocally()
                        }
                        loginIntoApp()
                    } else {
                        Toast.makeText(
                            this.context,
                            "email or password are incorrect",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } else {
                Toast.makeText(this.context, "enter email and password", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this)[LoginFragmentViewModel::class.java]
    }

    private fun loginIntoApp() {
        context?.let {
            LoginUtils.loginUser(it, MainActivity::class.java) {
                requireActivity().finish()
            }
        }
    }

    private fun saveUserLocally() {
        val sharedPreferences = ILinkTripApplication.Globals.appContext?.getSharedPreferences(
            GlobalConst.SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )

        val editor = sharedPreferences?.edit()
        editor?.putString(GlobalConst.AUTHENTICATED_USER, "true")
        editor?.apply()
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