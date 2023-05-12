package com.example.orderup.modelview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.orderup.lib.tool
import com.example.orderup.model.ModelOrder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OrderViewModel: ViewModel {
    private var _orderItems: MutableLiveData<ArrayList<ModelOrder>> =
        MutableLiveData<ArrayList<ModelOrder>>()
    val orderItems: LiveData<ArrayList<ModelOrder>>
        get() = _orderItems

    private var _orderItemsType: MutableLiveData<ArrayList<ModelOrder>> =
        MutableLiveData<ArrayList<ModelOrder>>()
    init {
        initCartItems()
    }
    constructor()


    fun initCartItems() {
        val uid = tool.getCurrentId()
        val ref = FirebaseDatabase.getInstance().getReference("Orders/$uid")
        ref.orderByChild("timestamp")
            .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val cartItems = ArrayList<ModelOrder>()
                for (ds in snapshot.children) {
                    val model = ds.getValue(ModelOrder::class.java)
                    if (model != null) {
                        cartItems.add(model)
                    }
                }
                _orderItems.postValue(cartItems)
                _orderItemsType.postValue(cartItems)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun changeTypeOrders(type: String){
        val items = ArrayList<ModelOrder>()
        if (type == "confirmed"){
            for (i in 1.._orderItemsType.value!!.size) {
                val model = _orderItemsType.value!!.get(i-1)
                if (model.state == "confirmed" || model.state == "canceled") items.add(model)
            }
        }else{
            for (i in 1.._orderItemsType.value!!.size) {
                val model = _orderItemsType.value!!.get(i-1)
                if (model.state == "waiting" || model.state == "deliving") items.add(model)
            }
        }
        _orderItems.value = items
    }
}