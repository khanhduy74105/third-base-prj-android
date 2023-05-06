package com.example.orderup.rcvAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.orderup.R
import com.example.orderup.databinding.RcvFoodCartBinding
import com.example.orderup.lib.tool
import com.example.orderup.model.ModelCartItem
import com.example.orderup.modelview.CartViewModel
import com.example.orderup.usedashboard.CartFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FoodsCartAdapter(
    private var context: Context,
    public var cartItemArraylist: ArrayList<ModelCartItem>,
    private val viewModel: CartViewModel
) : RecyclerView.Adapter<FoodsCartAdapter.HolderFood>() {
    private lateinit var binding: RcvFoodCartBinding

    inner class HolderFood(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var foodIv: ImageView = binding.foodIv
        var foodNameTv: TextView = binding.foodNameTv
        var priceTv: TextView = binding.priceTv
        var amountFoodTv: TextView = binding.amountFoodTv
        var minusBtn: ImageView = binding.minusFoodBtn
        var plusBtn: ImageView = binding.plusFoodBtn
        var removeBtn: ImageView = binding.removeBtn
        var amount: Int = 0

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderFood {

        binding = RcvFoodCartBinding.inflate(LayoutInflater.from(context), parent, false)

        return HolderFood(binding.root)

    }

    override fun onBindViewHolder(holder: HolderFood, position: Int) {
        val model = cartItemArraylist[position]
        holder.priceTv.text = "${model.price} vnd"
        holder.foodNameTv.text = model.foodname
        holder.amountFoodTv.text = "${model.amount}"
        holder.amount = model.amount
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
        holder.minusBtn.setOnClickListener {
            if (holder.amount > 1) {
                holder.amount--
                updateAmountItem(model.id, amount = holder.amount)
            }
//            holder.amountFoodTv.text = holder.amount.toString()
            viewModel.reloadCartItems()
            notifyDataSetChanged()
        }
        holder.plusBtn.setOnClickListener {
            holder.amount++
            updateAmountItem(model.id, amount = holder.amount)
            viewModel.reloadCartItems()
            notifyDataSetChanged()

        }
        holder.removeBtn.setOnClickListener {
            removeItem(model.id)
        }

    }

    private fun removeItem(itemId: String) {
        val uid = tool.getCurrentId()
        val ref = FirebaseDatabase.getInstance().getReference("Cart/$uid")
            ref.child(itemId)
                .removeValue()
                .addOnSuccessListener {
                    Toast.makeText(this.context, "Removed out!", Toast.LENGTH_SHORT).show()
                }
    }

    override fun getItemCount(): Int {
        return cartItemArraylist.size
    }

    fun updateAmountItem(itemId: String, amount: Int) {
        val uid = tool.getCurrentId()
        val ref = FirebaseDatabase.getInstance().getReference("Cart/$uid")
        ref.child("$itemId/amount")
            .setValue(amount)
    }




}