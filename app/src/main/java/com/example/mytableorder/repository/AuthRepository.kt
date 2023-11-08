package com.example.mytableorder.repository

import android.net.Uri
import com.example.mytableorder.model.User
import com.example.mytableorder.model.updateUser
import com.example.mytableorder.utils.Resource
import com.google.firebase.storage.StorageReference


interface AuthRepository {
    //suspend fun saveUserDetails(user: User, result: (Resource<User>) -> Unit)
    suspend fun login(email: String, password: String, result: (Resource<Map<String, Any>?>) -> Unit)
    suspend fun register(email: String, password: String, user: User, result: (Resource<String>) -> Unit)
    suspend fun logout(result: () -> Unit)

    suspend fun getUserImage(result: (Resource<Uri>)->Unit)
    suspend fun setUserImage(imagePath: Uri, result: (Resource<Uri>)->Unit)
    suspend fun getUserInfo(result: (Resource<Map<String, Any>?>) -> Unit)
    suspend fun updateUserInfo(user: updateUser, result: (Resource<String>) -> Unit)
    suspend fun updateAuthEmail(email: String)

    suspend fun deleteUserImage(result: (Resource<String>) -> Unit)
}
