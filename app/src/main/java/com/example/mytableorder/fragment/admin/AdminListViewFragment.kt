package com.example.mytableorder.fragment.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.mytableorder.R
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.android.material.button.MaterialButton
class AdminListViewFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private var shopLocation: LatLng? = null

    // 클래스 레벨 변수로 위도와 경도를 저장할 변수를 선언합니다.
    private var raName: String? = null
    private var raImg: String? = null
    private var raMenu: String? = null
    private var raInfo: String? = null
    private var raLatitude: Double? = null
    private var raLongitude: Double? = null
    private var raNum: Int? = null // 이 변수는 리스트에서 아이템을 클릭할 때 넘겨져야 함


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_list_view, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 지도 프래그먼트를 초기화합니다.
        val mapFragment = childFragmentManager.findFragmentById(R.id.shopMapView) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        // 넘겨진 Bundle에서 데이터를 추출합니다.
        arguments?.let { bundle ->
            val raName = bundle.getString("raName")
            val raImg = bundle.getString("raImg")
            val raMenu = bundle.getString("raMenu")
            val raInfo = bundle.getString("raInfo")
            // 클래스 레벨 변수인 shopLocation에 값을 할당합니다.
            raLatitude = bundle.getDouble("raLatitude")
            raLongitude = bundle.getDouble("raLongitude")
            raNum = bundle.getInt("raNum")
            if (raLatitude != null && raLongitude != null) {
                shopLocation = LatLng(raLatitude!!, raLongitude!!)
            }

            // 위도와 경도를 LatLng 객체로 변환합니다.
            var shopLocation = LatLng(raLatitude!!, raLongitude!!)

            // 추출된 데이터를 레이아웃에 적용합니다.
            view.findViewById<TextView>(R.id.shopName).text = raName
            view.findViewById<TextView>(R.id.shopMenu).text = raMenu
            view.findViewById<TextView>(R.id.shopInfo).text = raInfo

            // 이미지를 로드합니다. Glide 라이브러리가 필요합니다.
            Glide.with(this)
                .load(raImg)
                .into(view.findViewById<ImageView>(R.id.shopImg))

            // 지도에 관련 처리를 합니다. 여기에 지도 설정 코드를 넣습니다.
            // 예를 들어, 지도에 마커를 추가하고 카메라 위치를 조정하는 등의 작업이 필요합니다.
        }
        // 수정 버튼 클릭 리스너
        view.findViewById<MaterialButton>(R.id.buttonUpdate).setOnClickListener {
            val updateBundle = Bundle().apply {
                putString("raName", raName)
                putString("raInfo", raInfo)
                putString("raMenu", raMenu)
                putDouble("raLatitude", raLatitude!!)
                putDouble("raLongitude", raLongitude!!)
                raNum?.let { putInt("raNum", it) }
            }
            findNavController().navigate(R.id.action_adminListViewFragment_to_adminUpdateFragment, updateBundle)
        }

        // 삭제 버튼 클릭 리스너
        view.findViewById<MaterialButton>(R.id.buttonDelete).setOnClickListener {
            raNum?.let { num ->
                FirebaseDatabase.getInstance().getReference("Restaurants").child(num.toString())
                    .removeValue().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Restaurant deleted successfully", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack() // 삭제 후 이전 화면으로 돌아감
                        } else {
                            Toast.makeText(context, "Failed to delete restaurant", Toast.LENGTH_SHORT).show()
                        }
                    }
            } ?: Toast.makeText(context, "Error: Restaurant number is not available", Toast.LENGTH_SHORT).show()
        }

        // 뒤로가기 버튼 클릭 리스너
        view.findViewById<MaterialButton>(R.id.buttonBack).setOnClickListener {
            findNavController().navigate(R.id.action_adminListViewFragment_to_adminHomeFragment)
        }

    }
    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        //확대 축소
        googleMap.uiSettings.isZoomControlsEnabled = true
        // 로그를 추가하여 위도와 경도가 올바르게 로드되는지 확인합니다.
        Log.d("AdminListViewFragment", "Lat: $raLatitude, Lng: $raLongitude")

        shopLocation?.let { location ->
            googleMap.addMarker(MarkerOptions().position(location).title("Shop Location"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        } ?: Log.e("AdminListViewFragment", "Shop location is null")
        }





}


