package com.example.mytableorder.fragment.booking


import com.example.mytableorder.fragment.admin.AdminListDTO
import com.example.mytableorder.model.UserDTO
import java.time.LocalDateTime

data class BookingDTO(

    //UserDTO DTO 를 들고와서
    val uid: String="",
    val userName: String =  "",

    //AdminListDTO 에서 가게번호 들고와서

    val restaurantNum: Int = 0,

    //예약인원수
    val memberCount: Int=0,
    //예약번호
    val bookNum: Int = 0,

    //예약시간
    val reservationTime:String? = null, // 예약시간저장하는 필드

    //예약취소 확인
    val check: Boolean = false//
)


