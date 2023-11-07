package com.example.mytableorder.fragment.board

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mytableorder.R


// 1. 클릭 리스너 인터페이스 정의


class ListAdapterBoard(private val onItemClickListener: OnItemClickListener):  RecyclerView.Adapter<ListAdapterBoard.ListAdapter>() {
    var titles = arrayListOf("Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9", "Title 10", "Title 11")
    var contents = arrayListOf("Content 1", "Content 2", "Content 3", "Content 4", "Content 5", "Content 6", "Content 7", "Content 8", "Content 9", "Content 10", "Content 11")

    class ListAdapter(val layout: View): RecyclerView.ViewHolder(layout) {
        val textTitle = layout.findViewById<TextView>(R.id.textTitle)
        val textContent = layout.findViewById<TextView>(R.id.textContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_board, parent, false)
        return ListAdapter(view)
    }

    override fun onBindViewHolder(holder: ListAdapter, position: Int) {
        holder.textTitle.text = titles[position]
        holder.textContent.text = contents[position]

        //3. 클릭리스너 추가
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(position)
        }

    }

    override fun getItemCount(): Int {
        return contents.size
    }
}