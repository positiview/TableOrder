package com.example.mytableorder.viewmodelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mytableorder.fragment.booking.BookingDTO
import com.example.mytableorder.repository.AuthRepository
import com.example.mytableorder.repository.BookingRepository
import com.example.mytableorder.viewModel.BookingViewModel
import com.example.mytableorder.viewModel.UserViewModel

class BookingViewModelFactory(
    private val bookingRepository: BookingRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookingViewModel::class.java)) {
            return BookingViewModel(bookingRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}