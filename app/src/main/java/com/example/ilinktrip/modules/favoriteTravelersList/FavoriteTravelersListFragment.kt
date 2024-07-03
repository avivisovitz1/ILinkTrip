package com.example.ilinktrip.modules.favoriteTravelersList

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.ilinktrip.entities.User
import com.example.ilinktrip.interfaces.FavoriteTravelerListListeners
import com.example.ilinktrip.modules.favoriteTravelersList.adapter.TravelerRecyclerViewAdapter
import com.example.ilinktrip.utils.LinkingUtils
import com.example.ilinktrip.viewModels.FavoriteTravelersViewModel
import com.example.ilinktrip.viewModels.UserViewModel
import com.ilinktrip.R
import com.ilinktrip.databinding.FragmentTravelersListBinding

class FavoriteTravelersListFragment : Fragment() {
    private var travelersListRecyclerView: RecyclerView? = null
    private var travelersIds: MutableList<String>? = null
    private var adapter: TravelerRecyclerViewAdapter? = null
    private var binding: FragmentTravelersListBinding? = null
    private var userViewModel: UserViewModel? = null
    private var favoriteTravelersViewModel: FavoriteTravelersViewModel? = null
    private var listener: FavoriteTravelerListListeners? = null

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

        adapter = TravelerRecyclerViewAdapter(travelersIds, listener)
        travelersListRecyclerView?.adapter = adapter

        adapter?.listener = object : FavoriteTravelerListListeners {
            override fun onRemoveFavoriteClick(user: User, position: Int) {
                val currentUser = userViewModel?.getCurrentUser()?.value

                if (currentUser != null) {
                    favoriteTravelersViewModel?.deleteFromFavorites(currentUser.id, user.id)
                } else {
                    Toast.makeText(
                        context,
                        "error removing from favorites",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

            override fun onLinkWithUserClick(user: User, position: Int) {
                context?.let {
                    LinkingUtils.checkSendSMSPermissions(
                        user.phoneNumber,
                        it, requireActivity()
                    )
                }
            }
        }

        favoriteTravelersViewModel?.getToastMessage()?.observe(viewLifecycleOwner) { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        return view
    }

    private fun loadFavoriteTravelers() {
        binding!!.favoriteTravelerListProgressBar.visibility = View.VISIBLE

        val user = userViewModel?.getCurrentUser()?.value

        if (user != null) {
            userViewModel?.getUserFavoriteUsersIds()?.observe(viewLifecycleOwner) { favoritesIds ->
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

    override fun onResume() {
        super.onResume()
        loadFavoriteTravelers()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        favoriteTravelersViewModel = ViewModelProvider(this)[FavoriteTravelersViewModel::class.java]
    }
}