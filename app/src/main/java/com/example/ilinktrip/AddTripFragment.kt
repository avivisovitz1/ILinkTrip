package com.example.ilinktrip

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.ilinktrip.models.Model
import com.example.ilinktrip.models.Trip
import com.ilinktrip.R
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.Calendar

class AddTripFragment : Fragment() {
    private var selectedCountry: String? = null
    private val calendar: Calendar = Calendar.getInstance()
    private var startsAtEt: EditText? = null
    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
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

        val placeEt = view.findViewById<EditText>(R.id.add_trip_place_et)
        startsAtEt = view.findViewById(R.id.add_trip_starts_at_et)
        val durationEt = view.findViewById<EditText>(R.id.add_trip_duration_et)
        val saveBtn = view.findViewById<Button>(R.id.add_trip_btn)

        startsAtEt?.setOnClickListener { view ->
            openPicker()
        }

        saveBtn.setOnClickListener { view ->
            val country = countrySpinner.selectedItem.toString()
            val place = placeEt.text.toString()
            val startsAt = LocalDate.parse(startsAtEt?.text.toString(), formatter)
            val duration = durationEt.text.toString().toInt()

            Model.instance().getCurrentUser { user ->
                if (user != null) {
                    val trip = Trip(user.id, country, place, startsAt, duration, false)

                    Model.instance().addTrip(trip) {
                        Navigation.findNavController(view).popBackStack()
                    }
                } else {
                    Toast.makeText(this.context, "error saving trip details", Toast.LENGTH_SHORT)
                        .show()
//                    todo: raise error
                }
            }
        }

        return view
    }

    private fun openPicker() {

        context?.let {
            DatePickerDialog(
                it, { DatePicker, year: Int, month: Int, day: Int ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, month, day)

                    // Create Local Date
                    val localDate = LocalDate.of(year, month, day)

                    // Format the LocalDateTime
                    val formattedDate = localDate.format(formatter)

                    startsAtEt?.setText(formattedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        }?.show()
    }

    companion object {
        //        @JvmStatic
        fun newInstance() =
            AddTripFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}