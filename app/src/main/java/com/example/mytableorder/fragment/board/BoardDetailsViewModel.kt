package com.example.mytableorder.fragment.board

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mytableorder.utils.Resource

class BoardDetailsViewModel : ViewModel() {
    // 수정할 게시물 데이터를 LiveData로 가지고 있음
    val selectedPost = MutableLiveData<Board>()
    // 생성자에서 데이터를 초기화하는 대신 이 메서드를 사용하여 Board 객체를 생성
    fun selectBoard(postId: String, title: String, content: String, timestamp: String, userId: String) {
        val board = Board(postId, title, content, timestamp, userId)
        selectedPost.value = board
    }

}