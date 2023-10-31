package com.example.tableorder.fragments.about

import MyAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tableorder.R
import com.example.tableorder.databinding.FragmentAboutUsBinding
import com.example.tableorder.model.AboutUsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AboutUsFragment : DialogFragment() {
    private lateinit var binding: FragmentAboutUsBinding
    private lateinit var viewModel: AboutUsViewModel
    private lateinit var adapter: MyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_about_us, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

        // RecyclerView에 어댑터를 설정
        adapter = MyAdapter(getRandomData()) // getRandomData() 함수는 임의의 데이터를 생성하는 함수라고 가정합니다
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        return view
    }

    // 임의의 데이터 생성 함수
    private fun getRandomData(): List<String> {
        // 원하는대로 데이터를 생성하고 반환합니다.
        return listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
    }
}