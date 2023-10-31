package com.example.tableorder.info
//
//import android.content.pm.PackageManager
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.Manifest
//import android.util.Log
//import android.widget.Toast
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.core.content.ContextCompat
//import com.example.tableorder.databinding.ActivityInfoBinding
//
//import com.example.tableorder.R
//import com.example.tableorder.RestaurantInfoDTO
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.Marker
//import com.google.android.gms.maps.model.MarkerOptions
//
//class InfoActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
//    private lateinit var gMap: GoogleMap
//    private lateinit var bindMain: InfoBinding
//
//    lateinit var fusedLocationClient: FusedLocationProviderClient
//
//    val curLocation: PointD = PointD()
//    lateinit var restaurantMap: MutableMap<String, RestaurantInfoDTO>
//
//    val permissionLauncher = registerForActivityResult(
//        ActivityResultContracts.RequestMultiplePermissions()
//    ) {
//        // XML에 있는 권한 파일들 하나하나에 대한 설정값을 체크해서 각각
//        if (it.all { permission -> permission.value == true }) {
//            Toast.makeText(this, "Permission granted...", Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    fun checkPermission() {
//        // 권한을 확인하고, 없으면 요청합니다.
//        val status = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//        if (status == PackageManager.PERMISSION_GRANTED) {
//            Log.d(">>", "Permission granted")
//        } else {
//            Log.d(">>", "Permission denied")
//            permissionLauncher.launch(
//                arrayOf(
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                )
//            )
//        }
//    }
//
//    private fun getLastKnownLocation() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            fusedLocationClient.lastLocation
//                .addOnSuccessListener(this) { location ->
//                    if (location != null) {
//                        curLocation.x = location.latitude
//                        curLocation.y = location.longitude
//                        Toast.makeText(
//                            this,
//                            "Latitude: ${curLocation.x}, Longitude: ${curLocation.y}",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    } else {
//                        Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show()
//                    }
//                }
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        checkPermission()
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//
//        restaurantMap = mutableMapOf()
////        restaurantMap["AAA"] = RestaurantInfoDTO("AAA", 35.88, 128.59)
////        restaurantMap["BBB"] = RestaurantInfoDTO("BBB", 35.89, 128.58)
////        restaurantMap["CCC"] = RestaurantInfoDTO("CCC", 35.86, 128.605)
//
//        bindMain = ActivityInfoBinding.inflate(layoutInflater)
//        setContentView(bindMain.root)
//
//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//
//        bindMain.buttonCurrent.setOnClickListener {
//            getLastKnownLocation()
//            val latLng = LatLng(curLocation.x, curLocation.y)
//            gMap.addMarker(MarkerOptions().position(latLng).title("here"))
//            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
//        }
//    }
//
//    fun displayRestaurantList() {
//        for ((key, rest) in restaurantMap) {
//            gMap.addMarker(MarkerOptions().position(LatLng(rest.latitude, rest.longitude)).title(rest.name))
//        }
//    }
//
//    override fun onMapReady(googleMap: GoogleMap) {
//        gMap = googleMap
//        gMap.setOnMarkerClickListener(this)
//
//        getLastKnownLocation()
//        Log.i("current Location", curLocation.toString())
//        // 초기 위치 설정 및 마커 표시
//        displayRestaurantList()
//    }
//
//    inner class PointD {
//        var x: Double = 0.0
//        var y: Double = 0.0
//        override fun toString(): String {
//            return "PointD(x=$x, y=$y)"
//        }
//    }
//
//    override fun onMarkerClick(p0: Marker): Boolean {
//        Log.i("onMarkerClick", p0.title.toString())
//        bindMain.textView.text = restaurantMap[p0.title.toString()].toString()
//        return true
//    }
//}