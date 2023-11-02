package com.example.mytableorder.loginSignUp

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mytableorder.R
import com.example.mytableorder.databinding.FragmentLoginBinding
import com.example.mytableorder.model.User
import com.example.mytableorder.utils.CheckInternet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth


//    private val viewModel : LoginViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        //disable dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        (activity as AppCompatActivity).supportActionBar?.hide()


        auth = Firebase.auth

        binding.registerTv.setOnClickListener {
            Log.d("$$", "회원가입 실행")
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
        binding.forgotPasswordTv.setOnClickListener {
            Log.d("$$", "비밀번호수정 실행")
            findNavController().navigate(R.id.action_loginFragment_to_resetPasswordFragment)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.emailTinputLayout.editText?.text.toString()
            val password = binding.passwordInputLayout.editText?.text.toString()

            when {
                email.isEmpty() -> {
                    binding.emailTinputLayout.error = "Email is required"
                    binding.emailTinputLayout.isErrorEnabled = true
                    return@setOnClickListener
                }
                password.isEmpty() -> {
                    binding.passwordInputLayout.error = "Password is required"
                    binding.passwordInputLayout.isErrorEnabled = true
                    return@setOnClickListener
                }

                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.emailTinputLayout.error = "Invalid email format"
                    binding.emailTinputLayout.isErrorEnabled = true
                    return@setOnClickListener
                }
                else -> {
                    binding.emailTinputLayout.isErrorEnabled = false
                    binding.passwordInputLayout.isErrorEnabled = false
                    binding.btnLogin.isEnabled = false

                    if (CheckInternet.isConnected(requireActivity())) {
                        Log.d("$$", "인터넷 연결됨")
                        //Toast.makeText(activity, "Internet is available", Toast.LENGTH_SHORT).show()
                        binding.emailTinputLayout.isEnabled = false
                        binding.passwordInputLayout.isEnabled = false
                        binding.btnLogin.isEnabled = false
                        binding.btnLogin.text = "Loading..."
                       /* viewModel.login(email, password)
                        viewModel.loginRequest.observe(viewLifecycleOwner){
                            when(it){
                                is Resource.Loading -> {
                                    binding.progressCircular.isVisible = true
                                }
                                is Resource.Error -> {
                                    binding.progressCircular.isVisible = false
                                    binding.emailTinputLayout.isEnabled = true
                                    binding.passwordInputLayout.isEnabled = true
                                    Toast.makeText(requireContext(), it.string, Toast.LENGTH_SHORT).show()
                                }
                                is Resource.Success -> {
                                    binding.progressCircular.isVisible = false
                                    val result = it.data
                                    //UserType(result)
                                    val sharedPref = requireActivity().getSharedPreferences("userType", Context.MODE_PRIVATE)
                                    val editor = sharedPref.edit()
                                    editor.putString("user_type", result)
                                    editor.apply()

                                    if (result == "Organization"){
                                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                                        Toast.makeText(requireContext(), "Logged in as Organization", Toast.LENGTH_SHORT).show()
                                    }else if (result == "Restaurant"){
                                        //Navigate to Donors View
//                                        findNavController().navigate(R.id.action_loginFragment_to_donorsHomeFragment)
                                        Toast.makeText(requireContext(), "Logged in as Restaurant", Toast.LENGTH_SHORT).show()
                                    }else if(result == "Admin"){
                                        //Navigate to Admin
//                                        findNavController().navigate(R.id.action_loginFragment_to_adminHomeFragment)
                                        Toast.makeText(requireContext(), "Logged in as Admin", Toast.LENGTH_SHORT).show()
                                    }else{
                                        Toast.makeText(requireContext(), "You are not registered yet or an error occurred", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }*/

                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(requireActivity()) { task ->
                                val currentUser = auth.currentUser
                                Log.d("$$", "로그인 실행")
                                if (task.isSuccessful) {
                                       if (currentUser!!.isEmailVerified) {
                                           var databaseReference =
                                               FirebaseDatabase.getInstance().getReference("User")
                                        val firebaseUser: FirebaseUser? =
                                            FirebaseAuth.getInstance().currentUser
                                        val uid: String? = firebaseUser?.uid
                                        uid?.let { id ->
                                            databaseReference.child(id)
                                                .addValueEventListener(object : ValueEventListener {
                                                    override fun onDataChange(snapshot: DataSnapshot) {
                                                        val user =
                                                            snapshot.getValue(User::class.java)
                                                        if (user != null) {
                                                            binding.progressCircular.isVisible =
                                                                false
                                                            binding.btnLogin.isEnabled = true
                                                            binding.btnLogin.text = "Login"
                                                            Toast.makeText(
                                                                requireContext(),
                                                                "Login in successful",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                                                        }
                                                    }

                                                    override fun onCancelled(error: DatabaseError) {
                                                        binding.progressCircular.isVisible = false
                                                        binding.btnLogin.isEnabled = true
                                                        binding.btnLogin.text = "Login"
                                                        binding.emailTinputLayout.editText?.text?.clear()
                                                        binding.passwordInputLayout.editText?.text?.clear()
                                                        binding.emailTinputLayout.isEnabled = true
                                                        binding.passwordInputLayout.isEnabled = true
                                                        Toast.makeText(
                                                            activity,
                                                            "Error: ${error.message}",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                })
                                        }

                                    } else {
                                        binding.progressCircular.isVisible = false
                                        Toast.makeText(
                                            requireContext(),
                                            "Please verify your email",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        binding.emailTinputLayout.editText?.text?.clear()
                                        binding.passwordInputLayout.editText?.text?.clear()
                                        binding.emailTinputLayout.isEnabled = true
                                        binding.passwordInputLayout.isEnabled = true
                                    }
                                } else {
                                    binding.progressCircular.isVisible = false
                                    binding.btnLogin.isEnabled = true
                                    binding.emailTinputLayout.editText?.text?.clear()
                                    binding.passwordInputLayout.editText?.text?.clear()
                                    binding.emailTinputLayout.isEnabled = true
                                    binding.passwordInputLayout.isEnabled = true
                                    binding.btnLogin.isEnabled = true
                                    binding.btnLogin.text = "Login"
                                    Toast.makeText(
                                        activity,
                                        "Error: ${task.exception?.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                    } else {
                        Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT)
                            .show()
                        binding.progressCircular.isVisible = false
                        binding.btnLogin.isEnabled = true
                        binding.btnLogin.text = "Login"
                    }

                }
            }
        }
        return view
    }

}