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
import androidx.appcompat.widget.SearchView
import com.google.firebase.database.ServerValue


class BoardFragment : Fragment() {
    private val boardList: MutableList<String> = mutableListOf()
    private val timestampList: MutableList<String> = mutableListOf() // contentList를 초기화합니다.
    private val adapter: BoardListAdapter = BoardListAdapter(boardList, timestampList)
    private val postIdList: MutableList<String> = mutableListOf()
    private var originalBoardList: MutableList<String> = mutableListOf() // 원본 게시글 리스트 초기화
    private var searchQuery: String = "" // 검색어 쿼리 변수 추가

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

        readDataFromFirebase()

        val button = view.findViewById<Button>(R.id.button)
        button.setOnClickListener {
            // 버튼을 클릭하면 "fragment_write" 프래그먼트로 이동
            findNavController().navigate(R.id.action_boardFragment_to_WriteFragment)
        }

        //검색
        val searchView = view.findViewById<SearchView>(R.id.search_view)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // 검색어를 제출할 때 호출되는 메서드
                searchByTitle(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // 검색어가 변경될 때 호출되는 메서드
                searchQuery = newText // 검색어 업데이트
                searchByTitle(searchQuery) // 검색어로 필터링된 결과 표시
                return true
            }
        })


        searchView.setOnCloseListener {
            // 모든 리스트를 표시하는 기능을 구현합니다.
            searchQuery = "" // 검색어 초기화
            boardList.clear()
            boardList.addAll(originalBoardList)

            adapter.notifyDataSetChanged()
            false
        }


        return view
    }

    private fun readDataFromFirebase() {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("boards")
        val query = databaseReference.orderByChild("timestamp").limitToLast(10)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                val newBoardList: MutableList<String> = mutableListOf()
                val newTimestampList: MutableList<String> = mutableListOf()
                val newPostIdList: MutableList<String> = mutableListOf()

                for (dataSnapshot in snapshot.children.reversed()) { // 역순으로 순회하여 최신 데이터부터 가져옴
                    val title = dataSnapshot.child("title").getValue(String::class.java)
                    val timestamp = dataSnapshot.child("timestamp").getValue(String::class.java)
                    val postId = dataSnapshot.key

                    if (title != null) {
                        newBoardList.add(title)
                        newTimestampList.add(timestamp ?: "")

                        if (postId != null) {
                            newPostIdList.add(postId)
                        }
                    }
                }



                boardList.clear()
                boardList.addAll(newBoardList)

                timestampList.clear()
                timestampList.addAll(newTimestampList)

                postIdList.clear()
                postIdList.addAll(newPostIdList)

                originalBoardList = newBoardList // 원본 게시글 리스트 업데이트

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("BoardFragment", "Firebase에서 데이터 읽기 실패: ${error.message}")
            }
        })
    }




    private fun getContentForPosition(position: Int, callback: (String?) -> Unit) {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("boards")
        val title = boardList.getOrNull(position)

        if (title != null) {
            val query = databaseReference.orderByChild("title").equalTo(title)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
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
        } else {
            callback(null) // title이 null인 경우 데이터를 찾지 못한 것으로 처리
        }
    }

    private fun getUserIdForPosition(position: Int, callback: (String?) -> Unit) {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("boards")

        val title = boardList.getOrNull(position)

        if (title != null) {
            val query = databaseReference.orderByChild("title").equalTo(title)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
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
        } else {
            callback(null) // title이 null인 경우 데이터를 찾지 못한 것으로 처리
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val boardRecyclerView = view.findViewById<RecyclerView>(R.id.BoardList)
        boardRecyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val postId = postIdList.getOrNull(position)
                if (postId != null) {
                    // postId를 사용하여 Firebase에서 데이터를 동기적으로 읽어오기
                    getContentForPosition(position) { content ->
                        if (content != null) {
                            getUserIdForPosition(position) { userId ->
                                if (userId != null) {
                                    val title = boardList.getOrNull(position)
                                    val timestamp = timestampList.getOrNull(position)

                                    val bundle = Bundle()
                                    bundle.putString("postId", postId)
                                    bundle.putString("title", title)
                                    bundle.putString("content", content)
                                    bundle.putString("userId", userId)
                                    bundle.putString("timestamp", timestamp)

                                    // 페이지 이동은 데이터 읽기가 완료된 후에 수행
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
                } else {
                    Log.e("Firebase Realtime Database", "Failed to get postId")
                }
            }
        })
        readDataFromFirebase()
    }

    //검색
    private fun searchByTitle(query: String) {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("boards")

        val searchQuery = if (query.isEmpty()) {
            databaseReference // 검색어가 없는 경우 모든 리스트를 가져옴
        } else {
            databaseReference.orderByChild("title").startAt(query).endAt(query + "\uf8ff")
        }

        searchQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val newBoardList: MutableList<String> = mutableListOf()
                val newTimestampList: MutableList<String> = mutableListOf()
                val newPostIdList: MutableList<String> = mutableListOf()

                for (dataSnapshot in snapshot.children) {
                    val title = dataSnapshot.child("title").getValue(String::class.java)
                    val timestamp = dataSnapshot.child("timestamp").getValue(String::class.java)
                    val postId = dataSnapshot.key

                    if (title != null) {
                        newBoardList.add(title)
                        newTimestampList.add(timestamp ?: "")

                        if (postId != null) {
                            newPostIdList.add(postId)
                        }
                    }
                }

                boardList.clear()
                boardList.addAll(newBoardList)

                timestampList.clear()
                timestampList.addAll(newTimestampList)

                postIdList.clear()
                postIdList.addAll(newPostIdList)

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("BoardFragment", "Firebase에서 데이터 읽기 실패: ${error.message}")
            }
        })
    }


}