package com.example.mytableorder.fragment.board

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mytableorder.R
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore

class BoardDetailsFragment : Fragment() {
    private val realtimeDatabase = FirebaseDatabase.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_boarddetails, container, false)
        val btnModify = view.findViewById<Button>(R.id.btnModify)
        val btnDelete = view.findViewById<Button>(R.id.btnDelete)
        val btnBack = view.findViewById<Button>(R.id.btnBack)

        val postId = requireArguments().getString("postId")
        val title = requireArguments().getString("title")
        val content = requireArguments().getString("content")
        val timestamp = requireArguments().getString("timestamp")
        val userId = requireArguments().getString("userId")

        if (title != null) {
            val titleTextView = view.findViewById<TextView>(R.id.Title_tv)
            titleTextView.text = title
        } else {
            Log.e("BoardDetailsFragment", "데이터가 존재하지 않습니다.")
        }

        if (content != null) {
            val contentTextView = view.findViewById<TextView>(R.id.Content_tv)
            contentTextView.text = content
        } else {
            Log.e("BoardDetailsFragment", "content 데이터가 존재하지 않습니다.")
        }

        if (timestamp != null) {
            val timestampTextView = view.findViewById<TextView>(R.id.Time_tv)
            timestampTextView.text = timestamp // timestamp 화면에 표시
        } else {
            Log.e("BoardDetailsFragment", "Time 데이터가 존재하지 않습니다.")
        }


        if (userId != null) {
            fetchUserNameFromFirestore(userId)
        } else {
            Log.e("BoardDetailsFragment", "userId 데이터가 존재하지 않습니다.")
        }


        btnModify.setOnClickListener {
            findNavController().navigate(R.id.action_boardFragment_to_WriteFragment)
        }

        btnDelete.setOnClickListener {
            if (postId != null) {
                deleteDataFromRealtimeDatabase(postId)
            } else {
                Log.e("BoardDetailsFragment", "유효하지 않은 postId: $postId")
            }
        }

        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        return view
    }

    private fun fetchUserIdFromRealtimeDatabase(postId: String) {
        // Firebase Realtime Database 경로를 설정
        val databaseReference = realtimeDatabase.getReference("boards/$postId/userId")

        // 데이터를 가져오기 위한 이벤트 리스너 등록
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userId = dataSnapshot.getValue(String::class.java)
                if (userId != null) {
                    fetchUserNameFromFirestore(userId) // Firebase Realtime Database에서 userId를 가져온 후 Firestore에서 사용자 이름 가져오도록 수정
                } else {
                    Log.e("BoardDetailsFragment", "User ID not found in Firebase Realtime Database")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(
                    "BoardDetailsFragment",
                    "Failed to read userId from Firebase Realtime Database: ${databaseError.message}"
                )
            }
        })
    }

    private fun fetchUserNameFromFirestore(userId: String) {
        val usersCollection = firestore.collection("users")
        val userDocument = usersCollection.document(userId)

        userDocument.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userName = documentSnapshot.getString("name")
                    if (userName != null) {
                        // 사용자 이름을 R.id.edName TextView에 설정
                        val userNameTextView = view?.findViewById<TextView>(R.id.Name_tv)
                        userNameTextView?.text = userName
                    } else {
                        Log.e("BoardDetailsFragment", "User name not found in Firestore")
                    }
                } else {
                    Log.e("BoardDetailsFragment", "User document not found in Firestore")
                }
            }
            .addOnFailureListener { e ->
                Log.e("BoardDetailsFragment", "Error fetching user document from Firestore: $e")
            }
    }

    private fun deleteDataFromRealtimeDatabase(postId: String) {
        // Firebase Realtime Database 경로를 설정
        val databaseReference = realtimeDatabase.getReference("boards").child(postId)

        // 데이터 삭제 작업을 수행
        databaseReference.removeValue()
            .addOnSuccessListener {
                Log.d("BoardDetailsFragment", "데이터 삭제 성공")
                // 데이터를 삭제한 후 필요한 작업을 수행할 수 있습니다.
            }
            .addOnFailureListener { e ->
                Log.e("BoardDetailsFragment", "데이터 삭제 실패: $e")
            }
    }

}
