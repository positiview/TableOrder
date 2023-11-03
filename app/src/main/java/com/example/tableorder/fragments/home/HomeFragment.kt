package com.example.tableorder.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.tableorder.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.fragment_home, container, false)

    val recyclerVertical = view.findViewById<RecyclerView>(R.id.recyclerVertical)
    val recyclerHorizon = view.findViewById<RecyclerView>(R.id.recyclerHorizon)

    val manager01 = LinearLayoutManager(requireContext())
    val adapter01 = ListAdapterVertical()

    recyclerVertical.adapter = adapter01
    recyclerVertical.layoutManager = manager01

    val list = arrayListOf("Test 1", "Test 2", "Test 3", "Test 4")
    val manager02 = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    val adapter02 = ListAdapterHorizontal(list)

    recyclerHorizon.adapter = adapter02
    recyclerHorizon.layoutManager = manager02

    return view
  }
}
