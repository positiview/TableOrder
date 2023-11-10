package com.example.mytableorder.repository

import android.util.Log
import androidx.navigation.fragment.findNavController
import com.example.mytableorder.Db.db
import com.example.mytableorder.R
import com.example.mytableorder.fragment.booking.BookingDTO
import com.example.mytableorder.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class BookingRepositoryImpl:BookingRepository {
    val database = Firebase.database
    val auth: FirebaseAuth = Firebase.auth
    override suspend fun setBookingList(
        bookingDto: BookingDTO,
        result: (Resource<Map<String, Any>?>) -> Unit
    ) {
        // 파이어베이스 데이터베이스 인스턴스를 가져옴
        val uid = auth.currentUser?.uid
        // 'bookings' 노드 아래에 새 데이터를 생성
        if(uid != null){

            val myRef = database.getReference("bookings").child(uid).push()

            myRef.setValue(bookingDto).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                } else {
                    task.exception?.message?.let {
                        Log.e("BookWriteFragment", it)
                    }
                }
            }
        }


    }

    override suspend fun getBookingList(
        result: (Resource<Map<String, Any>?>) -> Unit
    ) {

        Log.d("$$", "getBookingList Repository 들어감")
        if (auth.currentUser != null) {
            val uid = auth.currentUser!!.uid
            val bookingsRef = database.getReference("bookings/$uid") // 수정된 부분
            Log.d("$$", "booking Ref : $bookingsRef")
            bookingsRef.addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val bookingsMap = HashMap<String, BookingDTO>()
                    Log.d("$$", "bookingMap : $bookingsMap")

                    dataSnapshot.children.forEach { snapshot ->
                        val bookingDTO = snapshot.getValue(BookingDTO::class.java)
                        bookingDTO?.let { bookingsMap[snapshot.key!!] = it }
                    }

                    result.invoke(Resource.Success(bookingsMap))
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    val errorMessage = databaseError.message
                    Log.e("BookingListFragment", errorMessage)
                    result.invoke(Resource.Error(errorMessage))
                }
            })
        }



    }
}
