package com.example.ilinktrip.interfaces

import com.example.ilinktrip.models.User

interface RemoveFavoriteTravelerClickListener {
    fun onRemoveFavoriteClick(user: User, position: Int)
}