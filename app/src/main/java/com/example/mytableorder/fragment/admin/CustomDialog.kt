package com.example.mytableorder.fragment.admin

import com.example.mytableorder.databinding.DialogCustomBinding
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class CustomDialog : DialogFragment() {


    private val firestore = FirebaseFirestore.getInstance()
    private val userIdList = mutableListOf<String>()
    private val userNameList = mutableListOf<String>()
    private lateinit var adapter: DialogAdapter
    private lateinit var binding: DialogCustomBinding // DialogCustomBinding 변수 추가
    private lateinit var userIdTextView: TextView // userId 텍스트뷰 선언
    private lateinit var userNameTextView: TextView // userName 텍스트뷰 선언

    interface OnItemClickListener {
        fun onItemClicked(userId: String)
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("$$", "onCreateView called")
        binding = DialogCustomBinding.inflate(inflater, container, false) // binding 초기화
        // RecyclerView 초기화 및 설정
        userIdTextView = binding.userId // userId 텍스트뷰 초기화
        userNameTextView = binding.userName
        initRecyclerView(binding.BoardList)
        // Firestore에서 데이터 가져오기
        fetchUserList()

        // 확인 버튼 클릭
        binding.yesButton.setOnClickListener {
            val userId = userIdTextView.text.toString()
            Log.d("$$","userId : $userId")
            // 클릭 이벤트 발생 시 콜백 호출
            onItemClickListener?.onItemClicked(userId)

            dismiss()
        }

        // 취소 버튼 클릭
        binding.noButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
    // 클릭 이벤트 발생 시 userId 값을 텍스트뷰에 설정하는 메서드
    private fun setUserId(userName: String, userId: String) {
        Log.d("$$","setUserName : $userName")
        userNameTextView.text = userName
        userIdTextView.text = userId
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = DialogAdapter(userNameList)
        recyclerView.adapter = adapter

        // DialogAdapter에 클릭 리스너 콜백 설정
        adapter.setOnItemClickListener(object : DialogAdapter.OnItemClickListener {
            override fun onItemClicked(position: Int) {
                val userName = userNameList[position]
                val userId = userIdList[position]
                setUserId(userName, userId) // 클릭 이벤트 발생 시 userId 값을 텍스트뷰에 설정
//                dismiss() // 다이얼로그 닫기
            }
        })
    }

    private fun fetchUserList() {
        firestore.collection("users")
            .whereEqualTo("user_type", "shop") // 사용자의 user_type 필드가 'shop'인 문서 필터링
            .get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot? ->
                if (querySnapshot != null) {
                    for (document in querySnapshot.documents) {

                        val userId = document.id
                        userIdList.add(userId)
                        val userName = document.getString("name")
                        Log.d("Firestore", "User ID: $userId, User Name: $userName")
                        if (userName != null) {
                            userNameList.add(userName)
                        }else{

                            userNameList.add(userId)
                        }
                    }
                    adapter.notifyDataSetChanged() // RecyclerView 어댑터 갱신
                }
            }
            .addOnFailureListener { exception ->
                // 실패 시 처리
            }
    }
}
