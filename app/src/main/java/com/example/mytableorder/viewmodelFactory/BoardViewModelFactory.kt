package com.example.mytableorder.viewmodelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mytableorder.fragment.board.Board
import com.example.mytableorder.fragment.booking.BookingDTO
import com.example.mytableorder.repository.BoardRepository
import com.example.mytableorder.repository.BookingRepository
import com.example.mytableorder.viewModel.BoardViewModel
import com.example.mytableorder.viewModel.BookingViewModel

class BoardViewModelFactory(
    private val repository: BoardRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(Board::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BoardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}