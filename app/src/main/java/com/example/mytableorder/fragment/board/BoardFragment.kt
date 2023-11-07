package com.example.mytableorder.fragment.board

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytableorder.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.mytableorder.fragment.board.OnItemClickListener


class BoardFragment : Fragment() {
    private val boardList: MutableList<String> = mutableListOf()
    private val timestampList: MutableList<String> = mutableListOf() // contentList를 초기화합니다.

    private val adapter: BoardListAdapter = BoardListAdapter(boardList, timestampList)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_board, container, false)
        val boardRecyclerView = view.findViewById<RecyclerView>(R.id.BoardList)

        // RecyclerView 설정
        val layoutManager = LinearLayoutManager(requireContext())
        boardRecyclerView.layoutManager = layoutManager
        boardRecyclerView.adapter = adapter

        // Firebase Realtime Database에서 데이터를 읽어와서 boardList에 추가합니다.
        readDataFromFirebase()

        val button = view.findViewById<Button>(R.id.button)
        button.setOnClickListener {
            // 버튼을 클릭하면 "fragment_write" 프래그먼트로 이동
            findNavController().navigate(R.id.action_boardFragment_to_WriteFragment)
        }

        return view
    }

    private fun readDataFromFirebase() {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("boards")
        val query = databaseReference.orderByChild("timestamp").limitToLast(10) // 정렬 및 가져올 데이터 개수 설정

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                boardList.clear()
                timestampList.clear()

                val reversedBoardList = mutableListOf<String>()
                val reversedTimestampList = mutableListOf<String>()

                for (dataSnapshot in snapshot.children) {
                    val title = dataSnapshot.child("title").getValue(String::class.java)
                    val timestamp = dataSnapshot.child("timestamp").getValue(String::class.java)

                    if (title != null) {
                        reversedBoardList.add(title)
                        reversedTimestampList.add(timestamp ?: "")
                    }
                }

                // 리스트를 뒤집어 원하는 순서로 표시
                boardList.addAll(reversedBoardList.reversed())
                timestampList.addAll(reversedTimestampList.reversed())

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("BoardFragment", "Firebase에서 데이터 읽기 실패: ${error.message}")
            }
        })
    }



    private fun getContentForPosition(position: Int, callback: (String?) -> Unit) {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("boards")
        val query = databaseReference.orderByChild("title").equalTo(boardList.getOrNull(position))
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val content = dataSnapshot.child("content").getValue(String::class.java)
                        if (content != null) {
                            // 데이터가 올바르게 가져와진 경우 콜백을 사용하여 처리
                            callback(content)
                            return
                        }
                    }
                }
                callback(null) // 데이터를 찾지 못한 경우 null 처리
            }

            override fun onCancelled(error: DatabaseError) {
                // 데이터 읽기 실패 또는 오류가 발생했을 때 처리
                Log.e("BoardFragment", "Failed to read data from Firebase: ${error.message}")
                callback(null)
            }
        })
    }

    private fun getUserIdForPosition(position: Int, callback: (String?) -> Unit) {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("boards")
        val query = databaseReference.orderByChild("title").equalTo(boardList.getOrNull(position))
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val userId = dataSnapshot.child("userId").getValue(String::class.java)
                        if (userId != null) {
                            // 데이터가 올바르게 가져와진 경우 콜백을 사용하여 처리
                            callback(userId)
                            return
                        }
                    }
                }
                callback(null) // 데이터를 찾지 못한 경우 null 처리
            }

            override fun onCancelled(error: DatabaseError) {
                // 데이터 읽기 실패 또는 오류가 발생했을 때 처리
                Log.e("BoardFragment", "Failed to read data from Firebase: ${error.message}")
                callback(null)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val boardRecyclerView = view.findViewById<RecyclerView>(R.id.BoardList)
        boardRecyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val bundle = Bundle()
                bundle.putInt("position", position)
                Log.d("Firebase Realtime Database", "Position: $position")

                val title = boardList.getOrNull(position)
                val timestamp = timestampList.getOrNull(position) // 게시물의 timestamp 값 가져오기
                getContentForPosition(position) { content ->
                    if (content != null) {
                        val userId = getUserIdForPosition(position) { userId ->
                            if (userId != null) {
                                bundle.putString("title", title)
                                bundle.putString("content", content)
                                bundle.putString("userId", userId)

                                // timestamp를 Bundle에 추가
                                bundle.putString("timestamp", timestamp)

                                findNavController().navigate(
                                    R.id.action_boardFragment_to_boardDetailsFragment,
                                    bundle
                                )
                            } else {
                                Log.e("Firebase Realtime Database", "Failed to get userId")
                            }
                        }
                    } else {
                        Log.e("Firebase Realtime Database", "Failed to get content")
                    }
                }
            }
        })

    }

}