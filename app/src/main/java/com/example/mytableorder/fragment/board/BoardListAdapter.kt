package com.example.mytableorder.fragment.board

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mytableorder.R
import com.example.mytableorder.fragment.board.OnItemClickListener


class BoardListAdapter(private val titleList: List<String>, private val timestampList: List<String>) : RecyclerView.Adapter<BoardListAdapter.BoardViewHolder>() {
    private var onItemClickListener: OnItemClickListener? = null


    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_board, parent, false)
        return BoardViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        val title = titleList[position]
        val timestamp = timestampList.getOrNull(position)
        holder.bind(title, timestamp)
    }

    override fun getItemCount(): Int {
        return titleList.size
    }

    inner class BoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.textTitle)
        private val timestampTextView: TextView = itemView.findViewById(R.id.textTimestamp) // Add this line

        fun bind(title: String, timestamp: String?) {
            titleTextView.text = title
            timestampTextView.text = timestamp ?: "No timestamp available" // Set the timestamp text

            itemView.setOnClickListener {
                // 클릭 리스너를 호출하고 클릭한 항목의 위치를 전달합니다.
                onItemClickListener?.onItemClick(adapterPosition)
            }
        }
    }
}