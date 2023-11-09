package com.example.mytableorder.fragment.board

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytableorder.model.User
import com.example.mytableorder.repository.AuthRepository
import com.example.mytableorder.utils.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

class WriteViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    private val _registerRequest = MutableLiveData<Resource<String>>()
    val registerRequest = _registerRequest as LiveData<Resource<String>>


    fun register(email: String, password: String, user: User){
        viewModelScope.launch {
            _registerRequest.value = Resource.Loading
            try {
                repository.register(email, password, user){
                    _registerRequest.value = it
                }
            }catch (e: Exception){
                _registerRequest.value = Resource.Error(e.message.toString())

            }
        }
    }

}