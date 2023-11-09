package com.example.mytableorder.repository

import android.net.Uri
import com.example.mytableorder.model.UserDTO
import com.example.mytableorder.model.updateUser
import com.example.mytableorder.utils.Resource


interface AuthRepository {
    //suspend fun saveUserDetails(user: UserDTO, result: (Resource<UserDTO>) -> Unit)
    suspend fun login(email: String, password: String, result: (Resource<Map<String, Any>?>) -> Unit)
    suspend fun register(email: String, password: String, userDTO: UserDTO, result: (Resource<String>) -> Unit)
    suspend fun logout(result: () -> Unit)

    suspend fun getUserImage(result: (Resource<Uri>)->Unit)
    suspend fun setUserImage(imagePath: Uri, result: (Resource<Uri>)->Unit)
    suspend fun getUserInfo(result: (Resource<Map<String, Any>?>) -> Unit)
    suspend fun updateUserInfo(user: updateUser, result: (Resource<String>) -> Unit)
    suspend fun updateAuthEmail(email: String)

    suspend fun deleteUserImage(result: (Resource<String>) -> Unit)
}
