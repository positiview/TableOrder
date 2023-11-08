package com.example.mytableorder.fragment.board

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mytableorder.utils.Resource

class BoardDetailsViewModel : ViewModel() {
    // 수정할 게시물 데이터를 LiveData로 가지고 있음
    val selectedPost = MutableLiveData<Board>()


}