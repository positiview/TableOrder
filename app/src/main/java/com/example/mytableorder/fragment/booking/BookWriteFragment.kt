package com.example.mytableorder.fragment.booking

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.example.mytableorder.R
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class BookWriteFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_write, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let{ bundle ->
            //UserDateailsFragment 에서 전달한 데이터 받음

            val raName = bundle.getString("raName")
            val raNum = bundle.getInt("raNum")

            //받은데이터 화면에표시
            view.findViewById<TextView>(R.id.bookReName).text=raName

            //'등록표시 클릭리스너 표시'
            view.findViewById<Button>(R.id.buttonBookRegi).setOnClickListener {
                val memberCount = view.findViewById<EditText>(R.id.bookMemberCount).text.toString().toInt()
                // 현재 시간을 ISO 8601 포맷으로 변환 (파이어베이스에서 지원하지 않는 타입은 변환해야 함)
                val currentDateTimeString = DateTimeFormatter.ISO_INSTANT
                    .format(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())

                // BookingDTO 객체 생성
                val bookingDto = BookingDTO(
                    // ... 기존 필드 데이터 설정 ...
                    memberCount = memberCount,
                    reservationTime = currentDateTimeString
                    // ... 나머지 필드도 설정 ...
                )

                // 파이어베이스 데이터베이스에 저장
                saveBookingToFirebase(bookingDto)

                findNavController().navigate(R.id.action_bookWriteFragment_to_bookignListFragment)
            }
        }
    }

    private fun saveBookingToFirebase(bookingDto: BookingDTO) {
        // 파이어베이스 데이터베이스 인스턴스를 가져옴
        val database = FirebaseDatabase.getInstance()
        // 'bookings' 노드 아래에 새 데이터를 생성
        val myRef = database.getReference("bookings").push()

        // 데이터베이스에 객체를 저장
        myRef.setValue(bookingDto).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // 성공적으로 데이터베이스에 저장되면 BookingListFragment로 이동
                findNavController().navigate(R.id.action_bookWriteFragment_to_bookignListFragment)
            } else {
                // 실패한 경우 오류 처리
                task.exception?.message?.let {
                    Log.e("BookWriteFragment", it)
                    // 여기에서 사용자에게 오류를 표시하거나 다른 처리를 할 수 있음
                }
            }
        }
    }
}