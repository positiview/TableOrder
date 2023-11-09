package com.example.mytableorder.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytableorder.model.UpdateUser
import com.example.mytableorder.repository.AuthRepository
import com.example.mytableorder.utils.Resource
import kotlinx.coroutines.launch


class UserViewModel (
    private val repository: AuthRepository
): ViewModel() {

    /*private val _loginRequest = MutableLiveData<Resource<Map<String, Any>?>>() // 쓰기
    val loginRequest = _loginRequest as LiveData<Resource<Map<String, Any>?>> // 읽기*/
    private val _getUserInfoResponse : MutableLiveData<Resource<Map<String, Any>?>> = MutableLiveData()
    val getUserInfoResponse : LiveData<Resource<Map<String, Any>?>> get() = _getUserInfoResponse



    private val _getUserImgResponse : MutableLiveData<Resource<Uri?>> = MutableLiveData()
    val getUserImgResponse : LiveData<Resource<Uri?>> get() = _getUserImgResponse


    private val _deleteUserImgResponse : MutableLiveData<Resource<String>> = MutableLiveData()
    val deleteUserImgResponse : LiveData<Resource<String>> get() = _deleteUserImgResponse


    fun login(email: String, password: String){
        viewModelScope.launch {
            _getUserInfoResponse.value = Resource.Loading
            try {
                repository.login(email, password){
                    _getUserInfoResponse.value = it
                }
            }catch (e: Exception){
                _getUserInfoResponse.value = Resource.Error(e.message.toString())
            }
        }
    }


    fun editUserImage(imagePath: Uri){
        viewModelScope.launch{
            _getUserImgResponse.value = Resource.Loading
            try{
                repository.setUserImage(imagePath){
                    Log.d("$$","setUserImage")
                    _getUserImgResponse.value = it

                }
            }catch (e:Exception){
                _getUserImgResponse.value = Resource.Error(e.message.toString())
            }
        }
    }

    fun getUserImage(){
        viewModelScope.launch {
            _getUserImgResponse.value = Resource.Loading
            try {
                repository.getUserImage() {
                    _getUserImgResponse.value = it
                    Log.d("$$","img Uri : $it")
                }
            }catch (e:Exception){
                _getUserImgResponse.value = Resource.Error(e.message.toString())
            }
        }
    }

    fun deleteUserImage(){
        viewModelScope.launch {
            try{
                repository.deleteUserImage(){
                    _getUserImgResponse.value = it
                }
            }catch (e:Exception){
                _getUserImgResponse.value = Resource.Error(e.message.toString())
            }
        }
    }

    fun getUserInfo(){
        viewModelScope.launch {
            _getUserInfoResponse.value = Resource.Loading
            try{
                repository.getUserInfo(){
                    _getUserInfoResponse.value = it
                }
            }catch (e:Exception){
                _getUserInfoResponse.value = Resource.Error(e.message.toString())
            }
        }
    }

    fun editUserInfo(user: UpdateUser) {
        viewModelScope.launch {
            _getUserInfoResponse.value = Resource.Loading
            try{
                repository.setUserInfo(user){
                    _getUserInfoResponse.value = it
                }
            }catch (e:Exception){
                _getUserInfoResponse.value = Resource.Error(e.message.toString())
            }
        }
    }


}