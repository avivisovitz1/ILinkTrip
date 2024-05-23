package modules.favoriteTravelersList.adapter

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ilinktrip.R
import interfaces.RemoveFavoriteTravelerClickListener
import models.Model
import models.User

class TravelerViewHolder(
    itemView: View,
    listener: RemoveFavoriteTravelerClickListener?
) :
    RecyclerView.ViewHolder(itemView) {
    var user: User? = null
    private var userNameTv: TextView? = null
    private val userProfileIV: ImageView? = null

    init {
        userNameTv = itemView.findViewById(R.id.traveler_username_tv)
        val linkTravelerBtn = itemView.findViewById<Button>(R.id.link_traveler_btn)
        val markedFavoriteIb = itemView.findViewById<ImageButton>(R.id.mark_favorite_traveler_btn)

//        linkTravelerBtn.setOnClickListener()

        markedFavoriteIb.setOnClickListener {
            // remove traveler for favorites in db
            markedFavoriteIb.setImageResource(R.drawable.check_star)
            user?.let { it1 -> listener?.onRemoveFavoriteClick(it1) }
        }
    }

    fun bind(user: User?) {
        this.user = user
        this.userNameTv?.text = user?.firstName + " " + user?.lastName
        this.userProfileIV?.setImageResource(R.drawable.girl_avatar)
    }
}