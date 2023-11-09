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

        // SharedPreferences에서 좋아요 상태를 로드합니다.
        sharedPreferences = requireContext().getSharedPreferences("board_likes", Context.MODE_PRIVATE)

        // 좋아요 상태가 아닌 경우에만 isLikedByUser 변수를 초기화합니다.
        val isLiked = sharedPreferences.getBoolean(postId, false)
        if (isLiked) {
            isLikedByUser = true
            hasLiked = true
        } else {
            isLikedByUser = false
            hasLiked = false
        }

        // 이미지 상태를 SharedPreferences에서 불러와서 좋아요 이미지를 변경합니다
        updateLikeImage()
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

        // 좋아요 수를 표시하는 TextView 초기화
        tvLikes = view.findViewById(R.id.tvLikes)

        // 이미지 상태를 SharedPreferences에서 불러오기
        postId?.let { postId ->
            isLikedByUser = sharedPreferences.getBoolean(postId, false)
        }
        // 좋아요 이미지 변경
        updateLikeImage()

        // 좋아요 수 업데이트
        updateLikes()

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                this@BoardDetailsFragment.snapshot = snapshot // DataSnapshot을 전역 변수에 할당

                // 좋아요 상태를 업데이트
                val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
                val likesSnapshot = snapshot.child("likes")
                if (likesSnapshot.exists()) {
                    val likesData = likesSnapshot.value
                    if (likesData is HashMap<*, *>) {
                        val likesMap = likesData as HashMap<String, Boolean>
                        likes = likesMap.size.toLong() // 좋아요 수는 HashMap의 크기로 정의됩니다

                        // 현재 사용자의 좋아요 상태를 확인
                        isLikedByUser = currentUserId != null && likesMap.containsKey(currentUserId)
                    }
                }

                // 좋아요 이미지 변경
                updateLikeImage()
                postId?.let { saveLikedState(it) }
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

        // 이미지 상태를 SharedPreferences에서 불러오기
        postId?.let { postId ->
            isLikedByUser = sharedPreferences.getBoolean(postId, false)
        }
        // 좋아요 이미지 변경
        updateLikeImage()

        // 좋아요 수 업데이트
        updateLikes()



        val updateData = hashMapOf<String, Any?>() // Any?로 null 허용으로 설정

        ivLike.setOnClickListener {
            // 현재 사용자 ID 가져오기
            val uid = currentUser?.uid ?: return@setOnClickListener
            val userLiked = isLikedByUser


            // Firebase Realtime Database에서 좋아요 상태 업데이트
            databaseReference.child("likes").child(uid).setValue(!userLiked)
                .addOnSuccessListener {
                    // 상태 변경 성공 시 로직
                    isLikedByUser = !userLiked
                    if (isLikedByUser) {
                        likes
                    } else {
                        likes--
                    }
                    updateLikeImage()
                    updateLikes()
                    saveLikedState(postId)  // SharedPreferences에 상태 저장
                    postId?.let { saveLikedState(it) }
                }
                .addOnFailureListener { e ->
                    // 상태 변경 실패 시 로직
                    Log.e("BoardDetailsFragment", "좋아요 상태 업데이트 실패: $e")
                }

        }


        isLikedByUser = likes > 0

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
                    val likesData = likesSnapshot.value

                    if (likesData is HashMap<*, *>) {
                        val likesMap = likesData as HashMap<String, Boolean>
                        likes = likesMap.size.toLong() // 좋아요의 수는 HashMap의 크기로 정의됩니다

                        updateLikes()

                        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
                        if (currentUserId != null) {
                            isLikedByUser = likesMap.containsKey(currentUserId)
                        }
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

        //  버튼 클릭 이벤트 처리
        btnBack.setOnClickListener {
            postId?.let {
                saveLikedState(it)
            }
            findNavController().navigate(R.id.action_BoardDetailsFragment_to_BoardFragment)
        }

        return view
    }

    private fun updateLikeImage() {
        val ivLike = view?.findViewById<ImageView>(R.id.ivLike)
        if (isLikedByUser) {
            ivLike?.setImageResource(R.drawable.heart2) // 좋아요 상태 이미지
        } else {
            ivLike?.setImageResource(R.drawable.heart) // 좋아요 아닌 상태 이미지
        }
    }



    private fun updateLikes() {
        tvLikes.text = "좋아요 $likes 개"
    }

    private fun saveLikedState(postId: String?) {
        if (postId == null) return
        sharedPreferences.edit().apply {
            putBoolean(postId, isLikedByUser)
            apply()
        }
    }


}