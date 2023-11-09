package com.example.mytableorder.fragment.receive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mytableorder.databinding.FragmentRestaurantBookedusersBinding


class RestaurantFragment : Fragment() {
    private lateinit var binding: FragmentRestaurantBookedusersBinding
    /*private val viewModel: DonationsViewModel by viewModels()
    private val adapter by lazy { DonationsAdapter() }*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRestaurantBookedusersBinding.inflate(inflater, container, false)
        val view = binding.root


//        binding.recyclerView.adapter = adapter
/*
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.getDonations()
        }

        viewModel.donations.observe(viewLifecycleOwner) { state ->
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