package com.example.mytableorder.fragment.home.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.mytableorder.R

class ReviewListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_review, container, false)

        // 여기에서 RecyclerView 및 어댑터를 초기화하고 리뷰 목록을 표시하는 코드를 추가합니다.
        val review: RecyclerView = view.findViewById(R.id.review)

        // 리뷰 목록을 표시하는 어댑터 설정 등의 코드를 추가하세요.
        val button = view.findViewById<Button>(R.id.button)
        button.setOnClickListener {
            findNavController().navigate(R.id.action_ReviewListFragment_to_reviewWriteFragment)
        }

        return view


    }

}
