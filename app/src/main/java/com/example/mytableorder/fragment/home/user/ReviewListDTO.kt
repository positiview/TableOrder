package com.example.mytableorder.fragment.home.user

data class Review(
    val storeId: Int,
    val storeName: String,
    val writerName: String,
    val timestamp: Long, // 또는 String 등 작성 시간을 나타내는 필드
    val content: String,
    val imageUrl: String // 이미지를 나타내는 URL 또는 다른 형식으로 사용
)



