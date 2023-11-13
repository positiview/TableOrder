package com.example.mytableorder.loginSignUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.example.mytableorder.R
import com.example.mytableorder.databinding.FragmentResetPasswordBinding
import com.example.mytableorder.utils.CheckInternet
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordFragment : DialogFragment() {
    private lateinit var binding: FragmentResetPasswordBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        val view = binding.root
        auth = FirebaseAuth.getInstance()
        val str = arguments?.getString("pwTitle")
        val userEmail = arguments?.getString("putText")
        if(str != null){

            val textView: MaterialTextView = binding.resetTextView
            textView.text = str
            val userEmailTextView: TextInputEditText = binding.emailEditText
            userEmailTextView.setText(userEmail)
        }

        binding.dialogConfirm.setOnClickListener {
            val email = binding.userEmil.editText?.text.toString().trim()

            if (email.isEmpty()) {
                binding.userEmil.error = "이메일을 입력해주세요"
                binding.userEmil.requestFocus()
                return@setOnClickListener
            } else {
                binding.progressBar6.isVisible = true
                auth.sendPasswordResetEmail(email)
                    .addOnCanceledListener {
                        binding.progressBar6.isVisible = false
                        Toast.makeText(requireContext(), "에러 발생", Toast.LENGTH_SHORT)
                            .show()
                    }
                    .addOnCompleteListener {
                        if (it.isSuccessful && CheckInternet.isConnected(requireContext())) {
                            binding.progressBar6.isVisible = false
                            Toast.makeText(
                                requireContext(), "입력한 이메일로 리셋링크를 보냈습니다 \n" +
                                        "이메일을 확인해주세요", Toast.LENGTH_SHORT
                            ).show()
                            dismiss()
                        } else {
                            binding.progressBar6.isVisible = false
                            Toast.makeText(
                                requireContext(),
                                "에러 발생",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .addOnFailureListener {
                        binding.progressBar6.isVisible = false
                        Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        return view
    }
}