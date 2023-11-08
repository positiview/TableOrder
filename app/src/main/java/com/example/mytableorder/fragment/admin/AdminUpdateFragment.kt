package com.example.mytableorder.fragment.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.mytableorder.R
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.FirebaseDatabase

class AdminUpdateFragment : Fragment() {

    private var raName: String? = null
    private var raImg: String? = null
    private var raMenu: String? = null
    private var raInfo: String? = null
    private var raLatitude: Double? = null
    private var raLongitude: Double? = null
    private var raNum: Int? = null // 식별 번호

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            raName = it.getString("raName", "")
            raImg = it.getString("raImg")
            raMenu = it.getString("raMenu")
            raInfo = it.getString("raInfo")
            raLatitude = it.getDouble("raLatitude")
            raLongitude = it.getDouble("raLongitude")
            raNum = it.getInt("raNum")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_update, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 데이터를 UI에 설정
        view.findViewById<EditText>(R.id.shopName).setText(raName)
        view.findViewById<EditText>(R.id.shopMenu).setText(raMenu)
        view.findViewById<EditText>(R.id.shopInfo).setText(raInfo)
        view.findViewById<EditText>(R.id.shopLatitude).setText(raLatitude?.toString() ?: "")
        view.findViewById<EditText>(R.id.shopLongitude).setText(raLongitude?.toString() ?: "")

        // '수정' 버튼 클릭 리스너
        view.findViewById<MaterialButton>(R.id.buttonUpdate).setOnClickListener {
            // 수정 사항을 데이터베이스에 저장하는 로직
            saveUpdatedRestaurantInfo()
        }
    }

    private fun saveUpdatedRestaurantInfo() {
        // UI에서 수정된 데이터를 가져옵니다.
        val updatedName = view?.findViewById<EditText>(R.id.shopName)?.text.toString()
        val updatedMenu = view?.findViewById<EditText>(R.id.shopMenu)?.text.toString()
        val updatedInfo = view?.findViewById<EditText>(R.id.shopInfo)?.text.toString()
        val updatedLatitude = view?.findViewById<EditText>(R.id.shopLatitude)?.text.toString().toDoubleOrNull()
        val updatedLongitude = view?.findViewById<EditText>(R.id.shopLongitude)?.text.toString().toDoubleOrNull()

        // Firebase에 업데이트를 요청합니다.
        raNum?.let {
            val databaseReference = FirebaseDatabase.getInstance().getReference("Restaurants")
            val updateMap = mutableMapOf<String, Any?>(
                "raName" to updatedName,
                "raMenu" to updatedMenu,
                "raInfo" to updatedInfo
            )
            if (updatedLatitude != null && updatedLongitude != null) {
                updateMap["raLatitude"] = updatedLatitude
                updateMap["raLongitude"] = updatedLongitude
            }

            databaseReference.child(it.toString()).updateChildren(updateMap)
                .addOnSuccessListener {
                    Toast.makeText(context, "Restaurant updated successfully", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_adminUpdateFragment_to_adminHomeFragment)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Failed to update restaurant: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } ?: Toast.makeText(context, "Error: Restaurant number is not available", Toast.LENGTH_SHORT).show()
    }

    companion object {
        // newInstance 메소드 생략...
    }
}