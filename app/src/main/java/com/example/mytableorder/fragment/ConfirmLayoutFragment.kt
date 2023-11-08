package com.example.mytableorder.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
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
            /*val sharedPreference = requireActivity().getSharedPreferences("userType", AppCompatActivity.MODE_PRIVATE)
            val editor = sharedPreference.edit()

            editor.remove("user_type")
            // 전체 삭제는 editor.clear()
            editor.commit()
            Toast.makeText(requireContext(), "로그아웃 완료", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.splashFragment)*/


        }
        return view
    }
}
