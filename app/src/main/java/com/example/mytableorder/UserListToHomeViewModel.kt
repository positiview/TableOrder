package com.example.mytableorder


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mytableorder.fragment.admin.AdminListDTO
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserListToHomeViewModel() : ViewModel(){

    private val _restaurants = MutableLiveData<List<AdminListDTO>>()
    val restaurants:  LiveData<List<AdminListDTO>> = _restaurants
    init {
        loadRestaurants()
    }

    private fun loadRestaurants() {
        // 데이터 로드 로직을 여기에 추가합니다.
        val databaseReference = FirebaseDatabase.getInstance().getReference("Restaurants")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tempList = mutableListOf<AdminListDTO>()
                for (restaurantSnapshot in snapshot.children) {
                    val restaurant = restaurantSnapshot.getValue(AdminListDTO::class.java)
                    restaurant?.let { tempList.add(it) }
                }
                _restaurants.value = tempList
            }

            override fun onCancelled(error: DatabaseError) {
                // 에러 처리
            }
        })
    }

    fun setRestaurants(list: List<AdminListDTO>) {
        _restaurants.value = list
    }

}