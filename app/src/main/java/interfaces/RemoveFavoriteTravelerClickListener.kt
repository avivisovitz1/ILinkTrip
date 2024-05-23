package interfaces

import models.User

interface RemoveFavoriteTravelerClickListener {
    fun onRemoveFavoriteClick(user: User)
}