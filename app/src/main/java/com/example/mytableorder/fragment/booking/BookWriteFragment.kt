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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_write, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 받은 데이터를 화면에 표시
        val raNameTextView: TextView = view.findViewById(R.id.bookReName)
        val memberCountEditText: EditText = view.findViewById(R.id.bookMemberCount)
        val registerButton: Button = view.findViewById(R.id.buttonBookRegi)

        arguments?.let { bundle ->
            val raName = bundle.getString("raName")
            val raNum = bundle.getInt("raNum")

            // 받은 데이터 화면에 표시
            raNameTextView.text = raName

            // '등록' 버튼 클릭 리스너 설정
            registerButton.setOnClickListener {
                val memberCount = memberCountEditText.text.toString().toIntOrNull() ?: 0

                // 현재 시간을 문자열로 변환
                val currentDateTimeString = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)

                // BookingDTO 객체 생성
                val bookingDto = BookingDTO(
                    userName = raName ?: "",
                    resturantNum = raNum,
                    memberCount = memberCount,
                    reservationTime = currentDateTimeString
                )

                // 파이어베이스 데이터베이스에 저장
                saveBookingToFirebase(bookingDto)
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