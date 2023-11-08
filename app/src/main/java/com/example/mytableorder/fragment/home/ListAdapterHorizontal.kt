package com.example.mytableorder.fragment.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mytableorder.R
import com.example.mytableorder.fragment.admin.AdminListDTO


// 1. , , private val onItemClicked: (Int) -> Unit) 추가
class ListAdapterHorizontal(
    private var items: List<AdminListDTO>, // Pair<이미지 URL, 가게 이름>
    private val onItemClicked: (Int) -> Unit
) : RecyclerView.Adapter<ListAdapterHorizontal.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.userImgList)
        val textView: TextView = view.findViewById(R.id.userTextImgList)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_hori, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.textView.text = item.raName
        Glide.with(holder.imageView.context)
            .load(item.raImg)
            .into(holder.imageView)
        holder.itemView.setOnClickListener { onItemClicked(position) }
    }

    override fun getItemCount() = items.size
}