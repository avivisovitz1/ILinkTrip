package com.example.ilinktrip.interfaces

import com.example.ilinktrip.entities.User

interface FavoriteTravelerListListeners {
    fun onRemoveFavoriteClick(user: User, position: Int)
    fun onLinkWithUserClick(user: User, position: Int)
}