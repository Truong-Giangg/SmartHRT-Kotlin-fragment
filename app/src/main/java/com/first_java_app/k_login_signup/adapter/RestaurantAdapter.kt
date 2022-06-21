package com.first_java_app.k_login_signup.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.first_java_app.k_login_signup.R

import com.first_java_app.k_login_signup.model.Restaurant


class RestaurantAdapter(val mListener: OnItemClickListener) : ListAdapter<Restaurant, RestaurantAdapter.RestaurantViewHodel>(IdolDiffUtil()) {

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
    class IdolDiffUtil : DiffUtil.ItemCallback<Restaurant>(){
        override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHodel {
        return RestaurantViewHodel.from(parent,mListener)
    }

    override fun onBindViewHolder(holder: RestaurantViewHodel, position: Int) {
        val restaurant = getItem(position)
        holder.bindData(restaurant)
    }


    class RestaurantViewHodel(itemView: View,listener : OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        companion object{
            fun from(parent: ViewGroup,listener : OnItemClickListener) : RestaurantViewHodel{
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.item_menu_list_restaurant_adapter, parent, false)
                return RestaurantViewHodel(view,listener )
            }
        }
        init{
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }

        fun bindData(restaurant : Restaurant){
            val tvTen = itemView.findViewById<TextView>(R.id.txt_ten)
            val tvDiaChi = itemView.findViewById<TextView>(R.id.txt_diachi)
            val ivAvatar = itemView.findViewById<ImageView>(R.id.imgHinh)

            tvTen.text = restaurant.name
            tvDiaChi.text = restaurant.address
            Glide.with(itemView).load(restaurant.image).into(ivAvatar)
        }
    }



}


