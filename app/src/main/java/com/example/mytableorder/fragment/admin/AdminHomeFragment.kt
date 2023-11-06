package com.example.mytableorder.fragment.admin

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
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