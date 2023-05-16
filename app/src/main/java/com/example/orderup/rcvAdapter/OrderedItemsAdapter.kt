package com.example.orderup.rcvAdapter

import android.content.Context
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
import com.example.orderup.databinding.RcvFoodCartBinding
import com.example.orderup.databinding.RcvOrderedItemBinding
import com.example.orderup.model.ModelCartItem
import com.example.orderup.modelview.CartViewModel

class OrderedItemsAdapter(
    private var context: Context,
    private var cartItemArraylist: ArrayList<ModelCartItem>,
) : RecyclerView.Adapter<OrderedItemsAdapter.HolderFood>() {
    private lateinit var binding: RcvOrderedItemBinding

    inner class HolderFood(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var foodIv: ImageView = binding.foodIv
        var foodNameTv: TextView = binding.foodNameTv
        var priceTv: TextView = binding.priceTv
        var amountFoodTv: TextView = binding.amountFoodTv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderFood {
        binding = RcvOrderedItemBinding.inflate(LayoutInflater.from(context), parent, false)

        return HolderFood(binding.root)
    }

    override fun onBindViewHolder(holder: HolderFood, position: Int) {
        val model = cartItemArraylist[position]
        holder.priceTv.text = "${model.price} vnd"
        holder.foodNameTv.text = model.foodname
        holder.amountFoodTv.text = "Amount: ${model.amount}"
        val imgUri = model.imageUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(holder.foodIv.context)
            .load(imgUri)
            .centerCrop()
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(holder.foodIv)
    }

    override fun getItemCount(): Int {
        return cartItemArraylist.size
    }

}