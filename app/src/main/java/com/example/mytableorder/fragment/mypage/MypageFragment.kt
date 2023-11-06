package com.example.mytableorder.fragment.mypage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.mytableorder.R
import com.example.mytableorder.databinding.FragmentMypageBinding
import com.example.mytableorder.utils.CheckInternet
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.remote.ConnectivityMonitor
import com.google.firebase.ktx.Firebase
import android.content.SharedPreferences
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

class MypageFragment : Fragment() {


    private lateinit var binding : FragmentMypageBinding
    private lateinit var auth: FirebaseAuth

   /* private lateinit var viewModel : MypageViewModel
    private lateinit var viewModelFactory: MypageViewModelFactory*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMypageBinding.inflate(inflater, container, false)
        val view = binding.root
       /* binding.lifecycleOwner = this
        viewModelFactory = MypageViewModelFactory(AuthRepository())
        viewModel = ViewModelProvider(this, viewModelFactory).get(MypageViewModel::class.java)
        binding.viewmodel = viewModel*/
        if (CheckInternet.isConnected(requireActivity())) {

            binding.connectedLayout.visibility = View.VISIBLE
            binding.disconnectedLayout.visibility = View.GONE


        }else{
            binding.connectedLayout.visibility = View.GONE
            binding.disconnectedLayout.visibility = View.VISIBLE


        }
        return view
    }

    private val backPressedDispatcher = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(backPressedDispatcher)
        setButton()
//        setViewModel()
    }

   /* fun setViewModel() {
        viewModel.getLogoutResponse.observe(viewLifecycleOwner) {
            if(it.success) {
                Toast.makeText(activity, it.response, Toast.LENGTH_SHORT).show()
                MyApplication.prefs.clear()
                val intent = Intent(binding.root.context, SignInActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                binding.mypageProgressBar.hide()
                binding.root.context?.startActivity(intent)
            }
        }
    }*/

    private fun setButton() {
        binding.apply {
            btnMypageMyInfo.setOnClickListener {
                findNavController().navigate(R.id.action_mypageFragment_to_myInfoFragment)

            }

            btnMypageChangePassword.setOnClickListener {
                findNavController().navigate(R.id.action_mypageFragment_to_resetPasswordFragment)
            }

         /*   btnMypageDeleteMember.setOnClickListener {
                val intent = Intent(it.context, DeleteMemberActivity::class.java)
                it.context.startActivity(intent)
            }*/

           /* btnMypageReservationHistory.setOnClickListener {
                val intent = Intent(it.context, ReservationHistoryActivity::class.java)
                it.context.startActivity(intent)
            }

            btnMypageWriteHistory.setOnClickListener {
                val intent = Intent(it.context, WriteHistoryActivity::class.java)
                it.context.startActivity(intent)
            }

            btnMypageCommentHistory.setOnClickListener {
                val intent = Intent(it.context, CommentHistoryActivity::class.java)
                it.context.startActivity(intent)
            }

            btnMypageSetting.setOnClickListener {
                val intent = Intent(it.context, SettingActivity::class.java)
                it.context.startActivity(intent)
            }*/

            btnMypageLogout.setOnClickListener {
                showLogoutDialog()
            }
        }
    }

    fun showLogoutDialog(){
        MaterialAlertDialogBuilder(binding.root.context)
            .setTitle("로그아웃")
            .setMessage("로그아웃하시겠습니까?")
            .setPositiveButton("확인") { _, _ ->
                if(CheckInternet.isConnected(requireActivity())){
                    auth = Firebase.auth
                    auth.signOut()
                    val navController = findNavController()
                    val sharedPreference = requireContext().getSharedPreferences("userType",
                        AppCompatActivity.MODE_PRIVATE
                    )
                    val editor = sharedPreference.edit()

                    editor.remove("user_type")
                    // 전체 삭제는 editor.clear()
                    editor.commit()
                    Toast.makeText(requireContext(), "로그아웃 완료", Toast.LENGTH_SHORT).show()
                    navController.navigate(R.id.splashFragment)
                    binding.mypageProgressBar.visibility = View.VISIBLE
                }
                else
                    Toast.makeText(activity, "네트워크 연결을 확인해 주세요.", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("취소") { _, _ -> // 취소시 처리 로직
            }
            .show()
    }
}