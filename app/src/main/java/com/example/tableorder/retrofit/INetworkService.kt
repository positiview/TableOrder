package com.example.tableorder.retrofit

import com.example.tableorder.data.MemberModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface INetworkService {
    @POST("/signOn")
    fun signOn(
        @Body member: MemberModel
    ): Call<MemberModel>

}