package com.example.mytableorder.repository

import android.util.Log
import androidx.navigation.fragment.findNavController
import com.example.mytableorder.R
import com.example.mytableorder.fragment.booking.BookingDTO
import com.example.mytableorder.utils.Resource
import com.google.firebase.database.FirebaseDatabase



class BookingRepositoryImpl:BookingRepository {
    override suspend fun setBookingList(bookingDto: BookingDTO, result: (Resource<Map<String, Any>?>) -> Unit) {
        // 파이어베이스 데이터베이스 인스턴스를 가져옴
        val database = FirebaseDatabase.getInstance()
        // 'bookings' 노드 아래에 새 데이터를 생성
        val myRef = database.getReference("bookings").push()

        // 데이터베이스에 객체를 저장
        myRef.setValue(bookingDto).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // 성공적으로 데이터베이스에 저장되면 BookingListFragment로 이동
            } else {
                // 실패한 경우 오류 처리
                task.exception?.message?.let {
                    Log.e("BookWriteFragment", it)
                    // 여기에서 사용자에게 오류를 표시하거나 다른 처리를 할 수 있음
                }
            }
        }
    }

    override suspend fun getBookingList(result: (Resource<Map<String, Any>?>) -> Unit) {

    }

    override suspend fun confirmBookingList(result: (Resource<Map<String, Any>?>) -> Unit) {
        TODO("Not yet implemented")
    }
}