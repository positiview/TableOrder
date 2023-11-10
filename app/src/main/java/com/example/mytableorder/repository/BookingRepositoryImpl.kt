package com.example.mytableorder.repository

import com.example.mytableorder.utils.Resource

class BookingRepositoryImpl:BookingRepository {
    override suspend fun setBookingList(result: (Resource<Map<String, Any>?>) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun getBookingList(result: (Resource<Map<String, Any>?>) -> Unit) {

    }

    override suspend fun confirmBookingList(result: (Resource<Map<String, Any>?>) -> Unit) {
        TODO("Not yet implemented")
    }
}