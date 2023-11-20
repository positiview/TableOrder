package com.example.mytableorder.fragment.restaurant

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.mytableorder.adapter.BookedUserListAdapter
import com.example.mytableorder.databinding.FragmentRestaurantBookedusersBinding
import com.example.mytableorder.repository.BookingRepository
import com.example.mytableorder.repository.BookingRepositoryImpl
import com.example.mytableorder.utils.Resource
import com.example.mytableorder.viewModel.BookingViewModel
import com.example.mytableorder.viewmodelFactory.BookingViewModelFactory
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class BookedUserListFragment : Fragment() {
    private lateinit var binding: FragmentRestaurantBookedusersBinding
    private lateinit var bookedUserListAdapter: BookedUserListAdapter
    private val bookingRepository: BookingRepository = BookingRepositoryImpl()
    private val bookingViewModelFactory = BookingViewModelFactory(bookingRepository)
    private val viewModel: BookingViewModel by activityViewModels() { bookingViewModelFactory }
//    private var eventListener: ValueEventListener? = null
    private var raName:String? = null

    private val auth = Firebase.auth
    /*private val viewModel: ViewModel by activityViewModels{  }
    private val adapter by lazy { Adapter() }*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRestaurantBookedusersBinding.inflate(inflater, container, false)
        val view = binding.root
        bookedUserListAdapter = BookedUserListAdapter()
        // 내 식당 번호로 예약자 리스트 얻기
        fetchMyRestNum()
        // 해당 식당 예약자 리스트
        viewModel.getBookingListResponse.observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading ->{
                    binding.progressCircular.visibility = View.VISIBLE
                }
                is Resource.Success ->{
                    binding.progressCircular.visibility = View.GONE
                    Log.d("$$","List<BookingDTO> 여기 : ${it.data} ")
                    bookedUserListAdapter.setBookingList(it.data)
                    binding.restaurantNameView.text = raName
                    binding.bookedCountView.text = bookedUserListAdapter.itemCount.toString()
                }
                else ->{
                    binding.progressCircular.visibility = View.GONE
                }
            }
        }
        binding.btnConfirm.setOnClickListener {
            // 체크된 아이템이 있으면 체크된 아이템들을 삭제하고,
            // 체크된 아이템이 없으면 첫 번째 아이템을 삭제합니다.
            if (bookedUserListAdapter.hasCheckedItems()) {
                bookedUserListAdapter.removeCheckedItems()
            } else {
                bookedUserListAdapter.removeFirstItem()
            }
            fetchMyRestNum()
        }



        binding.recyclerView.adapter = bookedUserListAdapter



       /* viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.getDonations()
        }*/

       /* viewModel.donations.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> {
                    binding.progressCircular.isVisible = true
                }
                is Resource.Success -> {
                    binding.progressCircular.isVisible = false
                    adapter.submitList(state.data)
                }
                is Resource.Error -> {
                    binding.progressCircular.isVisible = false
                    Toast.makeText(requireContext(), "An error occurred", Toast.LENGTH_SHORT).show()
                }
            }
        }*/

        return view
    }

    // 알람(notification) 띄우기
   /* private fun sendNotification(message: String) {
        val channelId = "channel_id"
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Android Oreo 이상에서는 채널을 만들어야 합니다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Notification Channel", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context!!, channelId)
            .setContentTitle("New Booking")
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()

        notificationManager.notify(0, notification)
    }*/
    private fun fetchMyRestNum() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Restaurants")
        val query = databaseReference.orderByChild("userId").equalTo(auth.currentUser?.uid)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                /*if (bookedUserListAdapter.itemCount > prevCount) {
                    sendNotification("New booking added.")
                }*/
                if (snapshot.exists()) {
                    var raNum:Int? = 0

                    for (data in snapshot.children) {
                        raNum = data.child("raNum").getValue(Int::class.java)
                        raName = data.child("raName").getValue(String::class.java)
                        Log.d("$$", "raNum: $raNum")
                        Log.d("$$", "raName: $raName")

                    }
                    viewModel.restaurantBookingData(raNum)
                } else {
                    Log.d("$$", "No matching user found")
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("MyRestNum", "Failed to fetch restaurant number", error.toException())
            }
        })
//        query.addValueEventListener(eventListener!!)
    }
   /* override fun onDestroy() {
        super.onDestroy()
        val databaseReference = FirebaseDatabase.getInstance().getReference("Restaurants")
        val query = databaseReference.orderByChild("userId").equalTo(auth.currentUser?.uid)
        if (eventListener != null) {
            query.removeEventListener(eventListener!!)
        }
    }*/
}

