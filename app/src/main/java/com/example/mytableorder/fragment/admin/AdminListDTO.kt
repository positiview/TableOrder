package com.example.mytableorder.fragment.admin

data class AdminListDTO(
    //레스트랑번호

    var userId : String = "",

    var raNum: Int=0 ,
    //레스토랑 이름
    var raName: String= "" ,
    //레스토랑 이미지 url
    var raImg: String= "",
    //가게 정보
    var raInfo: String = "",
    //레스토랑 메뉴
    var raMenu: String = "",
    //위도
    var raLatitude: Double=0.0,
    //경도
    var raLongitude: Double=0.0
)


