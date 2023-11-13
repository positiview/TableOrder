package com.example.mytableorder.fragment.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytableorder.R
import com.example.mytableorder.adapter.AdminListAdapter
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var listAdapter: AdminListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // fragment_admin_list.xml에서 리사이클러뷰를 인플레이트
        val view = inflater.inflate(R.layout.fragment_admin_recyclerview_list, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        listAdapter = AdminListAdapter()
        recyclerView.adapter = listAdapter

        recyclerView.adapter = listAdapter

      //  fetchRestaurantList() // Firebase Realtime Database에서 데이터 가져오기


        return view

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 어댑터를 설정하고 클릭 리스너를 추가합니다.
        listAdapter = AdminListAdapter().apply {
            setOnItemClickListener(object : AdminListAdapter.OnItemClickListener {
                override fun onItemClick(restaurant: AdminListDTO) {
                    // 클릭된 항목의 데이터로 네비게이션 로직을 수행합니다.
                    val bundle = Bundle().apply {
                        putInt("raNum",restaurant.raNum)
                        putString("raName", restaurant.raName)
                        putString("raImg", restaurant.raImg)
                        putString("raMenu", restaurant.raMenu)
                        putString("raInfo", restaurant.raInfo)
                        putDouble("raLatitude",restaurant.raLatitude)
                        putDouble("raLongitude",restaurant.raLongitude)

                        // 필요한 다른 데이터도 여기에 넣을 수 있습니다.
                    }
                    findNavController().navigate(R.id.action_adminListFragment_to_adminListViewFragment, bundle)
                }
            })
        }
        recyclerView.adapter = listAdapter
        // 데이터베이스에서 식당 목록을 가져옵니다.
        fetchRestaurantList()
    }




    private fun fetchRestaurantList() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Restaurants")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val restaurantList = mutableListOf<AdminListDTO>()
                for (restaurantSnapshot in snapshot.children) {
                    val restaurant = restaurantSnapshot.getValue(AdminListDTO::class.java)
                    restaurant?.let { restaurantList.add(it) }
                }
                listAdapter.setRestaurants(restaurantList)
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })
    }




}