package com.example.mytableorder.repository

import com.example.mytableorder.fragment.booking.BookingDTO
import com.example.mytableorder.utils.Resource

interface BookingRepository {


    suspend fun setBookingList(bookingDTO: BookingDTO, result: (Resource<Map<String, Any>?>) -> Unit)

    suspend fun getBookingList(result: (Resource<Map<String, Any>?>) -> Unit)

    suspend fun confirmBookingList(result: (Resource<Map<String, Any>?>) -> Unit)
}