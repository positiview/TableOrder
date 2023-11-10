package com.example.mytableorder.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mytableorder.databinding.BookedUserItemBinding
import com.example.mytableorder.fragment.booking.BookingDTO

class BookedUserListAdapter : RecyclerView.Adapter<BookedUserListAdapter.ViewHolder>() {
    private var booking = listOf<BookingDTO>()
    class ViewHolder(val binding: BookedUserItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(bookingDTO: BookingDTO){
            Log.d("$$","bookingDTO binding!")
            binding.bookingNumberView.text
            binding.usernameView.text
            binding.peooleNumberView.text
            binding.bookingTimeView.text
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
            holder.binding.root.setCardBackgroundColor(Color.GRAY)
        }
        holder.bind(bookingNum)
    }

}