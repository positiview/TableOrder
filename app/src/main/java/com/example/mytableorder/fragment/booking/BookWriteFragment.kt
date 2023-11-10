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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.liveData
import androidx.navigation.fragment.findNavController
import com.example.mytableorder.R
import com.example.mytableorder.repository.AuthRepository
import com.example.mytableorder.repository.AuthRepositoryImpl
import com.example.mytableorder.repository.BookingRepository
import com.example.mytableorder.repository.BookingRepositoryImpl
import com.example.mytableorder.utils.Resource
import com.example.mytableorder.viewModel.BookingViewModel
import com.example.mytableorder.viewModel.UserViewModel
import com.example.mytableorder.viewmodelFactory.AuthViewModelFactory
import com.example.mytableorder.viewmodelFactory.BookingViewModelFactory
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class BookWriteFragment : Fragment() {
    private val bookingRepository: BookingRepository = BookingRepositoryImpl()
    private val bookingViewModelFactory: BookingViewModelFactory = BookingViewModelFactory(bookingRepository)
    private val viewModel: BookingViewModel by activityViewModels() { bookingViewModelFactory }
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
            val userName = bundle.getString("userName")

            // 받은 데이터 화면에 표시
            raNameTextView.text = raName

            // '등록' 버튼 클릭 리스너 설정
            registerButton.setOnClickListener {
                val memberCount = memberCountEditText.text.toString().toIntOrNull() ?: 0

                // 현재 시간을 문자열로 변환
                val currentDateTimeString = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)

                // BookingDTO 객체 생성
                val bookingDto = BookingDTO(

                    userName = userName ?: "",
                    resturantNum = raNum,
                    memberCount = memberCount,
                    reservationTime = currentDateTimeString
                )

                // 파이어베이스 데이터베이스에 저장
                // 값 가져오기
                viewModel.setBookingData(bookingDto)
                viewModel.getBookingResponse.observe(viewLifecycleOwner){
                    if(it is Resource.Success){
                        findNavController().navigate(R.id.action_bookWriteFragment_to_bookignListFragment)
                    }
                }


            }
        }
    }



}