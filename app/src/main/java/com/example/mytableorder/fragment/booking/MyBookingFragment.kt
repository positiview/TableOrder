package com.example.mytableorder.fragment.booking

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mytableorder.R
import com.example.mytableorder.databinding.FragmentMyBookingBinding
import com.example.mytableorder.repository.BookingRepository
import com.example.mytableorder.repository.BookingRepositoryImpl
import com.example.mytableorder.utils.Resource
import com.example.mytableorder.viewModel.BookingViewModel
import com.example.mytableorder.viewmodelFactory.BookingViewModelFactory
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MyBookingFragment : Fragment() {

    private var _binding: FragmentMyBookingBinding? = null
    private val binding get() = _binding!!

    private val bookingRepository: BookingRepository = BookingRepositoryImpl()
    private val bookingViewModelFactory: BookingViewModelFactory = BookingViewModelFactory(bookingRepository)
    private val viewModel: BookingViewModel by activityViewModels() { bookingViewModelFactory }
    private var auth: FirebaseAuth = Firebase.auth

    private lateinit var bookNum:String




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        // 방금 예약한 예약결과
        viewModel.getBookingData()

        checkBookingData()


        val buttonCancel = binding.buttonCancel
        buttonCancel.setOnClickListener {
            bookingCancel()


        }


        val buttonHome = binding.buttonHome
        buttonHome.setOnClickListener {
            findNavController().navigate(R.id.action_myBookingFragment_to_homeFragment)
        }
    }

    private fun bookingCancel() {
        val builder = AlertDialog.Builder(requireContext()).create()
        val dialogView = layoutInflater.inflate(R.layout.confirm_dialog, null)
        dialogView.findViewById<MaterialTextView>(R.id.confirm).text = "정말 예약 취소하시겠습니까?"
        val okBtn = dialogView.findViewById<MaterialButton>(R.id.dialogConfirm)
        val cancelBtn = dialogView.findViewById<MaterialButton>(R.id.dialogCancel)

        okBtn.setOnClickListener {

            viewModel.deleteBookingData()
            Toast.makeText(requireContext(),"에약 취소 완료",Toast.LENGTH_SHORT).show()

            builder.dismiss()
        }

        cancelBtn.setOnClickListener {
            builder.dismiss()
        }

        builder.setView(dialogView)
        builder.show()
    }

    private fun checkBookingData() {
        viewModel.getBookingResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressCircular.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressCircular.visibility = View.GONE
                    if (resource.data == null) {
                        MaterialAlertDialogBuilder(binding.root.context)
                            .setTitle("Alert")
                            .setMessage("현재 예약 내역이 없습니다.")
                            .setPositiveButton("확인") { dialog, _ ->
                                dialog.dismiss()
                                findNavController().popBackStack()
                            }
                            .setCancelable(false)
                            .show()
                    } else {
                        displayBooking(resource.data)
                    }
                }
                is Resource.Error -> {
                    binding.progressCircular.visibility = View.GONE
                    Toast.makeText(context, resource.string, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun displayBooking(bookings: BookingDTO?) {
        Log.d("$$","Bookings : $bookings")
        bookings?.let { value ->
            // value가 실제로 BookingDTO 타입인지 확인합니다.

            // BookingDTO에서 값을 추출하고 TextView에 설정합니다.
            val userName1 = value.userName
            val bookNum1 = value.restaurantNum
            val bookCount1 = value.memberCount
            val bookTime1 = value.reservationTime

            Log.e("$$", "userName =$userName1")
            Log.e("$$", "bookNum =$bookNum1")
            Log.e("$$", "bookCount =$bookCount1")
            Log.e("$$", "bookTime =$bookTime1")

            with(binding) {
                bookUser.text = userName1
                bookNum.text = bookNum1.toString()
                bookCount.text = bookCount1.toString()
                bookTime.text = bookTime1 ?: "No time specified"
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}