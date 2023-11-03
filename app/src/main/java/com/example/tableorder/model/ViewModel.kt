package com.example.tableorder.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AboutUsViewModel : ViewModel() {
    private val _dataList = MutableLiveData<MutableList<String>>()
    val dataList: LiveData<MutableList<String>>
        get() = _dataList

    init {
        _dataList.value = mutableListOf("Item 1", "Item 2", "Item 3")
    }

    fun addItem(item: String) {
        _dataList.value?.add(item)
        _dataList.postValue(_dataList.value)
    }
}
