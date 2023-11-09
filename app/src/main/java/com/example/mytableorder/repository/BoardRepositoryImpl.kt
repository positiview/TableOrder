package com.example.mytableorder.repository

import com.example.mytableorder.utils.Resource

class BoardRepositoryImpl:BoardRepository {
    override suspend fun setBoard(result: (Resource<Map<String, Any>?>) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun getBoard(result: (Resource<Map<String, Any>?>) -> Unit) {
        TODO("Not yet implemented")
    }
}