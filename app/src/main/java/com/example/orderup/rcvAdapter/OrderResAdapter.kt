package com.example.orderup.rcvAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.orderup.databinding.RcvItemOrderBinding
import com.example.orderup.lib.tool
import com.example.orderup.model.ModelOrder
import com.example.orderup.restaurantdashboard.ManageOrder
import com.google.firebase.database.FirebaseDatabase

class OrderResAdapter : RecyclerView.Adapter<OrderResAdapter.HolderView> {

    private lateinit var binding: RcvItemOrderBinding
    private var ordersArraylist: ArrayList<ModelOrder>
    private var context: Context

    constructor(context: Context, ordersArraylist: ArrayList<ModelOrder>) : super() {
        this.ordersArraylist = ordersArraylist
        this.context = context
    }

    inner class HolderView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var id: TextView = binding.id
        var numberOfItem: TextView = binding.numberOfItem
        var totalMoney: TextView = binding.totalMoney
        var status: TextView = binding.status

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderView {
        binding = RcvItemOrderBinding.inflate(LayoutInflater.from(context), parent, false)

        return HolderView(binding.root)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: HolderView, position: Int) {
        val model = ordersArraylist[position]
        val timestamp = model.timestamp
        val orderId = model.id
        val items = model.items
        val address = model.address
        val restaurantName = "Bichingling"
        val state = model.state
        val price = model.total
        holder.id.text = orderId
        holder.numberOfItem.text = items.size.toString()
        holder.totalMoney.text = price.toString()
        Log.i("ORDER", timestamp.toString())
        if (model.state == "waiting") {
            holder.status.text = "waiting"

        } else if (model.state == "deliving") {
            holder.status.text = "delivering"
        } else if (model.state == "confirmed") {
            holder.status.text = "confirm"
        } else {
            holder.status.text = "Cancel"
        }
    }

    private fun confirmReceived(id: String) {
        val uid = tool.getCurrentId()
        val ref = FirebaseDatabase.getInstance().getReference("Orders/${uid}/$id")
        ref.child("state")
            .setValue("confirmed")
            .addOnSuccessListener {
                Toast.makeText(context, "Canceled orders", Toast.LENGTH_SHORT).show()
            }
    }

    private fun cancelOrder(id: String) {
        val uid = tool.getCurrentId()
        val ref = FirebaseDatabase.getInstance().getReference("Orders/${uid}/$id")
        ref.child("state")
            .setValue("canceled")
            .addOnSuccessListener {
                Toast.makeText(context, "Canceled orders", Toast.LENGTH_SHORT).show()
            }
    }

    override fun getItemCount(): Int {
        return ordersArraylist.size
    }


}