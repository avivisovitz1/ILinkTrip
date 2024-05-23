package modules.favoriteTravelersList

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ilinktrip.R
import models.Model
import models.User
import modules.favoriteTravelersList.adapter.TravelerRecyclerViewAdapter

class FavoriteTravelersListFragment : Fragment() {
    private var travelersListRecyclerView: RecyclerView? = null
    private var travelers: MutableList<User>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_travelers_list, container, false)

        travelers = Model.instance().getUserFavoriteTravelers()


        travelersListRecyclerView = view.findViewById(R.id.favorite_traveler_recycler_view)
        travelersListRecyclerView?.setHasFixedSize(true)
        travelersListRecyclerView?.layoutManager = LinearLayoutManager(context)

        val adapter = TravelerRecyclerViewAdapter(travelers)
        travelersListRecyclerView?.adapter = adapter

        return view
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