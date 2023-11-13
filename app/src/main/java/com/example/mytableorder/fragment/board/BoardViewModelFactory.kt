package com.example.mytableorder.fragment.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

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