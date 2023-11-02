package com.example.mytableorder.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.mytableorder.R
import com.example.mytableorder.databinding.FragmentConfirmLayoutBinding
import com.google.firebase.auth.FirebaseAuth

class ConfirmLayoutFragment : DialogFragment(){
    private lateinit var binding: FragmentConfirmLayoutBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentConfirmLayoutBinding.inflate(inflater, container,false)
        val view = binding.root

        binding.dialogConfirm.setOnClickListener{
            auth.signOut()


        }
        return view
    }
}
