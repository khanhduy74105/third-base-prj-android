package com.example.orderup.modelview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orderup.lib.tool
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class HomeVIewModel : ViewModel {
    private var _historySearchItem: MutableLiveData<ArrayList<String>> =
        MutableLiveData<ArrayList<String>>()
    val historySearchItem: LiveData<ArrayList<String>>
        get() = _historySearchItem
    init {
        initHistorySearchs()
    }
    constructor()

    fun initHistorySearchs(){
        val uid = tool.getCurrentId()
//        viewModelScope.launch {
//            val currentUser = tool.getFacebookUser() // Lấy thông tin user từ Firebase
//            if (currentUser != null) {
//                // Xử lý dữ liệu của currentUser ở đây
////                Log.i("GGG", " picture: ${currentUser.profileImage}")
//            } else {
//                // Xử lý trường hợp currentUser == null
//            }
//        }
        val ref = FirebaseDatabase.getInstance().getReference("HistorySearchs/$uid")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = ArrayList<String>()
                for (ds in snapshot.children) {
                    val model = ds.value.toString()
                   items.add(model)
                }
                _historySearchItem.postValue(items)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun addHistorySearchs(item: String){
        val timestamp = System.currentTimeMillis()
        val uid = tool.getCurrentId()
        if (item!="") _historySearchItem.value!!.add(item)
        val ref = FirebaseDatabase.getInstance().getReference("HistorySearchs/$uid")
        ref.child("$timestamp")
            .setValue(item)
    }
}