package com.example.ilinktrip

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class LandingPageFragment : Fragment() {

    interface OnGetStartedClickListener {
        fun onGetStartedClick()
    }

    private var listener: OnGetStartedClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_landing_page, container, false)
        val getStartedBtn = view.findViewById<Button>(R.id.get_started_btn)

        getStartedBtn.setOnClickListener {
            listener?.onGetStartedClick()
        }

        return view
    }

    fun setOnGetStartedClickListener(listener: OnGetStartedClickListener) {
        this.listener = listener
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LandingPageFragment().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }
}