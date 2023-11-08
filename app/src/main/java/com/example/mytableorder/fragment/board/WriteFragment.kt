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
    // 현재 로그인된 사용자를 가져옵니다.
    val currentUser = auth.currentUser

    if (currentUser != null) {
      val userId = currentUser.uid
      val databaseReference: DatabaseReference = Firebase.database.reference

      // 한국 시간대 설정
      val koreaTimeZone = TimeZone.getTimeZone("Asia/Seoul")
      val koreaTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").apply {
        timeZone = koreaTimeZone
      }.format(Date())

      // 데이터를 저장할 데이터 맵을 만듭니다.
      val postData = mapOf(
        "title" to title,
        "content" to content,
        "userId" to userId,
        "timestamp" to koreaTime // 한국 시간으로 설정
      )

      // "boards"라는 자식 노드에 데이터를 저장합니다.
      val postId = databaseReference.child("boards").push().key

      if (postId != null) {
        // postId가 null이 아닐 때 데이터를 저장합니다.
        databaseReference.child("boards").child(postId).setValue(postData)
        // postId를 Bundle에 추가
        val bundle = Bundle()
        bundle.putString("postId", postId)
        findNavController().navigate(R.id.action_writeFragment_to_BoardFragment, bundle)
      } else {
        // postId가 null인 경우에 대한 오류 처리 또는 로깅을 수행합니다.
        Log.e("WriteFragment", "Failed to generate postId")
      }
    }
  }

}
