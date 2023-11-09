package com.example.mytableorder.repository

import com.example.mytableorder.utils.Resource

interface BookingRepository {


    suspend fun setBookingList(result: (Resource<Map<String, Any>?>) -> Unit)

    suspend fun getBookingList(result: (Resource<Map<String, Any>?>) -> Unit)

    suspend fun confirmBookingList(result: (Resource<Map<String, Any>?>) -> Unit)
}