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
import com.squareup.picasso.Picasso

class CountrySpinnerAdapter(val context: Context, var countries: List<Country>) : BaseAdapter() {

    fun setData(data: List<Country>) {
        this.countries = data
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return countries.size
    }

    override fun getItem(p0: Int): Any {
        return countries[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View =
            convertView ?: LayoutInflater.from(context)
                .inflate(R.layout.country_spinner_item, parent, false)
        val countryFlagIv = view.findViewById<ImageView>(R.id.spinner_country_iv)
        val countryNameTv = view.findViewById<TextView>(R.id.spinner_country_name_tv)

        val country = countries[position]
        countryNameTv.setText(country.name.common)
        Picasso.get()
            .load(country.flags.png)
            .resize(70, 20)
            .centerInside()
            .into(countryFlagIv)

        return view
    }
}