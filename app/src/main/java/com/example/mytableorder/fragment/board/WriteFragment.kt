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

class WriteFragment : Fragment() {
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    val view = inflater.inflate(R.layout.fragment_write, container, false)
    val btnReg = view.findViewById<Button>(R.id.btnReg)
    val btnRegBack = view.findViewById<Button>(R.id.btnRegBack)


    btnReg.setOnClickListener {
      // 버튼을 클릭하면 "fragment_write" 프래그먼트로 이동
      findNavController().navigate(R.id.action_writeFragment_to_BoardDetailsFragment)
    }

    btnRegBack.setOnClickListener {
      // 버튼을 클릭하면 "fragment_write" 프래그먼트로 이동
      findNavController().navigate(R.id.action_writeFragment_to_BoardFragment)
    }

    return view

  }

}
