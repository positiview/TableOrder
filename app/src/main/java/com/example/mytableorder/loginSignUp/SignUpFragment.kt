package com.example.mytableorder.loginSignUp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mytableorder.Db.db
import com.example.mytableorder.model.User
import com.example.mytableorder.MainActivity
import com.example.mytableorder.R
import com.example.mytableorder.databinding.FragmentSignUpBinding
import com.example.mytableorder.loginSignUp.viewmodel.SignUpViewModel

import com.example.mytableorder.utils.CheckInternet
import com.example.mytableorder.utils.Resource
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var databaseReference: DatabaseReference
    private  val viewModel : SignUpViewModel by viewModels()
    lateinit var auth :FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        val view = binding.root
        (activity as MainActivity).supportActionBar?.hide()

        /*var firebaseDatabase = FirebaseDatabase.getInstance()
        var databaseReference = firebaseDatabase.getReference("Users")*/

        auth = Firebase.auth

        binding.loginTv.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }
        binding.btnRegister.setOnClickListener {
            val name = binding.nameInputLayout.editText?.text.toString().trim()
            val email = binding.emailInputLayout.editText?.text.toString().trim()
            val phone = binding.phoneNumberInputLayout.editText?.text.toString().trim()
            val password = binding.passInputLayout.editText?.text.toString().trim()
            val confirmPassword = binding.confPassInputLayout.editText?.text.toString().trim()


            when {
                /*!(binding.btnResturant.isChecked || binding.btnOrganization.isChecked || binding.btnAdmin.isChecked) -> {
                    Toast.makeText(requireContext(), "Please select user type", Toast.LENGTH_SHORT).show()
                }*/

                name.isEmpty() -> {
                    binding.nameInputLayout.error = "Name is required"
                    binding.nameInputLayout.isErrorEnabled = true
                }
                email.isEmpty() -> {
                    binding.emailInputLayout.error = "Email is required"
                    binding.emailInputLayout.isErrorEnabled = true
                }
                phone.isEmpty() -> {
                    binding.phoneNumberInputLayout.error = "Phone number is required"
                    binding.phoneNumberInputLayout.isErrorEnabled = true
                }
                password.isEmpty() -> {
                    binding.passInputLayout.error = "Password is required"
                    binding.passInputLayout.isErrorEnabled = true
                }
                confirmPassword.isEmpty() -> {
                    binding.confPassInputLayout.error = "Confirm password is required"
                    binding.confPassInputLayout.isErrorEnabled = true
                }
                password != confirmPassword -> {
                    binding.passInputLayout.error = "Password does not match"
                    binding.confPassInputLayout.error = "Password does not match"
                    binding.passInputLayout.isErrorEnabled = true
                }
                else -> {
                    binding.nameInputLayout.isErrorEnabled = false
                    binding.emailInputLayout.isErrorEnabled = false
                    binding.phoneNumberInputLayout.isErrorEnabled = false
                    binding.passInputLayout.isErrorEnabled = false
                    binding.confPassInputLayout.isErrorEnabled = false
                    binding.btnRegister.isEnabled = false


                    /*val selectedItemId = binding.radioGroup.checkedRadioButtonId
                    val selectedItem = binding.radioGroup.findViewById<RadioButton>(selectedItemId)*/
                    val userType = "user"
                    val level = "병아리"

                    if (CheckInternet.isConnected(requireContext())) {
                        //Toast.makeText(activity, "Internet is available", Toast.LENGTH_SHORT).show()
                        binding.progressCircular.isVisible = true

                        Log.d("$$", "인터넷 연결 확인 in SingUpFragment")

                        val user = User(
                            email,
                            name,
                            phone,
                            userType,
                            level
                        )

                        /*viewModel.register(email, password, user)
                        viewModel.registerRequest.observe(viewLifecycleOwner){
                            when(it){
                                is Resource.Loading -> {
                                    binding.progressCircular.isVisible = true
                                }
                                is Resource.Error -> {
                                    Toast.makeText(requireContext(), it.string, Toast.LENGTH_SHORT).show()
                                }
                                is Resource.Success -> {
                                    Toast.makeText(requireContext(), it.data, Toast.LENGTH_SHORT).show()
                                    requireActivity().onBackPressedDispatcher.onBackPressed()
                                }
                            }
                        }*/
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnSuccessListener {
                                auth.currentUser?.sendEmailVerification()
                                    ?.addOnSuccessListener {
                                        db.collection("users")
                                            .document(auth.uid.toString())
                                            .set(user)
                                            .addOnSuccessListener {
                                                Log.d("$$", "회원 가인 완료")
                                                val popupText =
                                                    Snackbar.make(view,"Account Created Successfully\n Check your email for verification Link", Snackbar.LENGTH_LONG)
                                                popupText.show()
                                                requireActivity().onBackPressed()
                                            }
                                    }
                            }
                            .addOnFailureListener {
                                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                                    .show()
                                Log.d("$$","회원가입 실패")
                                binding.progressCircular.isVisible = false
                                binding.btnRegister.isEnabled = true
                            }


                    }
                }
            }
        }

        return view
    }

}