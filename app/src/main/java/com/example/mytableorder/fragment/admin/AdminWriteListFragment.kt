package com.example.mytableorder.fragment.admin

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.mytableorder.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage


class AdminWriteListFragment : Fragment() {

    //파피어베이스 연결,스토리지 연결,이미지 uri 추가
    private lateinit var db : FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private var addImageUri: Uri? = null

    //등록 해야될 입력 값들 넣기
    private  lateinit var addRaNum: EditText
    private lateinit var addRaName: EditText
    private  lateinit var addBtnImage: Button
    private  lateinit var addRaInfo: EditText
    private  lateinit var addRaMenu: EditText
    private  lateinit var addRaLatitude: EditText
    private  lateinit var addRaLongitude: EditText





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_write_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 버튼 ID 수정
        addBtnImage = view.findViewById(R.id.btnAddImage)

        addBtnImage.setOnClickListener {
            // 이미지 선택기 열기 로직 구현 (openImagePicker() 함수 호출)
            openImagePicker()
        }

        val btnUpdate = view.findViewById<Button>(R.id.btnUpdate)
        btnUpdate.setOnClickListener {
            val raNum = addRaNum.text.toString().toInt()
            val raName = addRaName.text.toString()
            val raInfo = addRaInfo.text.toString()
            val raMenu = addRaMenu.text.toString()
            val raLatitude = addRaLatitude.text.toString().toDouble()
            val raLongitude = addRaLongitude.text.toString().toDouble()

            val adminListDTO = AdminListDTO(
                raNum = raNum,
                raName = raName,
                raImg = "",
                raInfo = raInfo,
                raMenu = raMenu,
                raLatitude = raLatitude,
                raLongitude = raLongitude
            )

            // Firebase Realtime Database에 데이터 쓰기
            val databaseReference = FirebaseDatabase.getInstance().getReference("AdminList")
            databaseReference.child(raNum.toString()).setValue(adminListDTO)
                .addOnSuccessListener {
                    // 데이터베이스 쓰기 성공
                }
                .addOnFailureListener {
                    // 데이터베이스 쓰기 실패
                }
        }
    }

    // 이미지 선택기 열기 로직 (openImagePicker() 함수 구현 필요)
    private fun openImagePicker() {
        // 이미지 선택 로직을 여기에 추가하세요.
    }
}