package com.example.orderup.modelview

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.orderup.lib.tool
import com.example.orderup.model.ModelCartItem
import com.example.orderup.rcvAdapter.FoodsCartAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartViewModel : ViewModel {
    private var _cartItemArraylist: MutableLiveData<ArrayList<ModelCartItem>> =
        MutableLiveData<ArrayList<ModelCartItem>>()
    val cartItemArraylist: LiveData<ArrayList<ModelCartItem>>
        get() = _cartItemArraylist

    init {
        initCartItems()
    }

    constructor()

    fun initCartItems() {
        val uid = tool.getCurrentId()
        val ref = FirebaseDatabase.getInstance().getReference("Cart/$uid")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val cartItems = ArrayList<ModelCartItem>()
                for (ds in snapshot.children) {
                    val model = ds.getValue(ModelCartItem::class.java)
                    if (model != null) {
                        cartItems.add(model)
                    }
                }
                _cartItemArraylist.postValue(cartItems)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun reloadCartItems() {
        val uid = tool.getCurrentId()
        val ref = FirebaseDatabase.getInstance().getReference("Cart/$uid")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val cartItems = ArrayList<ModelCartItem>()
                for (ds in snapshot.children) {
                    val model = ds.getValue(ModelCartItem::class.java)
                    if (model != null) {
                        cartItems.add(model)
                    }
                }
                _cartItemArraylist.value = cartItems
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun checkout(total: Long, context: Context?) {
        val timestamp = System.currentTimeMillis()
        val uid = tool.getCurrentId()
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["uid"] = uid
        hashMap["timestamp"] = timestamp
        hashMap["id"] = "$timestamp"
        hashMap["items"] = cartItemArraylist.value
        hashMap["total"] = total
        hashMap["state"] = "waiting"
        FirebaseDatabase.getInstance().getReference("Users/$uid").child("address")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    hashMap["address"] = snapshot.value
                    val ref = FirebaseDatabase.getInstance().getReference("Orders/$uid")
                    ref.child("$timestamp")
                        .setValue(hashMap)
                        .addOnSuccessListener {
                            FirebaseDatabase.getInstance().getReference("Cart/$uid").removeValue()
                            _cartItemArraylist.value?.clear()
                            Toast.makeText(context, "Ordered!", Toast.LENGTH_SHORT).show()
                        }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })


    }


}