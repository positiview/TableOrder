package com.example.mytableorder.fragment.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytableorder.R
import com.example.mytableorder.fragment.DetailsFragment

class HomeFragment : Fragment() {
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    val view = inflater.inflate(R.layout.fragment_home, container, false)


    val recyclerHorizon = view.findViewById<RecyclerView>(R.id.recyclerHorizon)
    val recyclerVertical = view.findViewById<RecyclerView>(R.id.recyclerVertical)

    // 수평 RecyclerView 설정
    val list = arrayListOf("Test 1", "Test 2", "Test 3", "Test 4")
    val manager02 = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    // 클릭 리스너를 포함하여 어댑터를 인스턴스화합니다.
    val adapter02 = ListAdapterHorizontal(list) { position ->
      // Bundle 객체를 생성하고 position을 추가합니다.
      val bundle = Bundle().apply {
        putInt("position", position)
      }
      // findNavController()를 사용하여 DetailsFragment로 이동하고 Bundle을 전달합니다.
      findNavController().navigate(R.id.action_homeFragment_to_detailsFragment, bundle)
    }
    recyclerHorizon.layoutManager = manager02
    recyclerHorizon.adapter = adapter02



    // 수직 RecyclerView 설정
    val manager01 = LinearLayoutManager(requireContext())
    // 4. 어댑터 인스턴스 생성 시 클릭 리스너 구현
    val adapter01 = ListAdapterVertical(object : OnItemClickListener {
      override fun onItemClick(position: Int) {
        val bundle = Bundle().apply {
          putInt("position", position)
        }
        // `action_homeFragment_to_detailsFragment`는 `nav_graph.xml`에서 정의한 액션 ID입니다.
        findNavController().navigate(R.id.action_homeFragment_to_detailsFragment, bundle)
      }
    })

    recyclerVertical.adapter = adapter01
    recyclerVertical.layoutManager = manager01







    return view
  }

}
