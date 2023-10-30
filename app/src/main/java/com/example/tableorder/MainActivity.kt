package com.example.tableorder

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.tableorder.data.MemberModel
import com.example.tableorder.databinding.ActivityMainBinding
import com.example.tableorder.retrofit.INetworkService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

//    val URL = "10.100.203.11"


    private fun saveMember() {
        val retrofit: Retrofit =
            Retrofit.Builder()
                .baseUrl("http://10.100.203.11/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        var networkService: INetworkService = retrofit.create(INetworkService::class.java)

        val signOnCall: Call<MemberModel> = networkService.signOn(
            MemberModel(
                username = "이름인부분",
                password = "123",
                email = "123@naver",
                phone = "000-000"
            )
        )

        signOnCall.enqueue(object : Callback<MemberModel> {
            override fun onResponse(call: Call<MemberModel>, response: Response<MemberModel>) {
                if(response.isSuccessful){
                    Log.i("$$ homecall success ", response.body().toString())

                    val result : MemberModel = response.body() as MemberModel

                    Log.d("$$ :::", result.toString())


                } else {
                    Log.i("$$ homecall result resonse not ok", response.code().toString())
                    Log.i("$$ homecall result resonse not ok", response.body().toString())


                }
            }

            override fun onFailure(call: Call<MemberModel>, t: Throwable) {
                call.cancel()
            }
        })

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnId.setOnClickListener {/*
            val backgroundScope = CoroutineScope(Dispatchers.Default)
            backgroundScope.launch {
                saveMember()
            }*/
            saveMember()
        }

    }
}