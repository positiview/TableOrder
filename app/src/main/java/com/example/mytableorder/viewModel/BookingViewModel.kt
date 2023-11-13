package com.example.mytableorder.viewModel

import android.net.Uri
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

    private val _setBookingResponse : MutableLiveData<Resource<String>> = MutableLiveData()

    val setBookingResponse : LiveData<Resource<String>> get() = _setBookingResponse

    private val _getBookingListResponse : MutableLiveData<Resource<List<BookingDTO>>?> = MutableLiveData()

    val getBookingListResponse : LiveData<Resource<List<BookingDTO>>?> get() = _getBookingListResponse



    fun setBookingData(bookingDTO: BookingDTO){
        viewModelScope.launch {
            _setBookingResponse.value = Resource.Loading
            try{
                repository.setBookingList(bookingDTO){
                    _setBookingResponse.value = it
                }
            }catch (e:Exception){
                _setBookingResponse.value = Resource.Error(e.message.toString())
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

    fun restaurantBookingData(raNum: Int?){
        viewModelScope.launch {
            _getBookingListResponse.value = Resource.Loading
            try{
                repository.restaurantBookingList(raNum){
                    _getBookingListResponse.value = it
                }
            }catch (e:Exception){
                _getBookingListResponse.value = Resource.Error(e.message.toString())
            }
        }
    }


}