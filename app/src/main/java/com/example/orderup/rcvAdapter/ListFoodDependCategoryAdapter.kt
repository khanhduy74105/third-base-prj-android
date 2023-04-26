package com.example.orderup.rcvAdapter
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.orderup.R
import com.example.orderup.databinding.RcvFoodItemBinding
import com.example.orderup.model.ModelFood

class ListFoodDependCategoryAdapter : RecyclerView.Adapter<ListFoodDependCategoryAdapter.HolderCategory>{
    private lateinit var binding: RcvFoodItemBinding
    private var context: Context
    private var foodArraylist: ArrayList<ModelFood>

    constructor(context: Context, foodArraylist: ArrayList<ModelFood>) : super() {
        this.context = context
        this.foodArraylist = foodArraylist
    }

    inner class HolderCategory(itemView: View): RecyclerView.ViewHolder(itemView){
        var foodNameTv: TextView = binding.foodNameTv
        var foodDescriptionTv: TextView = binding.foodDescriptionTv
        var foodPriceTv: TextView = binding.foodPriceTv
        var foodIv: ImageView = binding.foodIv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategory {
        binding = RcvFoodItemBinding.inflate(LayoutInflater.from(context), parent, false)

        return HolderCategory(binding.root)
    }
    override fun onBindViewHolder(holder: HolderCategory, position: Int) {
        val model = foodArraylist[position]
        val foodName = model.foodname
        val foodId = model.id
        val description = model.description
        val price = model.price
        val imageUrl = model.imageUrl
        holder.foodNameTv.text = foodName
        holder.foodDescriptionTv.text = description
        holder.foodPriceTv.text = price
        val imgUri = imageUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(holder.foodIv.context)
            .load(imgUri)
            .centerCrop()
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image))
            .into(holder.foodIv)
    }
    override fun getItemCount(): Int {
        return foodArraylist.size
    }
}