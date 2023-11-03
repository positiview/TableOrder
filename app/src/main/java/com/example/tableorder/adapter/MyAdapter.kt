package com.example.tableorder.adapter
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.tableorder.R
//
//class MyAdapter(private var dataList: List<String>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
//
//    // ViewHolder 클래스 정의
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(dataList[position])
//    }
//
//    override fun getItemCount(): Int {
//        return dataList.size
//    }
//
//    // 데이터 업데이트 함수 정의
//    fun updateData(newData: List<String>) {
//        dataList = newData
//        notifyDataSetChanged()
//    }
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val itemText: TextView = itemView.findViewById(R.id.itemText)
//
//        fun bind(item: String) {
//            itemText.text = item
//        }
//    }
//}