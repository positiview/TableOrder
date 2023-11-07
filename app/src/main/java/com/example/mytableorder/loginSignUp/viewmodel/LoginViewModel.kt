package com.example.mytableorder.loginSignUp.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mytableorder.repository.AuthRepository
import com.example.mytableorder.utils.Resource
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch
import javax.inject.Inject




class LoginViewModel (
    private val repository: AuthRepository
): ViewModel() {
    private val _loginRequest = MutableLiveData<Resource<Map<String, Any>?>>()
    val loginRequest = _loginRequest as LiveData<Resource<Map<String, Any>?>>

    private val _getUserInfoResponse : MutableLiveData<Resource<Map<String, Any>?>> = MutableLiveData()
    val getUserInfoResponse : LiveData<Resource<Map<String, Any>?>> get() = _getUserInfoResponse

    private val _getPutProfileResponse : MutableLiveData<Resource<Uri>> = MutableLiveData()
    val getPutProfileResponse : LiveData<Resource<Uri>> get() = _getPutProfileResponse


    private val _getUserImgResponse : MutableLiveData<Resource<Uri>> = MutableLiveData()
    val getUserImgResponse : LiveData<Resource<Uri>> get() = _getUserImgResponse

    fun login(email: String, password: String){
        viewModelScope.launch {
            _loginRequest.value = Resource.Loading
            try {
                repository.login(email, password){
                    _loginRequest.value = it
                }
            }catch (e: Exception){
                _loginRequest.value = Resource.Error(e.message.toString())
            }
        }
    }


    fun editUserImage(imagePath: Uri){
        viewModelScope.launch{
            _getPutProfileResponse.value = Resource.Loading
            try{
                repository.setUserImage(imagePath){
                    _getPutProfileResponse.value = it
                }
            }catch (e:Exception){
                _getPutProfileResponse.value = Resource.Error(e.message.toString())
            }
        }
    }

    fun getUserImage(){
        viewModelScope.launch {
            _getUserImgResponse.value = Resource.Loading
            try {
                repository.getUserImage() {
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
}