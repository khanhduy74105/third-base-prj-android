package com.example.orderup.modelview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.orderup.model.ModelOrder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class ChartForMoneyViewModel: ViewModel {
    private var _usersUid: MutableLiveData<ArrayList<String>> =
        MutableLiveData<ArrayList<String>>()
    private var _money: MutableLiveData<ArrayList<ModelOrder>> =
        MutableLiveData<ArrayList<ModelOrder>>()
    val usersIDItemArraylist: LiveData<ArrayList<String>>
        get() = _usersUid
    val orderItemArraylist: LiveData<ArrayList<ModelOrder>>
        get()=_money
    private var _moneys: MutableLiveData<Array<Long>> =
        MutableLiveData<Array<Long>>()
    val moneysItem :LiveData<Array<Long>>
        get()= _moneys

    constructor() {
    }
    init {
        getUser()
    }

    fun getUser(){
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userUID = ArrayList<String>()
                for(ds in snapshot.children){
                    val model = ds.child("uid").value.toString()
                    userUID.add(model)
                }
                _usersUid.postValue(userUID)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    fun getData(_usersUidRe: ArrayList<String>?) {
        if (_usersUidRe != null) {
            for (i in 1.. _usersUidRe.size) {
                Log.i("money",_usersUidRe[i])
            }
        }
        if (_usersUidRe != null) {
            for (i in 1.. _usersUidRe.size) {
                val ref = FirebaseDatabase.getInstance().getReference("Orders/${_usersUidRe[i-1]}")
                ref.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var listMoneyMonth = arrayOf(0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L)
                        val orders = ArrayList<ModelOrder>()
                        for (ds in snapshot.children) {
                            val model = ds.getValue(ModelOrder::class.java)
                            if (model != null) {
                                if(model.state=="confirmed"){
                                    Log.i("money",model.total.toString())
                                    orders.add(model)
                                }
                            }
                        }
                        _moneys.postValue(listMoneyMonth)
                        _money.postValue(orders)
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }
        }
    }

}

