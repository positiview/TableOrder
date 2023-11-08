package com.example.mytableorder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mytableorder.R
import com.example.mytableorder.fragment.admin.AdminListDTO

class UserListAdapter : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {
    private var restaurants: List<AdminListDTO> = listOf()

    //클릭 아이템 클릭리스터 추가
    interface OnItemClickListener {
        fun onItemClick(restaurant: AdminListDTO)
    }

    // 리스너 변수를 선언합니다.
    private var onItemClickListener: OnItemClickListener? = null


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userImageView: ImageView = view.findViewById(R.id.userImgView)
        val userTextView: TextView = view.findViewById(R.id.userImgViewText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_user_card_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurant = restaurants[position]

        holder.userTextView.text = restaurant.raName

        Glide.with(holder.userImageView.context)
            .load(restaurant.raImg) // 이미지 URL을 로드
            .into(holder.userImageView)

        //아이템뷰 추가

        // 항목 클릭 시 리스너의 onItemClick 메서드를 호출합니다.
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(restaurant)
        }
    }

    //클릭리스너 설정할수 있게 메서드 추가
    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun getItemCount() = restaurants.size

    fun setRestaurants(restaurantList: List<AdminListDTO>) {
        this.restaurants = restaurantList
        notifyDataSetChanged()
    }


}