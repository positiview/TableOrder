package com.example.mytableorder.fragment.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mytableorder.R


// 1. , , private val onItemClicked: (Int) -> Unit) 추가
class ListAdapterHorizontal(var list: ArrayList<String>, private val onItemClicked: (Int) -> Unit):  RecyclerView.Adapter<ListAdapterHorizontal.ListAdapter>() {

    class ListAdapter(val layout: View): RecyclerView.ViewHolder(layout) {
        val textImg = layout.findViewById<TextView>(R.id.textImg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_hori, parent, false)
        return ListAdapter(view)
    }

    //2.  holder.itemView.setOnClickListener {
    //            onItemClicked(position)
    //        }  클릭 리스너 추가
    override fun onBindViewHolder(holder: ListAdapter, position: Int) {
        holder.textImg.text = list[position]
        holder.itemView.setOnClickListener {
            onItemClicked(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}