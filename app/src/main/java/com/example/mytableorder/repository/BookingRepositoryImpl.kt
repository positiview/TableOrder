package com.example.mytableorder.repository

import android.util.Log
import android.widget.Toast
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
        result: (Resource<String>) -> Unit
    ) {
        // 파이어베이스 데이터베이스 인스턴스를 가져옴
        val uid = auth.currentUser?.uid
        // 'bookings' 노드 아래에 새 데이터를 생성
        if (uid != null) {

            val myRef = database.getReference("bookings").child(uid)
            //push()를 빼면 왜 에러가 날까?
            myRef.setValue(bookingDto).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    result.invoke(Resource.Success("예약에 성공했습니다."))
                } else {
                    task.exception?.message?.let {
                        result.invoke(Resource.Error("예약에 실패했습니다."))
                        Log.e("BookWriteFragment", it)
                    }
                }
            }.addOnFailureListener {
                result.invoke(Resource.Error("예약에 실패했습니다."))
            }
        }
    }

    override suspend fun checkingReserve(result: (Resource<Boolean>) -> Unit) {
        val uid = auth.currentUser?.uid
        Log.d("$$","CheckingReserve 진입")
        if(uid != null){
            val ref = FirebaseDatabase.getInstance().getReference("bookings").child(uid)
            ref.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        result.invoke(Resource.Success(true))
                    } else {
                        result.invoke(Resource.Success(false))
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    result(Resource.Error(databaseError.message))
                }
            })
        }
    }

    override suspend fun getBookingCount(raNum: Int?, result: (Resource<Int>) -> Unit) {
        val bookingsRef = database.getReference("bookings")
        val bookingDTOList = mutableListOf<BookingDTO>()
        bookingsRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                    dataSnapshot.children.forEach { bookingSnapshot ->
                        val bookingDTO = bookingSnapshot.getValue(BookingDTO::class.java)
                        if (bookingDTO?.restaurantNum == raNum) {
                            if (bookingDTO != null) {
                                bookingDTOList.add(bookingDTO)
                            }
                        }
                    }


                result(Resource.Success(bookingDTOList.size))
            }

            override fun onCancelled(databaseError: DatabaseError) {
                result(Resource.Error("예약 대기 인원을 찾을 수 없습니다."))
            }
        })
    }

    override suspend fun restaurantBookingList(raNum : Int?, result: (Resource<List<BookingDTO>>?) -> Unit) {
        val bookingsRef = database.getReference("bookings")
        val bookingDTOList = mutableListOf<BookingDTO>()
        Log.d("$$","요요요요raNum : $raNum")
        bookingsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                    dataSnapshot.children.forEach { bookingSnapshot ->
                        val bookingDTO = bookingSnapshot.getValue(BookingDTO::class.java)
                        if (bookingDTO?.restaurantNum == raNum) {
                            if (bookingDTO != null) {
                                bookingDTOList.add(bookingDTO)
                            }
                        }
                    }

                result.invoke(Resource.Success(bookingDTOList))
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("BookingListFragment", databaseError.message)
                result.invoke(Resource.Error(databaseError.message))
            }
        })
    }


    // 내가 방금 예약한 정보를 들고옴
    override suspend fun getBookingList(
        result: (Resource<BookingDTO?>) -> Unit
    ) {

        Log.d("$$", "getBookingList Repository 들어감")
        if (auth.currentUser != null) {
            val uid = auth.currentUser!!.uid
            val bookingsRef = database.getReference("bookings").child(uid) // 수정된 부분
            Log.d("$$", "booking Ref : $bookingsRef")

            bookingsRef.get().addOnSuccessListener { dataSnapshot ->
                val bookingDTO = dataSnapshot.getValue(BookingDTO::class.java)
                result.invoke(Resource.Success(bookingDTO))
            }.addOnFailureListener { exception ->
                val errorMessage = exception.message
                Log.e("BookingListFragment", errorMessage!!)
                result.invoke(Resource.Error(errorMessage))
            }
            /*bookingsRef.addListenerForSingleValueEvent(object :
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
            })*/
        }


    }

    override suspend fun deleteBookingData(result: (Resource<BookingDTO?>) -> Unit) {
        val uid = auth.currentUser!!.uid
        val bookingsRef = database.getReference("bookings").child(uid)

        bookingsRef.removeValue().addOnSuccessListener {

        }.addOnFailureListener { exception ->
            val errorMessage = exception.message ?: "Unknown error"
            result(Resource.Error(errorMessage))
        }

        bookingsRef.get().addOnSuccessListener { dataSnapshot ->
            val bookingDTO = dataSnapshot.getValue(BookingDTO::class.java)
            result.invoke(Resource.Success(bookingDTO))
        }.addOnFailureListener { exception ->

            result.invoke(Resource.Error("예약 취소 완료"))
        }
    }

}
