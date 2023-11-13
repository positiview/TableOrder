package com.example.mytableorder.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mytableorder.R
import com.example.mytableorder.databinding.BookedUserItemBinding
import com.example.mytableorder.fragment.admin.AdminListDTO
import com.example.mytableorder.fragment.booking.BookingDTO
import com.example.mytableorder.utils.Resource
import com.example.mytableorder.viewModel.BookingViewModel
import com.example.mytableorder.viewModel.UserViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class BookedUserListAdapter : RecyclerView.Adapter<BookedUserListAdapter.ViewHolder>() {

    private var booking = listOf<BookingDTO>()

    class ViewHolder(val binding: BookedUserItemBinding, ): RecyclerView.ViewHolder(binding.root){
        fun bind(bookingDTO: BookingDTO){
            Log.d("$$","bookingDTO binding!")
            binding.bookingNumberView.text = bookingDTO.bookNum
            binding.usernameView.text = bookingDTO.userName
            binding.peooleNumberView.text = bookingDTO.memberCount.toString()
            binding.bookingTimeView.text = bookingDTO.reservationTime

            getBookingUserImg(bookingDTO.uid)
            getCheckedStatus()
        }

        private fun getCheckedStatus() {
            binding.receiveCheckBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    binding.root.setBackgroundColor(Color.LTGRAY)
                } else {

                    binding.root.setBackgroundColor(Color.WHITE)
                }
            }
        }

        private fun getBookingUserImg(uid: String) {
            if(uid.isNotEmpty()){
                val storageRef = Firebase.storage.reference
                val imgRef: StorageReference = storageRef.child("user/$uid")
                imgRef.downloadUrl.addOnSuccessListener {
                    Log.d("$$","check downloadUrl : $it")
                    Glide.with(binding.root.context).load(it).error(R.drawable.img_user).into(binding.accountIvProfile)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = BookedUserItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = booking.size


    // 포지션 별로 코드 처리
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bookingNum = booking[position]
        Log.d("$$", "onBindViewHodler : $position")
        if(position == 0){
            holder.binding.root.elevation = 50f

        }
        holder.bind(bookingNum)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setBookingList(bookingList : List<BookingDTO>){
        this.booking = bookingList
        notifyDataSetChanged()
    }


}