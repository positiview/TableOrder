package com.example.mytableorder.fragment.home.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels

import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.mytableorder.R
import com.example.mytableorder.repository.BookingRepository
import com.example.mytableorder.repository.BookingRepositoryImpl
import com.example.mytableorder.utils.Resource
import com.example.mytableorder.viewModel.BookingViewModel
import com.example.mytableorder.viewmodelFactory.BookingViewModelFactory

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class UserDetailsFragment : Fragment(), OnMapReadyCallback {


    private lateinit var googleMap: GoogleMap
    private var shopLocation: LatLng? = null
    private val auth = Firebase.auth

    // 클래스 레벨 변수로 위도와 경도를 저장할 변수를 선언합니다.
    private var raName: String? = null
    private var raImg: String? = null
    private var raMenu: String? = null
    private var raInfo: String? = null
    private var raLatitude: Double? = null
    private var raLongitude: Double? = null
    private var raNum: Int? = null // 이 변수는 리스트에서 아이템을 클릭할 때 넘겨져야 함

    private val bookingRepository: BookingRepository = BookingRepositoryImpl()
    private val viewModel: BookingViewModel by activityViewModels {
        BookingViewModelFactory(bookingRepository)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout: TabLayout = requireActivity().findViewById(R.id.tabs)

        // 지도 프래그먼트를 초기화합니다.
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.userMapView) as SupportMapFragment?
        mapFragment?.getMapAsync(this)



        // 넘겨진 Bundle에서 데이터를 추출합니다.
        arguments?.let { bundle ->
             raName = bundle.getString("raName")
             raImg = bundle.getString("raImg")
             raMenu = bundle.getString("raMenu")
             raInfo = bundle.getString("raInfo")




            // 클래스 레벨 변수인 shopLocation에 값을 할당합니다.
            raLatitude = bundle.getDouble("raLatitude")
            raLongitude = bundle.getDouble("raLongitude")
            raNum = bundle.getInt("raNum")
            viewModel.getBookingCount(raNum)
            viewModel.getBookingCountResponse.observe(viewLifecycleOwner){
                if(it is Resource.Success){
                    if(it.data == 0){
                        Toast.makeText(requireContext(),"현재 예약그룹이 아무도 없습니다.",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireContext(),"현재 ${it.data}그룹이 예약 대기중입니다.",Toast.LENGTH_SHORT).show()
                    }
                }else if(it is Resource.Error){
                    Toast.makeText(requireContext(),"예약 대기 인원을 찾을 수 없습니다.",Toast.LENGTH_SHORT).show()
                }
            }
            if (raLatitude != null && raLongitude != null) {
                shopLocation = LatLng(raLatitude!!, raLongitude!!)
            }

            // 위도와 경도를 LatLng 객체로 변환합니다.
            var shopLocation = LatLng(raLatitude!!, raLongitude!!)

            // 추출된 데이터를 레이아웃에 적용합니다.
            view.findViewById<TextView>(R.id.userShopName).text = raName
            view.findViewById<TextView>(R.id.userShopMenu).text = raMenu
            view.findViewById<TextView>(R.id.userShopInfo).text = raInfo

            // 이미지를 로드합니다. Glide 라이브러리가 필요합니다.
            Glide.with(this)
                .load(raImg)
                .into(view.findViewById<ImageView>(R.id.userShopImg))

            // 지도에 관련 처리를 합니다. 여기에 지도 설정 코드를 넣습니다.
            // 예를 들어, 지도에 마커를 추가하고 카메라 위치를 조정하는 등의 작업이 필요합니다.


        }

        // 리스너로 돌아가는 클릭 리스너
        view.findViewById<MaterialButton>(R.id.buttonList).setOnClickListener {
            tabLayout.getTabAt(2)?.select() // 세 번째 탭을 선택한 상태로 표시
//            findNavController().navigate(R.id.action_userDetailsFragment_to_userListFragment)

        }
        view.findViewById<MaterialButton>(R.id.buttonHome).setOnClickListener {
            tabLayout.getTabAt(0)?.select() // 첫 번째 탭을 선택한 상태로 표시
//            findNavController().navigate(R.id.action_serDetailsFragment_to_homeFragment)
        }

        //예약하기
        view.findViewById<MaterialButton>(R.id.buttonBooking).setOnClickListener {
            /*val bundle = Bundle().apply {
                putString("raName", raName) // 클래스 레벨 변수 raName 사용
                putInt("raNum", raNum!!)
                Log.e("$$","$raNum")
                // 필요하다면 raImg, raMenu 등 다른 정보도 여기에 넣을 수 있습니다.
            }
            findNavController().navigate(R.id.action_userDetailsFragment_to_bookWriteFragment,bundle)*/
            viewModel.checkingReserve()
            // 예약했는지 확인하는 코드
            viewModel.getCheckingReserveResponse.observe(viewLifecycleOwner){
                when(it){
                    is Resource.Loading->{
                        view.findViewById<ProgressBar>(R.id.progress_circular).visibility = View.VISIBLE
                    }
                    is Resource.Success->{
                        view.findViewById<ProgressBar>(R.id.progress_circular).visibility = View.GONE
                        if(it.data){
                            checkDialog()
                        }else{
                            val bundle = Bundle().apply {
                                putString("raName", raName) // 클래스 레벨 변수 raName 사용
                                putInt("raNum", raNum!!)
                                Log.e("$$","$raNum")
                                // 필요하다면 raImg, raMenu 등 다른 정보도 여기에 넣을 수 있습니다.
                            }
                            findNavController().navigate(R.id.action_userDetailsFragment_to_bookWriteFragment,bundle)
                        }

                        // 예약하기 버튼이 클릭되면 BookWriteFragment로 데이터를 넘깁니다.
                    }
                    else ->{
                        view.findViewById<ProgressBar>(R.id.progress_circular).visibility = View.GONE
                        Toast.makeText(requireContext(),"예약 여부 확인 오류.",Toast.LENGTH_SHORT).show()
                    }
                }
            }



        }




        }

    private fun checkDialog() {
        val builder = AlertDialog.Builder(requireContext()).create()
        val dialogView = layoutInflater.inflate(R.layout.confirm_dialog, null)
        val okBtn = dialogView.findViewById<MaterialButton>(R.id.dialogConfirm)
        val cancelBtn = dialogView.findViewById<MaterialButton>(R.id.dialogCancel)

        okBtn.setOnClickListener {
            val bundle = Bundle().apply {
                putString("raName", raName) // 클래스 레벨 변수 raName 사용
                putInt("raNum", raNum!!)
                Log.e("$$","$raNum")
                // 필요하다면 raImg, raMenu 등 다른 정보도 여기에 넣을 수 있습니다.
            }
            builder.dismiss()
            findNavController().navigate(R.id.action_userDetailsFragment_to_bookWriteFragment,bundle)
        }

        cancelBtn.setOnClickListener {
            builder.dismiss()
        }

        builder.setView(dialogView)
        builder.show()
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





