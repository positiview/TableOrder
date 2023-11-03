package com.example.mytableorder.fragment.board

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.mytableorder.R


class BoardDetailsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_boarddetails, container, false)
        val btnModify = view.findViewById<Button>(R.id.btnModify)
        val btnDelete = view.findViewById<Button>(R.id.btnDelete)
        val btnBack = view.findViewById<Button>(R.id.btnBack)


        btnModify.setOnClickListener {
            // 버튼을 클릭하면 "fragment_write" 프래그먼트로 이동
            findNavController().navigate(R.id.action_writeFragment_to_BoardDetailsFragment)
        }

        btnDelete.setOnClickListener {
            // 버튼을 클릭하면 "fragment_write" 프래그먼트로 이동
            findNavController().navigate(R.id.action_writeFragment_to_BoardFragment)
        }

        btnBack.setOnClickListener {
            // 버튼을 클릭하면 "fragment_write" 프래그먼트로 이동
            findNavController().navigate(R.id.action_writeFragment_to_BoardFragment)
        }

        return view

    }

}
