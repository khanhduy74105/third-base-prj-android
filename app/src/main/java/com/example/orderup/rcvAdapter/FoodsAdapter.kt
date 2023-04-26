package com.example.orderup.rcvAdapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.orderup.FoodDetailActivity
import com.example.orderup.R
import com.example.orderup.databinding.RcvFoodItemBinding
import com.example.orderup.model.ModelFood

class FoodsAdapter : RecyclerView.Adapter<FoodsAdapter.HolderFood>, Filterable{
    private lateinit var binding: RcvFoodItemBinding

    private var context: Context
    public var foodsArraylist: ArrayList<ModelFood>
    private var filterList: ArrayList<ModelFood>
    private var filter: FilterFoods? = null
    constructor(context: Context, foodsArraylist: ArrayList<ModelFood>) : super() {
        this.context = context
        this.foodsArraylist = foodsArraylist
        this.filterList = foodsArraylist
    }


    inner class HolderFood(itemView: View): RecyclerView.ViewHolder(itemView){
        var foodNameTv: TextView = binding.foodNameTv
        var foodDescriptionTv: TextView = binding.foodDescriptionTv
        var foodPriceTv: TextView = binding.foodPriceTv
        var foodIv: ImageView = binding.foodIv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderFood {
        binding = RcvFoodItemBinding.inflate(LayoutInflater.from(context), parent, false)

        return HolderFood(binding.root)
    }

    override fun onBindViewHolder(holder: HolderFood, position: Int) {
        val model = foodsArraylist[position]
        val foodName = model.foodname
        val foodId = model.id
        val description = model.description
        val price = model.price
        val imageUrl = model.imageUrl

        holder.foodNameTv.text = foodName.capitalize()
        holder.foodDescriptionTv.text = description.capitalize()
        holder.foodPriceTv.text = "$price vnd"
        val imgUri = imageUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(holder.foodIv.context)
            .load(imgUri)
            .centerCrop()
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image))
            .into(holder.foodIv)

        holder.itemView.setOnClickListener {
            toDetailFood(foodId)
        }
        Log.i("ADAPTER", foodName)
    }

    private fun toDetailFood( foodId: String) {
        val intent = Intent(context, FoodDetailActivity::class.java)
        intent.putExtra("foodId", foodId)
        context.startActivity(intent)
    }

    override fun getItemCount(): Int {
        return foodsArraylist.size
    }

    override fun getFilter(): Filter {
        if (filter == null){
            filter = FilterFoods(filterList, this)
        }
        return filter as FilterFoods
    }

}