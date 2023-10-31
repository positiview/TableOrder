package com.example.tableorder.info.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.tableorder.R
import com.example.tableorder.RestaurantInfoDTO
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class InfoFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    override fun onMarkerClick(marker: Marker): Boolean {
        // 마커를 클릭했을 때 실행할 코드를 여기에 작성합니다.
        // 마커 정보를 가져와서 처리하거나 원하는 작업을 수행합니다.

        // 예시: 마커의 타이틀을 가져와서 토스트 메시지로 표시
        val title = marker.title
        if (!title.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "마커 클릭: $title", Toast.LENGTH_SHORT).show()
        }

        // 반환 값은 마커 클릭 이벤트를 처리했음을 나타냅니다.
        return true
    }
    private lateinit var gMap: GoogleMap
    lateinit var fusedLocationClient: FusedLocationProviderClient
    val curLocation: PointD = PointD()
    lateinit var restaurantMap: MutableMap<String, RestaurantInfoDTO>
    val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (it.all { permission -> permission.value == true }) {
            Toast.makeText(requireContext(), "권한 허용됨...", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "권한 거부됨...", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_info, container, false)
        val buttonCurrent = view.findViewById<Button>(R.id.buttonCurrent)
        val textView = view.findViewById<TextView>(R.id.textView)

        buttonCurrent.setOnClickListener {
            getLastKnownLocation()
            val latLng = LatLng(curLocation.x, curLocation.y)
            gMap.addMarker(MarkerOptions().position(latLng).title("여기"))
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkPermission()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        restaurantMap = mutableMapOf()

        val mapFragment = childFragmentManager.findFragmentById(R.id.infoMapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        gMap.setOnMarkerClickListener(this)

        getLastKnownLocation()
        Log.i("현재 위치", curLocation.toString())

        displayRestaurantList()
    }

    fun checkPermission() {
        val status = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
        if (status == PackageManager.PERMISSION_GRANTED) {
            Log.d(">>", "권한 허용됨")
        } else {
            Log.d(">>", "권한 거부됨")
            permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
        }
    }

    private fun getLastKnownLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener(requireActivity()) { location ->
                    if (location != null) {
                        curLocation.x = location.latitude
                        curLocation.y = location.longitude
                        Toast.makeText(requireContext(), "위도: ${curLocation.x}, 경도: ${curLocation.y}", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "위치를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    fun displayRestaurantList() {
        for ((key, rest) in restaurantMap) {
            gMap.addMarker(MarkerOptions().position(LatLng(rest.latitude, rest.longitude)).title(rest.name))
        }
    }

    inner class PointD {
        var x: Double = 0.0
        var y: Double = 0.0

        override fun toString(): String {
            return "PointD(x=$x, y=$y)"
        }
    }

}
