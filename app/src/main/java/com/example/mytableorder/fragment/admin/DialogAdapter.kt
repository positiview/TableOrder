package com.example.mytableorder.fragment.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mytableorder.R

class DialogAdapter(private val userIdList: List<String>) :
    RecyclerView.Adapter<DialogAdapter.ViewHolder>() {


    private var onItemClickListener: OnItemClickListener? = null


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userIdTextView: TextView = itemView.findViewById(R.id.userId)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.dialog_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userId = userIdList[position]
        holder.userIdTextView.text = userId

        holder.itemView.setOnClickListener {
            // 클릭 이벤트 발생 시 콜백 호출
            onItemClickListener?.onItemClicked(userId)
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(userId: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return userIdList.size
    }

}