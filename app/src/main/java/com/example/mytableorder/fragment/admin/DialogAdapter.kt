package com.example.mytableorder.fragment.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mytableorder.R

class DialogAdapter(private val userNameList: List<String>) :
    RecyclerView.Adapter<DialogAdapter.ViewHolder>() {


    private var onItemClickListener: OnItemClickListener? = null


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userIdTextView: TextView = itemView.findViewById(R.id.userIdItem)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.dialog_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userName = userNameList[position]
        holder.userIdTextView.text = userName

        holder.itemView.setOnClickListener {
            // 클릭 이벤트 발생 시 콜백 호출
            onItemClickListener?.onItemClicked(position)
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return userNameList.size
    }

}