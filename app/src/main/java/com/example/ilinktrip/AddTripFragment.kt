package com.example.ilinktrip

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.ilinktrip.models.Country
import com.example.ilinktrip.models.CountryModel
import com.example.ilinktrip.models.Model
import com.example.ilinktrip.models.Trip
import com.example.ilinktrip.modules.countriesSpinner.CountrySpinnerAdapter
import com.ilinktrip.R
import com.ilinktrip.databinding.FragmentAddTripBinding
import com.squareup.picasso.Picasso
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.Calendar
import java.util.UUID

class AddTripFragment : Fragment() {
    private var selectedCountry: String? = null
    private val calendar: Calendar = Calendar.getInstance()
    private var startsAtEt: EditText? = null
    private val saved_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val ui_formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    private var binding: FragmentAddTripBinding? = null
    private var countriesList: List<Country> = listOf()
    private var photosPicker: ActivityResultLauncher<PickVisualMediaRequest>? = null
    private var countriesSpinnerAdapter: CountrySpinnerAdapter? = null
    private val args by navArgs<AddTripFragmentArgs>()
    private val trip by lazy {
        args.trip
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        photosPicker =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Picasso.get()
                        .load(uri)
                        .resize(260, 120)
                        .centerInside()
                        .into(binding!!.tripPhotoIb)
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
        binding = FragmentAddTripBinding.inflate(
            inflater,
            container,
            false
        )

        val view = binding!!.root

        CountryModel.instance().getAllCountries { countriesList ->
            if (countriesList != null) {
                this.countriesList = countriesList
                countriesSpinnerAdapter?.setData(countriesList)
            } else {
                Toast.makeText(this.context, "error getting countries list", Toast.LENGTH_SHORT)
            }
        }

        countriesSpinnerAdapter =
            CountrySpinnerAdapter(requireActivity(), countriesList)

        val countrySpinner = view.findViewById<Spinner>(R.id.add_trip_country_spinner)
        countrySpinner.adapter = countriesSpinnerAdapter
        countrySpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedCountry = countriesList[p2].name.common
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        val placeEt = view.findViewById<EditText>(R.id.add_trip_place_et)
        startsAtEt = view.findViewById(R.id.add_trip_starts_at_et)
        val durationEt = view.findViewById<EditText>(R.id.add_trip_duration_et)
        val checkPhotoIb = view.findViewById<ImageButton>(R.id.trip_photo_ib)
        val markDoneCb = view.findViewById<CheckBox>(R.id.trip_mark_done_cb)
        val saveBtn = view.findViewById<Button>(R.id.add_trip_btn)

        startsAtEt?.setOnClickListener { view ->
            openPicker()
        }

        checkPhotoIb.setOnClickListener {
            photosPicker?.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }

        saveBtn.setOnClickListener { view ->
            val country = countrySpinner.selectedItem as Country
            val place = placeEt.text.toString()
            val startsAt = LocalDate.parse(startsAtEt?.text.toString(), ui_formatter)
            val duration = durationEt.text.toString().toInt()
            val isDone = markDoneCb.isChecked

            binding!!.tripPhotoIb.isDrawingCacheEnabled = true
            binding!!.tripPhotoIb.buildDrawingCache()
            val bitmap = (binding!!.tripPhotoIb.drawable).toBitmap()

            Model.instance().getCurrentUser { user ->
                if (user != null) {
                    val trip =
                        Trip(
                            trip?.id ?: UUID.randomUUID().toString(),
                            user.id,
                            country.name.common ?: "",
                            place,
                            startsAt,
                            duration,
                            "",
                            isDone
                        )

                    Model.instance().uploadTripImage(trip.id, bitmap) { url ->
                        if (url != null) {
                            trip.avatarUrl = url
                            Model.instance().upsertTrip(trip) {
                                Navigation.findNavController(view).popBackStack()
                            }
                        } else {
                            Toast.makeText(
                                this.context,
                                "error saving trip photo",
                                Toast.LENGTH_SHORT
                            )
                        }
                    }
                }
            }
        }

        if (trip != null) {
            val countryName =
                countriesList.indexOfFirst { country -> country.name.common == trip!!.country }
            if (countryName != null) {
                countrySpinner.setSelection(countryName)
            }

            placeEt.setText(trip!!.place)
            startsAtEt?.setText(
                LocalDate.parse(trip!!.startsAt.toString(), saved_formatter).format(ui_formatter)
                    .toString()
            )
            durationEt.setText(trip!!.durationInWeeks.toString())
            markDoneCb.isChecked = trip!!.isDone
            if (trip!!.avatarUrl != "") {
                Picasso.get()
                    .load(trip!!.avatarUrl)
                    .resize(260, 120)
                    .centerInside()
                    .into(binding!!.tripPhotoIb)
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
                    val formattedDate = localDate.format(ui_formatter)

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