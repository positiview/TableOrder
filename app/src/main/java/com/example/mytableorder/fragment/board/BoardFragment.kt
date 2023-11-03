package com.example.mytableorder.fragment.board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytableorder.R


class BoardFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_board, container, false)
        val boardList = view.findViewById<RecyclerView>(R.id.BoardList)

        val button = view.findViewById<Button>(R.id.button)

        // 수직 RecyclerView 설정
        val manager01 = LinearLayoutManager(requireContext())
        // 4. 어댑터 인스턴스 생성 시 클릭 리스너 구현
        val adapter01 = ListAdapterBoard(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val bundle = Bundle().apply {
                    putInt("position", position)
                }
                // `action_homeFragment_to_detailsFragment`는 `nav_graph.xml`에서 정의한 액션 ID입니다.
                findNavController().navigate(R.id.action_boardFragment_to_boardDetailsFragment, bundle)
            }
        })

        boardList.adapter = adapter01
        boardList.layoutManager = manager01

        button.setOnClickListener {
            // 버튼을 클릭하면 "fragment_write" 프래그먼트로 이동
            findNavController().navigate(R.id.action_boardFragment_to_WriteFragment)
        }

        return view

    }

}
