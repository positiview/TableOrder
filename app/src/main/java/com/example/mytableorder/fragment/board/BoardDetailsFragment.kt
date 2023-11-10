package com.example.mytableorder.fragment.board

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mytableorder.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore

class BoardDetailsFragment : Fragment() {
    private val realtimeDatabase = FirebaseDatabase.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var viewModel: BoardDetailsViewModel
    private var postId: String? = null
    private var likes: Long = 0
    private lateinit var tvLikes: TextView // 좋아요 수를 표시하는 TextView
    private var snapshot: DataSnapshot? = null

    private lateinit var sharedPreferences: SharedPreferences
    private var isLikedByUser: Boolean = false
    private var hasLiked: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewModelProvider를 사용하여 ViewModel 초기화
        viewModel = ViewModelProvider(this).get(BoardDetailsViewModel::class.java)
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

        // SharedPreferences 초기화
       /* sharedPreferences = requireContext().getSharedPreferences("board_likes", Context.MODE_PRIVATE)
//        postId = requireArguments().getString("postId")
        isLikedByUser = sharedPreferences.getBoolean(postId, false) // SharedPreferences에서 값 불러오기
        Log.d("$$","isLikedByUser : $isLikedByUser")


        hasLiked = sharedPreferences.getBoolean("hasLiked_$postId", false)*/
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateLikeImage(isLikedByUser)
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
        val ivLike = view.findViewById<ImageView>(R.id.ivLike)

        val postId = requireArguments().getString("postId")

        val databaseReference = realtimeDatabase.getReference("boards").child(postId.toString())
        val userId = requireArguments().getString("userId")
        val currentUser = FirebaseAuth.getInstance().currentUser

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                this@BoardDetailsFragment.snapshot = snapshot // DataSnapshot을 전역 변수에 할당
                // 나머지 코드 생략
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("BoardDetailsFragment", "Failed to get likes from database: $error")
            }
        })

        if (userId == currentUser?.uid) {
            btnModify.visibility = View.VISIBLE
            btnDelete.visibility = View.VISIBLE
        } else {
            btnModify.visibility = View.GONE
            btnDelete.visibility = View.GONE
        }

        // 좋아요 수를 표시하는 TextView 초기화
        tvLikes = view.findViewById(R.id.tvLikes)


        ivLike.setOnClickListener {
            if (isLikedByUser) {
                // 이미 좋아요를 누른 경우
                likes--
                isLikedByUser = false
                ivLike.setImageResource(R.drawable.heart2)

                // 좋아요 상태를 저장하는 코드를 추가합니다. (예: SharedPreferences)
                saveLikedState(postId)

                // 좋아요 수를 업데이트하는 코드
                updateLikes()

                // 좋아요를 누른 사용자 ID를 데이터베이스에서 제거하는 코드
                val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
                if (currentUserId != null) {
                    databaseReference.child("likes").child(currentUserId).removeValue()
                        .addOnSuccessListener {
                            Log.d("BoardDetailsFragment", "사용자 ID 제거 성공")
                        }
                        .addOnFailureListener { e ->
                            Log.e("BoardDetailsFragment", "사용자 ID 제거 실패: $e")
                        }
                }

                // 이미지 변경 코드
                updateLikeImage(isLikedByUser)

                // 여기에 사용자에게 좋아요가 성공적으로 취소되었다는 안내 메시지를 표시하는 코드를 추가할 수 있습니다.
            } else {
                // 좋아요 버튼을 누른 경우의 로직은 이전과 동일합니다.
                likes++
                isLikedByUser = true

                // 좋아요 상태를 저장하는 코드를 추가합니다. (예: SharedPreferences)
                saveLikedState(postId)

                // 좋아요 수를 업데이트하는 코드
                updateLikes()

                // 좋아요를 누른 사용자 ID를 데이터베이스에 저장하는 코드
                val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
                if (currentUserId != null) {
                    val likesData = hashMapOf<String, Boolean>()
                    likesData[currentUserId] = true
                    databaseReference.child("likes").setValue(likesData)
                        .addOnSuccessListener {
                            Log.d("BoardDetailsFragment", "사용자 ID 저장 성공")
                        }
                        .addOnFailureListener { e ->
                            Log.e("BoardDetailsFragment", "사용자 ID 저장 실패: $e")
                        }
                }

                // 이미지 변경 코드
                updateLikeImage(isLikedByUser)

                // 여기에 사용자에게 좋아요가 성공적으로 등록되었다는 안내 메시지를 표시하는 코드를 추가할 수 있습니다.
            }
        }



        isLikedByUser = likes > 0 // 0이 아니면 true

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

                val likesSnapshot = snapshot!!.child("likes")
                if (likesSnapshot.exists()) {
                    val likesData = likesSnapshot.value as HashMap<String, Boolean>
                    likes = likesData.size.toLong() // 좋아요의 수는 HashMap의 크기로 정의됩니다

                    updateLikes()

                    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
                    if (currentUserId != null) {
                        isLikedByUser = likesSnapshot.child(currentUserId).exists()
                        Log.d("$$", "isLikedByUser 상태 :$isLikedByUser")
                        updateLikeImage(isLikedByUser)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("BoardDetailsFragment", "Failed to get likes from database: $error")
            }
        })

        // 수정 버튼 클릭 이벤트 처리
        btnModify.setOnClickListener {
            val postId = postId ?: return@setOnClickListener
            val action = BoardDetailsFragmentDirections.actionBoardDetailsFragmentToModifyFragment(postId)
            findNavController().navigate(action)
        }

        // 삭제 버튼 클릭 이벤트 처리
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

        // 닫기 버튼 클릭 이벤트 처리
        btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_BoardDetailsFragment_to_BoardFragment)
        }

        return view
    }

    // 좋아요 이미지 변경
    private fun updateLikeImage(isLikedByUser:Boolean) {
        val ivLike = view?.findViewById<ImageView>(R.id.ivLike)
        if (isLikedByUser) {
            ivLike?.setImageResource(R.drawable.heart2)
        } else {
            ivLike?.setImageResource(R.drawable.heart)
        }
    }

    private fun updateLikes() {
        tvLikes.text = "좋아요 $likes 개"
    }

    private fun saveLikedState(postId: String?) {
        val postId = postId ?: return  // postId가 null이면 함수를 종료합니다.

        val sharedPreferences = requireContext().getSharedPreferences("board_likes", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(postId, isLikedByUser)
        editor.putBoolean("hasLiked_$postId", hasLiked)
        editor.apply()
    }



}
