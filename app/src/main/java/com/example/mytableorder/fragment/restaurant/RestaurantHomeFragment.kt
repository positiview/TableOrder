package com.example.mytableorder.fragment.restaurant

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.MenuProvider
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mytableorder.MainActivity
import com.example.mytableorder.R
import com.example.mytableorder.databinding.FragmentRestaurantHomeBinding
import com.example.mytableorder.fragment.mypage.MypageFragment
import com.example.mytableorder.utils.CheckInternet
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RestaurantHomeFragment : Fragment(),MenuProvider {
    private lateinit var binding: FragmentRestaurantHomeBinding

    var auth: FirebaseAuth = Firebase.auth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRestaurantHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        (activity as AppCompatActivity).supportActionBar?.show()
        //(activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        binding.cardLogout.setOnClickListener {

            showLogoutDialog()
        }
        binding.cardBookedList.setOnClickListener {
            findNavController().navigate(R.id.action_restaurantHomeFragment_to_BookedUserListFragment)
        }
        /*binding.cardHistory.setOnClickListener {
            findNavController().navigate(R.id.action_donorsHomeFragment_to_historyFragment)
        }*/
       /* binding.cardAboutus.setOnClickListener {
            findNavController().navigate(R.id.action_donorsHomeFragment_to_aboutUsFragment)
        }*/
      /*  binding.cardContact.setOnClickListener {
            findNavController().navigate(R.id.action_donorsHomeFragment_to_contactUsFragment)
        }*/





        return view
    }

    private fun showLogoutDialog() {
        MaterialAlertDialogBuilder(binding.root.context)
            .setTitle("로그아웃")
            .setMessage("로그아웃하시겠습니까?")
            .setPositiveButton("확인") { _, _ ->
                if(CheckInternet.isConnected(requireActivity())){

                    auth.signOut()

                    val sharedPreference = requireContext().getSharedPreferences("userType",
                        AppCompatActivity.MODE_PRIVATE
                    )
                    val editor = sharedPreference.edit()

                    editor.remove("user_type")
                    // 전체 삭제는 editor.clear()
                    editor.commit()
                    Toast.makeText(requireContext(), "로그아웃 완료", Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    requireActivity().finish()

                }
                else
                    Toast.makeText(activity, "네트워크 연결을 확인해 주세요.", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("취소") { _, _ -> // 취소시 처리 로직
            }
            .show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.admin_menu, menu)
        menu.findItem(R.id.action_logout).setOnMenuItemClickListener {
            findNavController().navigate(R.id.homeFragment)
            Toast.makeText(requireContext(),"예약관리 모드를 나갑니다.", Toast.LENGTH_SHORT).show()
            true
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId){
            R.id.action_logout -> {
                Toast.makeText(requireContext(),"예약관리 모드를 나갑니다.", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.homeFragment)
            }
        }
        if (menuItem.itemId == android.R.id.home) {
            // Drawer를 열도록 설정
            val drawerLayout = activity?.findViewById<DrawerLayout>(R.id.drawer_layout)
            drawerLayout?.openDrawer(GravityCompat.START)
            return true
        }
        return true
    }
}