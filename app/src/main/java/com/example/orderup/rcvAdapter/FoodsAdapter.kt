package com.example.orderup.rcvAdapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orderup.FoodDetailActivity
import com.example.orderup.databinding.RcvFoodItemBinding
import com.example.orderup.model.ModelFood

class FoodsAdapter : RecyclerView.Adapter<FoodsAdapter.HolderFood>{
    private lateinit var binding: RcvFoodItemBinding

    private var context: Context
    private var foodsArraylist: ArrayList<ModelFood>

    constructor(context: Context, foodsArraylist: ArrayList<ModelFood>) : super() {
        this.context = context
        this.foodsArraylist = foodsArraylist
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
        val foodName = model.foodName
        val foodId = model.id
        val description = model.description
        val price = model.price
        val url = model.url

        holder.foodNameTv.text = foodName
        holder.foodDescriptionTv.text = description
        holder.foodPriceTv.text = price.toString()

        holder.itemView.setOnClickListener {
            toDetailFood(foodId)
        }
    }

    private fun toDetailFood( foodId: String) {
        val intent = Intent(context, FoodDetailActivity::class.java)
        intent.putExtra("foodId", foodId)
        context.startActivity(intent)

    }

    override fun getItemCount(): Int {
        return foodsArraylist.size
    }

}