package com.example.mytableorder.fragment.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytableorder.R
import com.example.mytableorder.UserListToHomeViewModel


//private val imageUrlList: ArrayList<String> URL 리스트받을려면 추가
class HomeFragment() : Fragment() {


  private val viewModel: UserListToHomeViewModel by activityViewModels()
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    val view = inflater.inflate(R.layout.fragment_home, container, false)
    setupRecyclerViews(view)
    return view
  }

  private fun setupRecyclerViews(view: View) {

    val recyclerHorizon = view.findViewById<RecyclerView>(R.id.recyclerHorizon)
    val recyclerVertical = view.findViewById<RecyclerView>(R.id.recyclerVertical)

    viewModel.restaurants.observe(viewLifecycleOwner) { restaurants ->



      recyclerHorizon.layoutManager =
        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
      recyclerHorizon.adapter = ListAdapterHorizontal(restaurants) { position ->
        val selectedRestaurant = restaurants[position]
        val bundle = Bundle().apply {
          putString("raName", selectedRestaurant.raName)
          putString("raImg", selectedRestaurant.raImg)
          putString("raMenu", selectedRestaurant.raMenu)
          putString("raInfo", selectedRestaurant.raInfo)
          putDouble("raLatitude", selectedRestaurant.raLatitude)
          putDouble("raLongitude", selectedRestaurant.raLongitude)
        }
        findNavController().navigate(R.id.action_homFragment_to_user_details_fragment, bundle)

      }

        // Vertical RecyclerView adapter 설정
        recyclerVertical.layoutManager =
          LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        recyclerVertical.adapter = ListAdapterVertical(restaurants) { position ->
          // 클릭 리스너 - 상세 화면 이동 로직
          val selectedRestaurant = restaurants[position]
          val bundle = Bundle().apply {
            putString("raName", selectedRestaurant.raName)
            putString("raImg", selectedRestaurant.raImg)
            putString("raMenu", selectedRestaurant.raMenu)
            putString("raInfo", selectedRestaurant.raInfo)
            putDouble("raLatitude", selectedRestaurant.raLatitude)
            putDouble("raLongitude", selectedRestaurant.raLongitude)
          }
          findNavController().navigate(R.id.action_homFragment_to_user_details_fragment, bundle)
        }

    }
  }
}









