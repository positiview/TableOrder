package com.example.mytableorder.fragment.board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mytableorder.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

class ModifyFragment : Fragment() {
    private val realtimeDatabase = FirebaseDatabase.getInstance()
    val firestore = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_board_modify, container, false)

        val postId = requireArguments().getString("postId")
        val databaseReference = realtimeDatabase.getReference("boards").child(postId.toString())

        // Firebase Realtime Database에서 데이터 가져오기
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val title = dataSnapshot.child("title").value.toString()
                val content = dataSnapshot.child("content").value.toString()
                val timestamp = dataSnapshot.child("timestamp").value.toString()

                view.findViewById<TextInputEditText>(R.id.titleEditText).setText(title)
                view.findViewById<TextInputEditText>(R.id.contentEditText).setText(content)
                view.findViewById<TextView>(R.id.timeEditText).text = timestamp

                val userId = dataSnapshot.child("userId").value.toString()
                firestore.collection("users").document(userId).get()
                    .addOnSuccessListener { documentSnapshot ->
                        val name = documentSnapshot.getString("name")

                        // 'name' 값을 화면에 표시
                        view.findViewById<TextView>(R.id.nameEditText).text = name
                    }.addOnFailureListener { exception ->
                        // 데이터 가져오기 실패 시 처리를 구현합니다.
                    }

                // 수정 버튼을 클릭하면 Firebase Realtime Database의 데이터를 수정
                val btnModifyConfirm = view.findViewById<Button>(R.id.btnModifySave)
                btnModifyConfirm.setOnClickListener {
                    val newTitle = view.findViewById<TextInputEditText>(R.id.titleEditText).text.toString()
                    val newContent = view.findViewById<TextInputEditText>(R.id.contentEditText).text.toString()
                    // 한국 시간대 설정
                    val koreaTimeZone = TimeZone.getTimeZone("Asia/Seoul")
                    val koreaTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").apply {
                        timeZone = koreaTimeZone
                    }.format(Date())

                    val map = mutableMapOf<String, Any>()
                    map["title"] = newTitle
                    map["content"] = newContent
                    map["timestamp"] = koreaTime

                    databaseReference.updateChildren(map).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val updatedTimestamp = dataSnapshot.child("timestamp").value.toString()
                            view.findViewById<TextView>(R.id.timeEditText).text = updatedTimestamp

                            // 데이터 수정이 성공한 경우, 상세 페이지로 이동
                            postId?.let { id ->
                                val action = ModifyFragmentDirections.actionModifyFragmentToBoardDetailsFragment(id)
                                findNavController().navigate(action)

                            } ?: run {
                                // TODO: postId가 null인 경우의 처리를 구현합니다.
                            }
                        } else {
                            // 데이터 수정이 실패한 경우 처리를 구현
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 데이터 가져오기가 실패한 경우 처리를 구현
            }
        })
        //***********************닫기************************
        val btnModifyBack = view.findViewById<Button>(R.id.btnModifyBack)
        btnModifyBack.setOnClickListener {
            findNavController().navigateUp()
        }

        return view
    }

}