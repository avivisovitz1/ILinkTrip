package com.example.ilinktrip.modules.favoriteTravelersList

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.ilinktrip.models.FavoriteTraveler
import com.example.ilinktrip.models.Model
import com.example.ilinktrip.models.User
import com.example.ilinktrip.modules.favoriteTravelersList.adapter.TravelerRecyclerViewAdapter
import com.ilinktrip.R
import com.ilinktrip.databinding.FragmentTravelersListBinding

class FavoriteTravelersListFragment : Fragment() {
    private var travelersListRecyclerView: RecyclerView? = null
    private var travelersIds: MutableList<String>? = null
    private var adapter: TravelerRecyclerViewAdapter? = null
    private var binding: FragmentTravelersListBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTravelersListBinding.inflate(inflater, container, false)
        val view = binding!!.root;

        loadFavoriteTravelers()
        travelersListRecyclerView = view.findViewById(R.id.favorite_traveler_recycler_view)
        binding!!.favoriteTravelerRecyclerView.setHasFixedSize(true)
        binding!!.favoriteTravelerRecyclerView.layoutManager = LinearLayoutManager(context)

        adapter = TravelerRecyclerViewAdapter(travelersIds)
        travelersListRecyclerView?.adapter = adapter
        return view
    }

    fun loadFavoriteTravelers() {
        binding!!.favoriteTravelerListProgressBar.visibility = View.VISIBLE

        Model.instance().getCurrentUser { user ->
            if (user != null) {
                Model.instance().getUserFavoriteTravelers(user.id) { favoritesIds ->
                    val travelersIdsList = favoritesIds.toMutableList()
                    this.travelersIds = travelersIdsList
                    adapter?.setData(travelersIdsList)
                    binding!!.favoriteTravelerListProgressBar.visibility = View.GONE
                }
            } else {
                Toast.makeText(
                    this.context,
                    "error getting your favorite travelers",
                    Toast.LENGTH_SHORT
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadFavoriteTravelers()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FavoriteTravelersListFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}