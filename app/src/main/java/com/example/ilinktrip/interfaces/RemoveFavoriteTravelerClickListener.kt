package com.example.ilinktrip.interfaces

import com.example.ilinktrip.entities.User

interface RemoveFavoriteTravelerClickListener {
    fun onRemoveFavoriteClick(user: User, position: Int)
}