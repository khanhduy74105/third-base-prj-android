package com.example.orderup.usedashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.orderup.R
import com.example.orderup.databinding.ActivityDetailOrderBinding
import com.example.orderup.lib.tool
import com.example.orderup.model.ModelCartItem
import com.example.orderup.rcvAdapter.OrderedItemsAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetailOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailOrderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
        val orderId = intent.getStringExtra("orderId")
        if (orderId != null) {
            loadDetail(orderId)
        }
    }

    fun loadDetail(orderId: String) {
        val uid = tool.getCurrentId()
        val ref = FirebaseDatabase.getInstance().getReference("Orders/$uid")
        ref.child(orderId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.orderId.text = "Order Id: ${snapshot.child("id").value}"
                    binding.addressTv.text = "Address: ${snapshot.child("address").value}"
                    binding.stateTv.text = "State: ${snapshot.child("state").value}"
                    val time:Long = snapshot.child("timestamp").value as Long
                    binding.timeTv.text = "Order time: ${tool.getDate(time)}"
                    binding.totalTv.text = "Total: ${snapshot.child("total").value} Vnd"
                    val list = ArrayList<ModelCartItem>()
                    val items =  snapshot.child("items").children
                    for (ds in items){
                        val model = ds.getValue(ModelCartItem::class.java)
                        list.add(model!!)
                        val adapter = OrderedItemsAdapter(this@DetailOrderActivity, list)
                        binding.rcvItems.adapter =adapter
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
}