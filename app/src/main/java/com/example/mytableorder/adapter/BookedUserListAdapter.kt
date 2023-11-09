package com.example.mytableorder.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mytableorder.databinding.BookedUserItemBinding

class BookedUserListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//    var datas = listOf<BookingDTO>()
    class ViewHolder(val binding: BookedUserItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = BookedUserItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    //    override fun getItemCount(): Int = datas.size
    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        Log.d("$$", "onBindViewHodler : $position")

    }

}