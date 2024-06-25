package com.example.ilinktrip.interfaces

public interface TripFeedItemClickListener {
    fun onTripClick(position: Int)
    fun onTripEditClick(position: Int)
    fun onTripDeleteClick(position: Int)

}