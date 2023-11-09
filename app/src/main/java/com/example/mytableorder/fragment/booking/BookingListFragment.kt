package com.example.mytableorder.fragment.booking

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.mytableorder.R
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class BookingListFragment : Fragment() {
    private lateinit var tvReservationName: TextView
    private lateinit var tvReservationNumber: TextView
    private lateinit var tvReservationCount: TextView
    private lateinit var tvReservationTime: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // View 초기화
        tvReservationName = view.findViewById(R.id.bookUser)
        tvReservationNumber = view.findViewById(R.id.bookNum)
        tvReservationCount = view.findViewById(R.id.bookCount)
        tvReservationTime = view.findViewById(R.id.bookTime)

        // 예약 정보 설정, 예를 들어 BookingDTO 객체를 전달받았다고 가정
        arguments?.let { bundle ->
            val bookingDto: BookingDTO = bundle.getSerializable("bookingDto") as BookingDTO
            displayReservationInfo(bookingDto)
        }

        view.findViewById<Button>(R.id.buttonHome).setOnClickListener {
            // HomeFragment로 이동
            findNavController().navigate(R.id.action_bookignListFragment_to_homeFragment)
        }
    }

    private fun displayReservationInfo(bookingDto: BookingDTO) {
        // TextView에 예약 정보를 표시
        tvReservationName.text = bookingDto.userName
        tvReservationNumber.text = bookingDto.bookNum.toString()
        tvReservationCount.text = bookingDto.memberCount.toString()
        tvReservationTime.text = bookingDto.reservationTime
    }

}