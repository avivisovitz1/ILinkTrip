package com.example.ilinktrip.modules.countriesSpinner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.ilinktrip.entities.Country
import com.ilinktrip.R
import com.ilinktrip.databinding.CountrySpinnerItemBinding
import com.ilinktrip.databinding.FragmentProfileBinding
import com.squareup.picasso.Picasso

class CountrySpinnerAdapter(
    val context: Context,
    private var countries: List<Country>?
) :
    BaseAdapter() {
    private var binding: CountrySpinnerItemBinding? = null

    fun setData(data: List<Country>) {
        this.countries = data
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return countries?.size ?: 0
    }

    override fun getItem(p0: Int): Any {
        return countries!!.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        binding = CountrySpinnerItemBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        val countryFlagIv = binding!!.spinnerCountryIv
        val countryNameTv = binding!!.spinnerCountryNameTv

        val country = countries!![position]
        countryNameTv.setText(country.name.common)
        Picasso.get()
            .load(country.flags.png)
            .resize(70, 20)
            .centerInside()
            .into(countryFlagIv)

        return binding!!.root
    }
}