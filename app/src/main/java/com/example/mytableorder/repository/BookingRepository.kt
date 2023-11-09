package com.example.mytableorder.repository

interface BookingRepository {


    suspend fun setBookingList()

    suspend fun getBookingList()

    suspend fun confirmBookingList()
}