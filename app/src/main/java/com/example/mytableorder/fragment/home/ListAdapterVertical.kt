package com.example.mytableorder.fragment.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mytableorder.R
import com.example.mytableorder.fragment.admin.AdminListDTO



class ListAdapterVertical(
    private var items: List<AdminListDTO>,
    private val onItemClicked: (Int) -> Unit
) : RecyclerView.Adapter<ListAdapterVertical.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.findViewById(R.id.textTitle)
        val contentView: TextView = view.findViewById(R.id.textContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_vert, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.titleView.text = item.raName
        holder.contentView.text = item.raInfo
        holder.itemView.setOnClickListener { onItemClicked(position) }
    }

    override fun getItemCount() = items.size
}