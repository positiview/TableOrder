package edu.puo.foodonus.fragments.auth.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
//    private val repository: AuthRepository
): ViewModel() {

//    private val _registerRequest = MutableLiveData<Resource<String>>()
//    val registerRequest = _registerRequest as LiveData<Resource<String>>


//    fun register(email: String, password: String, user: User){
//        viewModelScope.launch {
//            _registerRequest.value = Resource.Loading
//            try {
//                repository.register(email, password, user){
//                    _registerRequest.value = it
//                }
//            }catch (e: Exception){
//                _registerRequest.value = Resource.Error(e.message.toString())
//
//            }
//        }
//    }

}