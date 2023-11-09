package com.example.mytableorder.fragment.restaurant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.fragment.app.Fragment
import com.example.mytableorder.adapter.AdminListAdapter
import com.example.mytableorder.adapter.BookedUserListAdapter
import com.example.mytableorder.databinding.FragmentRestaurantBookedusersBinding


class BookedUserListFragment : Fragment() {
    private lateinit var binding: FragmentRestaurantBookedusersBinding
    private lateinit var bookedUserListAdapter: BookedUserListAdapter
    /*private val viewModel: ViewModel by activityViewModels{  }
    private val adapter by lazy { Adapter() }*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRestaurantBookedusersBinding.inflate(inflater, container, false)
        val view = binding.root



        bookedUserListAdapter = BookedUserListAdapter()

//        binding.recyclerView.adapter = adapter

       /* viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.getDonations()
        }*/

       /* viewModel.donations.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> {
                    binding.progressCircular.isVisible = true
                }
                is Resource.Success -> {
                    binding.progressCircular.isVisible = false
                    adapter.submitList(state.data)
                }
                is Resource.Error -> {
                    binding.progressCircular.isVisible = false
                    Toast.makeText(requireContext(), "An error occurred", Toast.LENGTH_SHORT).show()
                }
            }
        }*/

        return view
    }

}