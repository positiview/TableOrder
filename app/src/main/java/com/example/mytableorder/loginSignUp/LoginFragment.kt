package com.example.mytableorder.loginSignUp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mytableorder.Db.db
import com.example.mytableorder.MainActivity
import com.example.mytableorder.R
import com.example.mytableorder.databinding.FragmentLoginBinding
import com.example.mytableorder.loginSignUp.viewmodel.LoginViewModel
import com.example.mytableorder.model.User
import com.example.mytableorder.repository.AuthRepository
import com.example.mytableorder.repository.AuthRepositoryImpl
import com.example.mytableorder.utils.CheckInternet
import com.example.mytableorder.utils.Resource
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private val TAG = "GoogleActivity"
    private val RC_SIGN_IN = 100
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private val authRepository: AuthRepository = AuthRepositoryImpl()
    private val viewModel: LoginViewModel by viewModels { LoginViewModel.AuthViewModelFactory(authRepository) }



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

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.your_web_client_id))
            .requestEmail()
            .build()
        val root = inflater.inflate(R.layout.fragment_login, container, false)

        mGoogleSignInClient = GoogleSignIn.getClient(root.context, gso) // this는 뭘로?

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

                        viewModel.login(email, password)
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

                                    if (result == "user"){
                                        moveFragment()
                                        Toast.makeText(requireContext(), "로그인에 성공했습니다. ", Toast.LENGTH_SHORT).show()
                                    }else if(result == "admin"){
                                        moveAdmin()

                                        Toast.makeText(requireContext(), "관리자 로그인에 성공햇씁니다.", Toast.LENGTH_SHORT).show()
                                    }else{
                                        Toast.makeText(requireContext(), "You are not registered yet or an error occurred", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                        /*auth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener {
                                val currentUser = auth.currentUser
                                if(currentUser!!.isEmailVerified){
                                    db.collection("users")
                                        .document(currentUser.uid)
                                        .get()
                                        .addOnSuccessListener {
                                            val userType = it.get("user_type") as String

                                            when (userType) {
                                                "admin" -> {
                                                    val sharedPref = requireActivity().getSharedPreferences("userType", Context.MODE_PRIVATE)
                                                    val editor = sharedPref.edit()
                                                    editor.putString("user_type", userType)
                                                    editor.commit()
                                                    moveAdmin()
                                                }

                                                else -> {
                                                    moveFragment()
                                                }
                                            }
                                        }

                                }else{
                                    Resource.Error("Email not verified")
                                }
                            }.addOnFailureListener {
                                Resource.Error(it.message.toString())
                            }*/

                        // 아래는 realtime database를 이용한 회원로그인
                        /*auth.signInWithEmailAndPassword(email, password)
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
                                                        val userType = user?.user_type

                                                        if (user != null) {

                                                            binding.progressCircular.isVisible =
                                                                false
                                                            binding.btnLogin.isEnabled = true
                                                            binding.btnLogin.text = "Login"
                                                            if(userType == "admin"){
                                                                moveAdmin()
                                                            }else{
                                                                moveFragment()
                                                            }
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
                            }*/

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
        binding.googleLogin.setOnClickListener {

            val signInIntent = mGoogleSignInClient?.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)

        }
        return view
    }
    private fun moveFragment() {
        Toast.makeText(
            requireContext(),
            "로그인 성공!!",
            Toast.LENGTH_SHORT
        ).show()
        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
    }
    private fun moveAdmin() {
        Toast.makeText(
            requireContext(),
            "관리자 로그인 성공!!",
            Toast.LENGTH_SHORT
        ).show()
        findNavController().navigate(R.id.action_loginFragment_to_adminHomeFragment)
    }

    /*   override fun onStart() {
           super.onStart()
           val currentUser = auth.currentUser
           updateUI(currentUser)
       }*/

    private fun updateUI(currentUser: FirebaseUser?) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        // 구글 로그인
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
//                var result = Auth.GoogleSignInApi.getSignInResultFromIntent(data!!)
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!

                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                // ...
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }




    /*private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Snackbar.make(
                            findViewById(com.example.mytableorder.R.id.layout_main),
                            "Authentication Successed.",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        val user: FirebaseUser = mAuth.getCurrentUser()
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Snackbar.make(
                            findViewById(com.example.mytableorder.R.id.layout_main),
                            "Authentication Failed.",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        updateUI(null)
                    }
                })
    }*/


}

