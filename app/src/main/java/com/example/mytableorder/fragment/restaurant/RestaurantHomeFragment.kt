package com.example.mytableorder.fragment.restaurant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mytableorder.R
import com.example.mytableorder.databinding.FragmentRestaurantHomeBinding
import com.google.firebase.auth.FirebaseAuth


class RestaurantHomeFragment : Fragment() {
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
        /*binding.cardLogout.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_donorsHomeFragment_to_loginFragment)
        }
        binding.cardDonate.setOnClickListener {
            findNavController().navigate(R.id.action_donorsHomeFragment_to_donateFragment)
        }
        binding.cardHistory.setOnClickListener {
            findNavController().navigate(R.id.action_donorsHomeFragment_to_historyFragment)
        }
        binding.cardAboutus.setOnClickListener {
            findNavController().navigate(R.id.action_donorsHomeFragment_to_aboutUsFragment)
        }
        binding.cardContact.setOnClickListener {
            findNavController().navigate(R.id.action_donorsHomeFragment_to_contactUsFragment)
        }*/





        return view
    }

}