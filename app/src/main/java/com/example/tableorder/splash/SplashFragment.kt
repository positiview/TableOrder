package com.example.tableorder.splash

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tableorder.databinding.FragmentSplashBinding

import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        val view = binding.root
        //(activity as AppCompatActivity).supportActionBar?.hide()
        /*activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN*/

//        val user = auth.currentUser
        val user = null

        Handler(Looper.getMainLooper()).postDelayed({
            if (user != null) {
                /*val action =
                    SplashFragmentDirections.actionSplashFragmentToHomeFragment()
                findNavController().navigate(action)*/
                //findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                val sharedPrefs = requireActivity().getSharedPreferences("userType", Context.MODE_PRIVATE)
                //SharedPreferences는 앱의 데이터를 영속적으로 저장하기 위한 클래스입니다.
                //
                // DBMS 방식의 데이터 영속화는 테이블 구조로 저장하지만,
                // SharedPreferences는 데이터를 간단하게 키-값(key-value) 성격으로 저장합니다.
                // SharedPreferences로 저장하는 데이터 역시 결국은 파일(XML)로 저장되지만,
                // 개발자가 직접 파일을 읽고 쓰는 코드를 작성하지 않고 SharedPreferences 객체를 이용해서 간단하게 이용할 수 있습니다.
                val userType = sharedPrefs.getString("user_type", null)
                if (userType == "Admin"){
//                    val action = SplashFragmentDirections.actionSplashFragmentToAdminHomeFragment()
//                    findNavController().navigate(action)

                }else if(userType == "Organization"){
                    val action = SplashFragmentDirections.actionSplashFragmentToHomeFragment()
                    findNavController().navigate(action)

                }else if(userType == "Restaurant"){
//                    val action = SplashFragmentDirections.actionSplashFragmentToDonorsHomeFragment()
//                    findNavController().navigate(action)

                }else{
                    val action =
                        SplashFragmentDirections.actionSplashFragmentToLoginFragment()
                    findNavController().navigate(action)
                }

            } else {
                val action =
                    SplashFragmentDirections.actionSplashFragmentToLoginFragment()
                findNavController().navigate(action)
                //findNavController().navigate(R.id.action_splashFragment_to_loginFragment)

            }
        }, 3000)


        return view
    }
}