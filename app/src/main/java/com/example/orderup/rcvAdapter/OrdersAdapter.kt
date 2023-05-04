package com.example.orderup.rcvAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.orderup.R
import com.example.orderup.databinding.RcvOrderItemsBinding
import com.example.orderup.lib.tool
import com.example.orderup.model.ModelOrder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class OrdersAdapter : RecyclerView.Adapter<OrdersAdapter.HolderView> {

    private lateinit var binding: RcvOrderItemsBinding
    private var ordersArraylist: ArrayList<ModelOrder>
    private var context: Context

    constructor(ordersArraylist: ArrayList<ModelOrder>, context: Context) : super() {
        this.ordersArraylist = ordersArraylist
        this.context = context
    }

    inner class HolderView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var timeTv: TextView = binding.timeTv
        var restaurantNameTv: TextView = binding.restaurantNameTv
        var itemsCountTv: TextView = binding.itemsCountTv
        var stateOrderTv: TextView = binding.stateOrderTv
        var preiceOrderTv: TextView = binding.preiceOrderTv
        var imgStateIv: ImageView = binding.stateIv
        var doneTabLl: LinearLayout = binding.doneTabs
        var excutingTabLl: LinearLayout = binding.excutingTab
        var reorderBtn: Button = binding.reorderBtn
        var rateBtn: Button = binding.rateBtn
        var cancelOrConfirmBtn: Button = binding.cancelOrConfirmBtn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderView {
        binding = RcvOrderItemsBinding.inflate(LayoutInflater.from(context), parent, false)

        return HolderView(binding.root)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: HolderView, position: Int) {
        val model = ordersArraylist[position]
        val timestamp = model.timestamp
        val orderId = model.id
        val items = model.items
        val address = model.address
        val restaurantName = "Ordered in ${tool.calculateDiffTime(model.timestamp)}"
        val state = model.state
        val price = model.total
        Log.i("ORDER", timestamp.toString())
        holder.timeTv.text = tool.getDate(timestamp)
        holder.restaurantNameTv.text = restaurantName
        holder.itemsCountTv.text = "${items.size} items"
        holder.stateOrderTv.text = state.capitalize()
        holder.preiceOrderTv.text = price.toString()
        if (model.state == "waiting") {
            holder.doneTabLl.visibility = View.INVISIBLE
            holder.imgStateIv.setImageResource(R.drawable.waiting)
            holder.stateOrderTv.setTextColor(ContextCompat.getColor(context, R.color.mainOrange))
            holder.cancelOrConfirmBtn.text = "Cancel"
            holder.cancelOrConfirmBtn.setOnClickListener {
                cancelOrder(model.id)
            }
        } else if (model.state == "deliving") {
            holder.doneTabLl.visibility = View.INVISIBLE
            holder.imgStateIv.setImageResource(R.drawable.confirm)
            holder.cancelOrConfirmBtn.text = "Confirm"
            holder.stateOrderTv.setTextColor(ContextCompat.getColor(context, R.color.mainOrange))
            holder.cancelOrConfirmBtn.setOnClickListener {
                confirmReceived(model.id)
            }
        } else if (model.state == "confirmed") {
            holder.excutingTabLl.visibility = View.INVISIBLE
            holder.imgStateIv.setImageResource(R.drawable.done)
            holder.reorderBtn.setOnClickListener {
                Toast.makeText(context, "Reorder clciked", Toast.LENGTH_SHORT).show()
            }
            holder.rateBtn.setOnClickListener {
                Toast.makeText(context, "rate clciked", Toast.LENGTH_SHORT).show()
            }
        } else {
            holder.excutingTabLl.visibility = View.INVISIBLE
            holder.doneTabLl.visibility = View.INVISIBLE
            holder.imgStateIv.setImageResource(R.drawable.canceled)
            holder.stateOrderTv.setTextColor(ContextCompat.getColor(context, R.color.red))
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