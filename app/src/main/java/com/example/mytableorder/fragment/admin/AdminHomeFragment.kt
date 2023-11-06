package com.example.mytableorder.fragment.admin

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.mytableorder.R
import com.example.mytableorder.databinding.FragmentAdminHomeBinding
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class AdminHomeFragment : Fragment(), MenuProvider {

    private lateinit var binding: FragmentAdminHomeBinding

    private val instance = this

    @Inject lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAdminHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        requireActivity().addMenuProvider(this, viewLifecycleOwner )

        (activity as AppCompatActivity).supportActionBar?.show()


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 여기에서 buttonR ID를 가진 "등록하기" 버튼에 대한 참조를 얻습니다.
        val buttonRegister = binding.buttonR
        buttonRegister.setOnClickListener {
            // 여기에서 Navigation Component를 사용하여 AdminWriteListFragment로 이동합니다.
            findNavController().navigate(R.id.action_adminHomeFragment_to_adminWriteFragment)
        }
        // 리스트보기
        val buttonList = binding.buttonList
        buttonList.setOnClickListener {
            findNavController().navigate(R.id.action_adminHomeFragment_to_adminListFragment)
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.admin_menu, menu)
        menu.findItem(R.id.action_logout).setOnMenuItemClickListener {
            auth.signOut()

            true
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId){
            R.id.action_logout -> {
                auth.signOut()

            }
        }
        return true
    }

}