package com.example.mytableorder.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytableorder.fragment.booking.BookingDTO
import com.example.mytableorder.repository.BookingRepository
import com.example.mytableorder.utils.Resource
import kotlinx.coroutines.launch

class BookingViewModel(private val repository: BookingRepository): ViewModel() {


    private val _getBookingResponse : MutableLiveData<Resource<Map<String, Any>?>> = MutableLiveData()

    val getBookingResponse : LiveData<Resource<Map<String, Any>?>> get() = _getBookingResponse





    fun setBookingData(bookingDTO: BookingDTO){
        viewModelScope.launch {
            _getBookingResponse.value = Resource.Loading
            try{
                repository.setBookingList(bookingDTO){
//                    _getBookingResponse.value = it
                }
            }catch (e:Exception){
                _getBookingResponse.value = Resource.Error(e.message.toString())
            }
        }
    }


    fun getBookingData(){
        viewModelScope.launch {
            _getBookingResponse.value = Resource.Loading
            try{
                repository.getBookingList(){
                    _getBookingResponse.value = it
                }
            }catch (e:Exception){
                _getBookingResponse.value = Resource.Error(e.message.toString())
            }
        }
    }

//    fun confirmBookingData(){
//        viewModelScope.launch {
//            _getBookingResponse.value = Resource.Loading
//            try{
//                repository.confirmBookingList(){
//                    _getBookingResponse.value = it
//                }
//            }catch (e:Exception){
//                _getBookingResponse.value = Resource.Error(e.message.toString())
//            }
//        }
//    }
}