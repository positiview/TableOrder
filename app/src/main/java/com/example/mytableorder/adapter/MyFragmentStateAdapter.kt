package com.example.mytableorder.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

import com.example.mytableorder.fragment.home.HomeFragment



// Adapter는 데이터를 RecyclerView에 주입하는 객체
class MyFragmentStateAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
  val fragments: List<Fragment>

  init {
    fragments = listOf(HomeFragment(), HomeFragment(), HomeFragment(), HomeFragment(), HomeFragment())
  }

  override fun getItemCount(): Int = fragments.size
  override fun createFragment(position: Int): Fragment = fragments[position]
}