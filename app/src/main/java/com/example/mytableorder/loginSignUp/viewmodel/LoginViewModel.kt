package com.example.mytableorder.loginSignUp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mytableorder.repository.AuthRepository
import com.example.mytableorder.utils.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject




class LoginViewModel (
    private val repository: AuthRepository
): ViewModel() {
    private val _loginRequest = MutableLiveData<Resource<Map<String, Any>?>>()
    val loginRequest = _loginRequest as LiveData<Resource<Map<String, Any>?>>

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> get() = _dataLoading



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


}