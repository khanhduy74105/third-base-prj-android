package com.example.orderup.rcvAdapter

import android.annotation.SuppressLint
import android.app.AlertDialog
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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
        holder.id.text = changeTimestampToHourDayMonth(model.timestamp)
        holder.numberOfItem.text = items.size.toString()
        holder.totalMoney.text = price.toString()
        if (model.state == "waiting") {
            if(!isTwoHoursAgo(model.timestamp)){
                cancelOrder(model.uid,orderId)
            }else{
                holder.status.text = "waiting"
                holder.status.setOnClickListener {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Confirm this order?")
                        .setMessage("Are you sure to order those items")
                        .setPositiveButton("Confirm"){
                                a,d ->
                            Toast.makeText(context, "Confirming...", Toast.LENGTH_SHORT).show()
                            confirmReceived(model.uid,orderId)
                        }
                        .setNegativeButton("Cancel"){
                                a,d ->
                            a.dismiss()
                            cancelOrder(model.uid,orderId)
                        }
                        .show()
                }
            }
        } else if (model.state == "deliving") {
            holder.status.text = "delivering"
        } else if (model.state == "confirmed") {
            holder.status.text = "confirm"
        } else {
            holder.status.text = "Cancel"
        }
    }
    fun isTwoHoursAgo(timestamp: Long): Boolean {
        val currentTime = System.currentTimeMillis()
        return currentTime - timestamp <= 2 * 60 * 60 * 1000
    }
    fun changeTimestampToHourDayMonth(timestamp: Long): String {
        val date = Date(timestamp)
        val dateFormat = SimpleDateFormat("dd-MM", Locale.getDefault())
        return dateFormat.format(date)
    }
    private fun confirmReceived(uidOrder:String,id: String) {
        val ref = FirebaseDatabase.getInstance().getReference("Orders/${uidOrder}/$id")
        ref.child("state")
            .setValue("confirmed")
            .addOnSuccessListener {
                Toast.makeText(context, "Canceled orders", Toast.LENGTH_SHORT).show()
            }
    }

    private fun cancelOrder(uidOrder:String, id: String) {
        val ref = FirebaseDatabase.getInstance().getReference("Orders/${uidOrder}/$id")
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