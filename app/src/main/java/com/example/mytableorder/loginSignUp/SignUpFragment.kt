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
import com.example.mytableorder.model.User
import com.example.mytableorder.MainActivity
import com.example.mytableorder.R
import com.example.mytableorder.databinding.FragmentSignUpBinding

import com.example.mytableorder.utils.CheckInternet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
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
        var db: FirebaseFirestore = FirebaseFirestore.getInstance()
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
                !(binding.btnResturant.isChecked || binding.btnOrganization.isChecked || binding.btnAdmin.isChecked) -> {
                    Toast.makeText(requireContext(), "Please select user type", Toast.LENGTH_SHORT).show()
                }

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


                    val selectedItemId = binding.radioGroup.checkedRadioButtonId
                    val selectedItem = binding.radioGroup.findViewById<RadioButton>(selectedItemId)
                    val userType = selectedItem.text.toString()

                    if (CheckInternet.isConnected(requireContext())) {
                        //Toast.makeText(activity, "Internet is available", Toast.LENGTH_SHORT).show()
                        binding.progressCircular.isVisible = true
                       /* val memberModel = MemberModel(
                            email,
                            name,
                            phone,
                            userType,
                            password
                        )*/

//                        RetrofitController.register(memberModel)


                      /*  val user = User(
                            email,
                            name,
                            phone,
                            userType
                        )*/

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
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    val currentUser = auth.currentUser
                                    binding.progressCircular.isVisible = false
                                    currentUser?.sendEmailVerification()
                                        ?.addOnCompleteListener {
                                        Toast.makeText(
                                            requireContext(),
                                            "인증링크를 이메일로 보냈습니다. 확인해주세요.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    val userId = currentUser?.uid
                                    val user =
                                        userId?.let { id -> User(email, name, phone, id) }
                                    // 위 코드는 userId가 null이 아닐 때만 User 객체를 생성하고 해당 객체를 user 변수에 할당합니다.

                                    if (userId != null) {
                                        /*databaseReference.child(userId).setValue(user)*/
                                        if (user != null) {
                                            db.collection("member").add(user)
                                                .addOnSuccessListener { r -> Log.d("$$", "successful signUp with ID :${r.id}") }
                                                .addOnFailureListener{ e -> Log.e("fail","error",e)
                                                }
                                        }
                                        Toast.makeText(
                                            requireContext(),
                                            "계정이 성공적으로 생성되었습니다.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    requireActivity().onBackPressed()
                                }
                            }
                            .addOnFailureListener {
                                binding.progressCircular.isVisible = false
                                binding.btnRegister.isEnabled = true
                                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                    }
                }
            }
        }

        return view
    }

}