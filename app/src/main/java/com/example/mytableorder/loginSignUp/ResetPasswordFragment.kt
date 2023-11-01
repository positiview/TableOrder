package com.example.mytableorder.loginSignUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.example.mytableorder.databinding.FragmentResetPasswordBinding

class ResetPasswordFragment : DialogFragment() {
    private lateinit var binding: FragmentResetPasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        val view = binding.root
//        auth = FirebaseAuth.getInstance()

        binding.dialogConfirm.setOnClickListener {
            val email = binding.userEmil.editText?.text.toString().trim()

            if (email.isEmpty()) {
                binding.userEmil.error = "Please enter your email"
                binding.userEmil.requestFocus()
                return@setOnClickListener
            } else {
                binding.progressBar6.isVisible = true
                /*auth.sendPasswordResetEmail(email)
                    .addOnCanceledListener {
                        binding.progressBar6.isVisible = false
                        Toast.makeText(requireContext(), "An error occurred", Toast.LENGTH_SHORT)
                            .show()
                    }
                    .addOnCompleteListener {
                        if (it.isSuccessful && CheckInternet.isConnected(requireContext())) {
                            binding.progressBar6.isVisible = false
                            Toast.makeText(
                                requireContext(), "Reset link has been sent to your email \n" +
                                        " Check your mailbox", Toast.LENGTH_SHORT
                            ).show()
                            dismiss()
                        } else {
                            binding.progressBar6.isVisible = false
                            Toast.makeText(
                                requireContext(),
                                "An error occurred",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .addOnFailureListener {
                        binding.progressBar6.isVisible = false
                        Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                    }*/
            }
        }

        return view
    }
}