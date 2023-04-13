package com.example.orderup.rcvAdapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.orderup.databinding.RcvCategoryItemRestaurantBinding
import com.example.orderup.model.ModelCategory

import com.example.orderup.restaurantdashboard.ListFoodDependCategory

class CategorysAdapter : RecyclerView.Adapter<CategorysAdapter.HolderCategory>{
    private lateinit var binding: RcvCategoryItemRestaurantBinding
    private var context: Context
    private var categoryArraylist: ArrayList<ModelCategory>

    constructor(context: Context, categoryArraylist: ArrayList<ModelCategory>) : super() {
        this.context = context
        this.categoryArraylist = categoryArraylist
    }

    inner class HolderCategory(itemView: View): RecyclerView.ViewHolder(itemView){
    var nameCategoryTV : TextView = binding.nameCategoryTV
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategory {
        binding = RcvCategoryItemRestaurantBinding.inflate(LayoutInflater.from(context), parent, false)

        return HolderCategory(binding.root)
    }

    override fun onBindViewHolder(holder: HolderCategory, position: Int) {
        val model = categoryArraylist[position]
        val nameCategory = model.category
        holder.nameCategoryTV.text = nameCategory
        holder.itemView.setOnClickListener {
            toListFood(model.timestamp.toString())
        }
        Log.i("ADAPTER", nameCategory)
    }
    private fun toListFood(nameCategory: String) {
        val intent = Intent(context,ListFoodDependCategory::class.java)
        intent.putExtra("categoryName",nameCategory)
        context.startActivity(intent)
    }


    override fun getItemCount(): Int {
        return categoryArraylist.size
    }

}