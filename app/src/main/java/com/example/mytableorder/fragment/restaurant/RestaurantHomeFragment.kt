package com.example.mytableorder.fragment.restaurant

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
import com.example.mytableorder.R
import com.example.mytableorder.databinding.FragmentRestaurantHomeBinding
import com.google.firebase.auth.FirebaseAuth


class RestaurantHomeFragment : Fragment(),MenuProvider {
    private lateinit var binding: FragmentRestaurantHomeBinding

    lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRestaurantHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        (activity as AppCompatActivity).supportActionBar?.show()
        //(activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
       /* binding.cardLogout.setOnClickListener {
            auth.signOut()

        }*/
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