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
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class BookedUserListAdapter : RecyclerView.Adapter<BookedUserListAdapter.ViewHolder>() {

    private var booking = listOf<BookingDTO>()
    private var checked = MutableList(booking.size) { false }
    // Firebase Realtime Database 참조를 가져옵니다.
    private val databaseReference = Firebase.database.getReference("bookings")

    // 체크된 아이템이 있는지 확인합니다.
    fun hasCheckedItems(): Boolean {
        return checked.contains(true)
    }
    class ViewHolder(val binding: BookedUserItemBinding, ): RecyclerView.ViewHolder(binding.root){
        fun bind(bookingDTO: BookingDTO, position: Int, checked: MutableList<Boolean>){
            Log.d("$$","bookingDTO binding!")
            binding.bookingNumberView.text = bookingDTO.bookNum
            binding.usernameView.text = bookingDTO.userName
            binding.peooleNumberView.text = bookingDTO.memberCount.toString()
            binding.bookingTimeView.text = bookingDTO.reservationTime
            itemView.setOnClickListener {
                binding.receiveCheckBox.isChecked = !binding.receiveCheckBox.isChecked
                checked[position] = binding.receiveCheckBox.isChecked
            }
            if (bookingDTO.uid != null) {
                getBookingUserImg(bookingDTO.uid)
            }
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
        val bookingDTO = booking[position]
        Log.d("$$", "onBindViewHodler : $position")
        if(position == 0){
            holder.binding.root.elevation = 50f
        }


        holder.bind(bookingDTO,position, checked)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setBookingList(bookingList : List<BookingDTO>){
        this.booking = bookingList
        this.checked = MutableList(booking.size) { false }
        notifyDataSetChanged()
    }




    // 체크된 아이템을 삭제하는 메소드를 추가합니다.
    fun removeCheckedItems() {
        booking.forEachIndexed { index, bookingDTO ->
            if (checked[index]) {
                // 해당 아이템의 bookNum이 bookingDTO.bookNum과 같은 데이터를 찾는 쿼리를 작성합니다.
                val query = databaseReference.orderByChild("bookNum").equalTo(bookingDTO.bookNum)

                // 쿼리에 단일 이벤트 리스너를 설정하여 일치하는 데이터를 찾고 삭제합니다.
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (data in snapshot.children) {
                            data.ref.removeValue()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("removeCheckedItems", "Failed to remove checked items", error.toException())
                    }
                })
            }
        }
        notifyDataSetChanged()
    }

    // 첫 번째 아이템을 삭제하는 메소드를 추가합니다.
    fun removeFirstItem() {
        if (booking.isNotEmpty()) {
            // 첫 번째 아이템의 bookNum을 사용하여 데이터베이스에서 해당 아이템을 찾고 삭제합니다.
            val query = databaseReference.orderByChild("bookNum").equalTo(booking[0].bookNum)

            // 쿼리에 단일 이벤트 리스너를 설정하여 일치하는 데이터를 찾고 삭제합니다.
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        data.ref.removeValue()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("removeFirstItem", "Failed to remove first item", error.toException())
                }
            })


        }
        notifyDataSetChanged()
    }

}