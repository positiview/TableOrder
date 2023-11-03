package com.example.mytableorder.fragment.splash

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.fragment.findNavController
import com.example.mytableorder.R
import com.example.mytableorder.databinding.FragmentSplashBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

import javax.inject.Inject



//Fragment() 를 확장 하여 Fragment 를 정의
class SplashFragment : Fragment() {

    @Inject lateinit var auth: FirebaseAuth
    //FragmentSplashBinding 타입의 변수 binding 을 선어하며 데이터 바인딩을 통해 화면 요소와 상호작용할떄 사용
    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        //inflater 레이아웃XML 을 로드 하기위한 LayoutInflater,container는 Fragment 가 표시될 ViewGroup
        inflater: LayoutInflater, container: ViewGroup?,
        //사용 자가 입력한 텍스트 등 사라지지 않고 복구 될수있음
        //Bundle 은 키-값 쌍으로 데이터를 저장하고 다른 액티비티, 프로그래그먼트 또는 컴포넌트로 데이터를 전달하
        //는데 주로 사용함. 예를들어 이전 화면 또는 다른 액티비티로 이동하는 경우 데이터 보존하고 복구하는데 유용.
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        val view = binding.root
//        (activity as AppCompatActivity).supportActionBar?.hide()
//        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        auth = Firebase.auth
        val user = auth.currentUser


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
                if (userType == "admin"){
                    val action = ActionOnlyNavDirections(R.id.action_splashFragment_to_adminHomeFragment);
                    findNavController().navigate(action)



                    }else {
                    val action =
                        ActionOnlyNavDirections(R.id.action_splashFragment_to_homeFragment)
                    findNavController().navigate(action)
                }

            } else {
                val action = ActionOnlyNavDirections(R.id.action_splashFragment_to_loginFragment)

                 findNavController().navigate(R.id.action_splashFragment_to_loginFragment)

            }
        }, 3000)


        return view
    }
}