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
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mytableorder.R
import com.example.mytableorder.fragment.admin.AdminListDTO
import com.example.mytableorder.model.UserDTO
import com.example.mytableorder.repository.AuthRepository
import com.example.mytableorder.repository.AuthRepositoryImpl
import com.example.mytableorder.repository.BookingRepository
import com.example.mytableorder.repository.BookingRepositoryImpl
import com.example.mytableorder.utils.Resource
import com.example.mytableorder.viewModel.BookingViewModel
import com.example.mytableorder.viewModel.UserViewModel
import com.example.mytableorder.viewmodelFactory.AuthViewModelFactory
import com.example.mytableorder.viewmodelFactory.BookingViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class BookWriteFragment : Fragment() {

    private val bookingRepository: BookingRepository = BookingRepositoryImpl()
    private val authRepository: AuthRepository = AuthRepositoryImpl()
    private var auth: FirebaseAuth = Firebase.auth
    private var raNum:Int = 0
    private lateinit var bookNum:String



    // 이친구가  BookingViewModel 안에 있는 거들고오고
    private val viewModel: BookingViewModel by activityViewModels {
        BookingViewModelFactory(bookingRepository)
    }

    //이친구가 UserViewModel 유저정보  Live 데이터 들고와서 쓸수 있게.
    private val viewModel2: UserViewModel by activityViewModels {
        AuthViewModelFactory(authRepository)
    }

    //    viewModel= ViewModelProvider(bookingRepository).get(BookingDTO::class.java)
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

        // 받은 데이터를 화면에 표시
        val raNameTextView: TextView = view.findViewById(R.id.bookReName)
        val memberCountEditText: EditText = view.findViewById(R.id.bookMemberCount)
        val registerButton: Button = view.findViewById(R.id.buttonBookRegi)
        val bookNumView:TextView = view.findViewById(R.id.bookRaNum)
        val progressBar: ProgressBar = view.findViewById(R.id.progress_circular)
        arguments?.let { bundle ->
            val raName = bundle.getString("raName")
            raNum = bundle.getInt("raNum")


            // 받은 데이터 화면에 표시
            raNameTextView.text = raName
            bookNumView.text = raNum.toString()
        }
        // '등록' 버튼 클릭 리스너 설정
        registerButton.setOnClickListener {
            val user = auth.currentUser
            if (user == null) {
                //                    findNavController().navigate(R.id.action_bookWriteFragment_to_loginFragment)
            }else{



                val memberCount = memberCountEditText.text.toString().toIntOrNull() ?: 0
                val now = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("MMdd")
                val formatted = now.format(formatter)
                val random = java.util.Random()
                val randomNumber = random.nextInt(999)
                bookNum = formatted + String.format("%03d", randomNumber)
                // bookNum 형식 : [오늘날짜 월일+랜덤숫자3자리] 예)1112972  --> 11월 12일 랜덤 숫자 3자리
                Log.d("$$", "bookNum : $bookNum")
                getUserName { userName ->
                    // 현재 시간을 문자열로 변환
                    val currentDateTimeString = now.format(DateTimeFormatter.ISO_DATE_TIME)
                    val bookingDTO = BookingDTO(
                        uid = auth.currentUser!!.uid,
                        userName = userName,
                        restaurantNum = raNum,
                        memberCount = memberCount,
                        reservationTime = currentDateTimeString,
                        bookNum = bookNum
                    )
                    // 파이어베이스 데이터베이스에 저장
                    viewModel.setBookingData(bookingDTO)

                }


                viewModel.setBookingResponse.observe(viewLifecycleOwner) {
                    when (it) {
                        is Resource.Loading -> {
                            progressBar.visibility = View.VISIBLE
                        }

                        is Resource.Success -> {
                            progressBar.visibility = View.GONE
                            Toast.makeText(requireContext(), it.data, Toast.LENGTH_SHORT).show()
                            navigateToNextPage()
                        }

                        is Resource.Error -> {
                            progressBar.visibility = View.GONE
                            Toast.makeText(requireContext(), "예약 실패", Toast.LENGTH_SHORT).show()
                        }
                    }



                }
            }
        }


    }

    private fun getUserName(onUserNameLoaded: (String) -> Unit) {
        Log.d("$$","getUserName 진입")
        viewModel2.getUserInfo()
        viewModel2.getUserInfoResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val userData = resource.data
                    if(userData!=null) {
                        val userName= userData["name"] as? String ?: ""
                        Log.d("$$","userName 은 ? $userName")
                        onUserNameLoaded(userName)
                    }
                }
                is Resource.Error -> {
                    // 오류 처리
                    Toast.makeText(context, resource.string, Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {
                    // 로딩 처리
                }
            }
        }
    }

    private fun navigateToNextPage() {

        findNavController().navigate(R.id.action_bookWriteFragment_to_myBookingFragment)
        // 여기에서 새 페이지로 이동합니다
        // 예를 들어 findNavController().navigate(R.id.action_current_fragment_to_next_fragment)와 같은 코드를 사용할 수 있습니다
    }
}







