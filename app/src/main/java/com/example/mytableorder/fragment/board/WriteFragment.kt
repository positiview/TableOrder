package com.example.mytableorder.fragment.board

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mytableorder.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

class WriteFragment : Fragment() {
  private val auth: FirebaseAuth = Firebase.auth
  private val firestore = Firebase.firestore

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.fragment_board_write, container, false)
    val btnReg = view.findViewById<Button>(R.id.btnReg)
    val btnRegBack = view.findViewById<Button>(R.id.btnRegBack)


    btnReg.setOnClickListener {
      // 버튼을 클릭하면 "fragment_write" 프래그먼트로 이동
      findNavController().navigate(R.id.action_writeFragment_to_BoardFragment)
    }

    btnRegBack.setOnClickListener {
      // 버튼을 클릭하면 "fragment_write" 프래그먼트로 이동
      findNavController().navigate(R.id.action_writeFragment_to_BoardFragment)
    }

    // Firebase Realtime Database에 데이터를 저장하려면 여기에 저장 로직을 추가합니다.
    btnReg.setOnClickListener {
      val title = view.findViewById<EditText>(R.id.titleWriteText).text.toString()
      val content = view.findViewById<EditText>(R.id.contentWriteText).text.toString()

      if (title.isNotEmpty() && content.isNotEmpty()) {
        // Firebase Realtime Database에 데이터를 저장하는 함수를 호출합니다.
        saveDataToFirebase(title, content)
      } else {
        // 제목 또는 내용이 비어있을 때 처리를 수행하세요.
      }
    }

    return view
  }

  private fun saveDataToFirebase(title: String, content: String) {
    val currentUser = auth.currentUser

    if (currentUser != null) {
      val userId = currentUser.uid
      val databaseReference: DatabaseReference = Firebase.database.reference

      val koreaTimeZone = TimeZone.getTimeZone("Asia/Seoul")
      val koreaTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").apply {
        timeZone = koreaTimeZone
      }.format(Date())

      val postId = databaseReference.child("boards").push().key

      if (postId != null) {
        // 게시글 데이터 생성
        val postData = mapOf(
          "title" to title,
          "content" to content,
          "userId" to userId,
          "timestamp" to koreaTime,
          "likesCount" to 0
        )

        // 게시글 좋아요 데이터 생성
        val likesData = mapOf(
          "users" to mapOf<String, Boolean>()
        )

        // 게시글과 좋아요 데이터를 함께 저장
        val updates = mapOf<String, Any>(
          "/boards/$postId" to postData,
          "/likes/$postId" to likesData
        )

        databaseReference.updateChildren(updates)
          .addOnSuccessListener {
            val bundle = Bundle()
            bundle.putString("postId", postId)
            findNavController().navigate(R.id.action_writeFragment_to_BoardFragment, bundle)
          }
          .addOnFailureListener { e ->
            Log.e("WriteFragment", "Failed to save post data: $e")
          }
      } else {
        Log.e("WriteFragment", "Failed to generate postId")
      }
    }
  }


}