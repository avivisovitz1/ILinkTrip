package com.example.ilinktrip

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

class AddTripFragment : Fragment() {
    //    private var param1: String? = null
//    private var param2: String? = null
    private var selectedCountry: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_trip, container, false)
        val countriesList = arrayOf("Argentina", "Brazil", "Columbia", "Thailand")
        val adapter =
            ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, countriesList)

        val countrySpinner = view.findViewById<Spinner>(R.id.add_trip_country_spinner)
        countrySpinner.adapter = adapter
        countrySpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedCountry = countriesList[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        return view
    }

    companion object {
        //        @JvmStatic
        fun newInstance() =
            AddTripFragment().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }
}