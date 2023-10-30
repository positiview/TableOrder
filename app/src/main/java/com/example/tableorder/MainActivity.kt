package com.example.tableorder

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.Manifest
import com.example.tableorder.databinding.ActivityMainBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.Marker


class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var gMap: GoogleMap
    private lateinit var bindMain: ActivityMainBinding

    //FusedLocationProviderClient 를 이용해서 위치 정보 제공 함
    lateinit var fusedLocationClient : FusedLocationProviderClient

    val curLocation : PointD = PointD()
    lateinit var restaurantMap : MutableMap<String, RestaurantInfoDTO>

    val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        // xml에 있는 권한 file들 하나하나에 대한 설정값을 체크해서 각각
        if (it.all { permission -> permission.value == true }) {
            Toast.makeText(this, "permission permitted...", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "permission denied...", Toast.LENGTH_SHORT).show()
        }
    }

    fun checkPermission(){
        // permission을 확인하고, 없으면 요청한다
        val status = ContextCompat.checkSelfPermission(this,
            "android.permission.ACCESS_FINE_LOCATION")
        if (status == PackageManager.PERMISSION_GRANTED) {
            Log.d(">>", "Permission Granted")
        } else {
            Log.d(">>", "Permission Denied")
            permissionLauncher.launch(
                arrayOf(
                    "android.permission.ACCESS_FINE_LOCATION"
                )
            )
        }
        //End of request for permission
    }

    private fun getLastKnownLocation() {
        if(ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION)  === PackageManager.PERMISSION_GRANTED){
            fusedLocationClient.lastLocation
                .addOnSuccessListener(this) { location ->
                    if (location != null) {
                        curLocation.x = location.latitude
                        curLocation.y = location.longitude
                        Toast.makeText(
                            this,
                            "Latitude: ${curLocation.x}, Longitude: ${curLocation.y}",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermission()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        restaurantMap = mutableMapOf<String, RestaurantInfoDTO>()

        restaurantMap.put("삼환축산", RestaurantInfoDTO("삼환축산", 35.1557217,129.0597084 ))
        restaurantMap.put("멕도날드", RestaurantInfoDTO("멕도날드", 35.1546287, 129.0610991))
        restaurantMap.put("먹담", RestaurantInfoDTO("먹담", 35.1535201, 129.0613613))

        bindMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindMain.root)

//        val mapFragment: SupportMapFragment? =
//            bindMain.childFragmentManager
//                .findFragmentById(fragmentId) as SupportMapFragment

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        bindMain.buttonCurrent.setOnClickListener {
            getLastKnownLocation()
            val latLng = LatLng(curLocation.x, curLocation.y)
            gMap.addMarker(MarkerOptions().position(latLng).title("here"))
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        }
    }

    fun displayRestaurantList(){
        for((key,rest) in restaurantMap){
            gMap.addMarker(MarkerOptions().position(LatLng(rest.latitude, rest.longitude)).title(rest.name))
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {

        gMap = googleMap

        gMap.setOnMarkerClickListener(this)

        getLastKnownLocation()
        Log.i("current Location", curLocation.toString())
        // 초기 위치 설정 및 마커 표시
        displayRestaurantList()

        //val latLng = LatLng(curLocation.x, curLocation.y)
        //gMap.addMarker(MarkerOptions().position(latLng).title("here"))
        //gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    inner class PointD {
        var x : Double = 0.0
        var y : Double = 0.0
        override fun toString(): String {
            return "PointD(x=$x, y=$y)"
        }
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        Log.i("onMarkerClick", p0.title.toString())
        bindMain.textView.text = restaurantMap.get(p0.title.toString()).toString()
        return true
    }


}