package com.example.ilinktrip.modules.login

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.ilinktrip.MainActivity
import com.example.ilinktrip.application.GlobalConst
import com.example.ilinktrip.application.ILinkTripApplication
import com.example.ilinktrip.utils.LoginUtils
import com.ilinktrip.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var viewModel: LoginFragmentViewModel? = null
    private var binding: FragmentLoginBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(
            inflater,
            container,
            false
        )

        val view = binding!!.root

        val emailEt = binding!!.loginEmailEt
        val passwordEt = binding!!.loginPasswordEt
        val loginBtn = binding!!.loginBtn
        val progressBar = binding!!.loginProgressBar
        val rememberMeCb = binding!!.loginRememberMeCb

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
}