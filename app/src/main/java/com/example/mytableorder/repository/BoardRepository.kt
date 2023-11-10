package com.example.mytableorder.repository

import com.example.mytableorder.utils.Resource

interface BoardRepository {
    suspend fun setBoard(result: (Resource<Map<String, Any>?>) -> Unit)

    suspend fun getBoard(result: (Resource<Map<String, Any>?>) -> Unit)
}