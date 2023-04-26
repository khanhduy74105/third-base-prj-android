package com.example.orderup.modelview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.orderup.lib.tool
import com.example.orderup.model.ModelCartItem
import com.example.orderup.model.ModelFood
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FavoriteViewModel : ViewModel {
    private var _favFoodItemArraylist: MutableLiveData<ArrayList<ModelFood>> =
        MutableLiveData<ArrayList<ModelFood>>()
    val favFoodItemArraylist: LiveData<ArrayList<ModelFood>>
        get() = _favFoodItemArraylist

    init {
        initFavItems()
    }

    private fun initFavItems() {
        val uid = tool.getCurrentId()
        val ref = FirebaseDatabase.getInstance().getReference("Favorites/$uid")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = ArrayList<ModelFood>()
                for (ds in snapshot.children) {
                    val foodId = ds.key.toString()
                    Log.i("FWM", "food id $foodId")
                    val refs = FirebaseDatabase.getInstance().getReference("Foods")
                    refs.child(foodId)
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val model = snapshot.getValue(ModelFood::class.java)
                                if (model != null ){
                                    items.add(model)
                                }
                                _favFoodItemArraylist.value = items

                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    constructor()
}