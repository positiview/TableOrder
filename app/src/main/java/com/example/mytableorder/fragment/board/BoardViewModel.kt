package com.example.mytableorder.fragment.board

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytableorder.utils.Resource
import kotlinx.coroutines.launch

class BoardViewModel(private val repository : BoardRepository): ViewModel() {

    private val _getBoardResponse : MutableLiveData<Resource<Map<String, Any>?>> = MutableLiveData()
    val getBoardResponse : LiveData<Resource<Map<String, Any>?>> get() = _getBoardResponse


    fun setBoard(){
        viewModelScope.launch {
            _getBoardResponse.value = Resource.Loading
            try {
                repository.setBoard(){
                    _getBoardResponse.value = it
                }
            }catch (e: Exception){
                _getBoardResponse.value = Resource.Error(e.message.toString())
            }
        }
    }

    fun getBoard(){
        viewModelScope.launch {
            _getBoardResponse.value = Resource.Loading
            try {
                repository.getBoard(){
                    _getBoardResponse.value = it
                }
            }catch (e: Exception){
                _getBoardResponse.value = Resource.Error(e.message.toString())
            }
        }
    }

}