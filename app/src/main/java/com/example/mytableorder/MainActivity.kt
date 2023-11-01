package com.example.mytableorder

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mytableorder.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.Manifest
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.Marker
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private lateinit var gMap: GoogleMap
    private lateinit var bindMain: ActivityMainBinding
    
    //viewModel 추가
    private lateinit var viewModel: ShopViewModel

    lateinit var fusedLocationClient : FusedLocationProviderClient


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
    //initializeViewModel
    fun initializeViewModel() : Unit{
        viewModel = ViewModelProvider(this)[ShopViewModel::class.java]

        viewModel.getRestaurantList().observe(this, Observer{
            Log.i("observer", "RestaurantMap ${viewModel.getRestaurantList().value}")
            viewModel.getRestaurantList().value?.let{
                for((key,rest) in it){
                    Log.i("add marker", rest.latitude.toString() + rest.longitude.toString())
                    Log.i("gamap", gMap.toString())
                    gMap?.addMarker(MarkerOptions().position(LatLng(rest.latitude+0.02, rest.longitude)).title(rest.name))
                }
            }
        })

        viewModel.getCurLocation().observe(this, Observer{
            Log.i("observer", "CurLocation")
            viewModel.getCurLocation().value?.let{
                gMap?.addMarker(MarkerOptions().position(LatLng(it.lati, it.longti)).title("나"))
                gMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.lati, it.longti), 15f))
            }

        })
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

    private fun setLastKnownLocation() {
        if(ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION)  === PackageManager.PERMISSION_GRANTED){
            fusedLocationClient.lastLocation
                .addOnSuccessListener(this) { location ->
                    if (location != null) {
                        viewModel.setCurrentLocation( PointD(location.latitude, location.longitude) )
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


        bindMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindMain.root)

        initializeViewModel()

//        val mapFragment: SupportMapFragment? =
//            bindMain.childFragmentManager
//                .findFragmentById(fragmentId) as SupportMapFragment

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        bindMain.buttonCurrent.setOnClickListener {
           setLastKnownLocation()
        }

        var db: FirebaseFirestore = FirebaseFirestore.getInstance()
        db.collection("shops").add(RestaurantInfoDTO("ssis",35.09,129.03))
            .addOnSuccessListener { doc ->
                Log.d("dddd","suceess")
            }
            .addOnFailureListener{
                e -> Log.e("fail","error",e)
            }





    }



    override fun onMapReady(googleMap: GoogleMap) {

        gMap = googleMap
        //mapReady = true
        gMap.setOnMarkerClickListener(this)
        setLastKnownLocation()
        viewModel.getRestaurantListFromServer()
    }




    
    override fun onMarkerClick(p0: Marker): Boolean {
        Log.i("onMarkerClick", p0.title.toString())
        bindMain.textView.text = viewModel.getRestaurantList().value?.get(p0.title.toString()).toString()
        return true
    }



}