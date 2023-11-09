package com.example.mytableorder.repository

import android.net.Uri
import com.example.mytableorder.model.UpdateUser
import com.example.mytableorder.model.User

import com.example.mytableorder.utils.Resource


interface AuthRepository {
    //suspend fun saveUserDetails(user: User, result: (Resource<User>) -> Unit)
    suspend fun login(email: String, password: String, result: (Resource<Map<String, Any>?>) -> Unit)
    suspend fun register(email: String, password: String, user: User, result: (Resource<String>) -> Unit)


    suspend fun getUserImage(result: (Resource<Uri>)->Unit)
    suspend fun setUserImage(imagePath: Uri, result: (Resource<Uri>)->Unit)
    suspend fun getUserInfo(result: (Resource<Map<String, Any>?>) -> Unit)
    suspend fun setUserInfo(user: UpdateUser, result: (Resource<Map<String, Any>?>) -> Unit)
    suspend fun updateAuthEmail(email: String)

    suspend fun deleteUserImage(result: (Resource<Uri?>) -> Unit)
}
