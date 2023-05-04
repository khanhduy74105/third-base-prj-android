package com.example.orderup.rcvAdapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.orderup.FoodDetailActivity
import com.example.orderup.R
import com.example.orderup.UserSearchActivity
import com.example.orderup.databinding.RcvCategoryItemColBinding
import com.example.orderup.databinding.RcvRestaurantCarditemColBinding
import com.example.orderup.model.ModalUser
import com.example.orderup.model.ModelCategory
import kotlin.math.nextDown
import kotlin.math.roundToInt

class RestaurantAdapter: RecyclerView.Adapter<RestaurantAdapter.CategoryHolder> {
    private lateinit var binding:RcvRestaurantCarditemColBinding

    private var context: Context
    public var restaurantArraylist: ArrayList<ModalUser>

    constructor(context: Context, restaurantArraylist: ArrayList<ModalUser>) {
        this.context = context
        this.restaurantArraylist = restaurantArraylist
    }

    inner class CategoryHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var restaurantTv:TextView = binding.resNameTv
        var addressTv:TextView = binding.addressTv
        var restaurantIv: ImageView = binding.restaurantIv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        binding = RcvRestaurantCarditemColBinding.inflate(LayoutInflater.from(context), parent, false)

        return CategoryHolder(binding.root)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        val model = restaurantArraylist[position]

        holder.restaurantTv.text = model.username
        holder.addressTv.text = model.address
        val imageUrl = model.profileImage
        val imgUri = imageUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(holder.restaurantIv.context)
            .load(imgUri)
            .centerCrop()
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image))
            .into(holder.restaurantIv)
    }

    override fun getItemCount(): Int {
        return restaurantArraylist.size
    }



}