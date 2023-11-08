package com.example.mytableorder.fragment.board

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mytableorder.R
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore

class BoardDetailsFragment : Fragment() {
    private val realtimeDatabase = FirebaseDatabase.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var viewModel: BoardDetailsViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewModelProvider를 사용하여 ViewModel 초기화
        viewModel = ViewModelProvider(this).get(BoardDetailsViewModel::class.java)
        // 데이터를 가져와 ViewModel에 저장
        // 데이터를 가져와 ViewModel에 저장
        val postId = requireArguments().getString("postId")
        val title = requireArguments().getString("title")
        val content = requireArguments().getString("content")
        val timestamp = requireArguments().getString("timestamp")
        val userId = requireArguments().getString("userId")

        if (postId != null && title != null && content != null && timestamp != null && userId != null) {
            val board = Board(postId, title, content, timestamp, userId)
            viewModel.selectedPost.value = board
        } else {
            Log.e("BoardDetailsFragment", "데이터가 유효하지 않습니다.")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_board_details, container, false)
        val btnModify = view.findViewById<Button>(R.id.btnModify)
        val btnDelete = view.findViewById<Button>(R.id.btnDelete)
        val btnBack = view.findViewById<Button>(R.id.btnBack)

        val postId = requireArguments().getString("postId")

        val databaseReference = realtimeDatabase.getReference("boards").child(postId.toString())


        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userId = dataSnapshot.child("userId").value.toString()
                val title = dataSnapshot.child("title").value.toString()
                val content = dataSnapshot.child("content").value.toString()
                val timestamp = dataSnapshot.child("timestamp").value.toString()

                view.findViewById<TextView>(R.id.Title_tv).text = title
                view.findViewById<TextView>(R.id.Content_tv).text = content
                view.findViewById<TextView>(R.id.Time_tv).text = timestamp

                // Firestore에서 'name' 값을 가져오기
                firestore.collection("users").document(userId).get()
                    .addOnSuccessListener { documentSnapshot ->
                        val name = documentSnapshot.getString("name")

                        // 'name' 값을 화면에 표시
                        view.findViewById<TextView>(R.id.Name_tv).text = name
                    }.addOnFailureListener { exception ->
                        // 데이터 가져오기 실패 시 처리를 구현합니다.
                    }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 데이터 가져오기가 실패한 경우 처리를 구현
            }
        })



        //************************수정************************
        btnModify.setOnClickListener {
            val postId = postId ?: return@setOnClickListener
            val action = BoardDetailsFragmentDirections.actionBoardDetailsFragmentToModifyFragment(postId)
            findNavController().navigate(action)
        }

        //************************삭제************************

        btnDelete.setOnClickListener {
            val postId = requireArguments().getString("postId")
            Log.d("BoardDetailsFragment", "postId: $postId") // postId 값 로깅

            if (postId != null) {
                // Firebase Realtime Database 경로를 설정
                val databaseReference = realtimeDatabase.getReference("boards").child(postId)

                // 데이터 삭제 작업을 수행
                databaseReference.removeValue()
                    .addOnSuccessListener {
                        Log.d("BoardDetailsFragment", "데이터 삭제 성공")
                        // 데이터를 삭제한 후 필요한 작업을 수행할 수 있습니다.
                        findNavController().navigate(R.id.action_BoardDetailsFragment_to_BoardFragment)
                    }
                    .addOnFailureListener { e ->
                        Log.e("BoardDetailsFragment", "데이터 삭제 실패: $e")
                    }
            } else {
                Log.e("BoardDetailsFragment", "유효하지 않은 postId: $postId")
            }
        }

        //***********************닫기************************
        btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_BoardDetailsFragment_to_BoardFragment)
        }

        return view
    }

}
