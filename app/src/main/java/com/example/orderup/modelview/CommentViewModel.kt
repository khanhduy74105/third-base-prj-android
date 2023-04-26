package com.example.orderup.modelview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.orderup.model.ModelCartItem
import com.example.orderup.model.ModelComment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CommentViewModel : ViewModel {
    private var _commentArraylist: MutableLiveData<ArrayList<ModelComment>> =
        MutableLiveData<ArrayList<ModelComment>>()
    val commentArraylist: LiveData<ArrayList<ModelComment>>
        get() = _commentArraylist
    private var _rating: MutableLiveData<Float> =
        MutableLiveData<Float>()
    val rating: LiveData<Float>
        get() = _rating

    constructor()

    fun loadComment(foodId: String) {
        if (foodId != "") {
            val ref = FirebaseDatabase.getInstance().getReference("Comments")
            ref.child(foodId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val items = ArrayList<ModelComment>()
                        for (ds in snapshot.children) {
                            val model = ds.getValue(ModelComment::class.java)
                            items.add(model!!)
                        }
                        _commentArraylist.postValue(items)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
        }
    }

    fun loadRating(foodId: String){
        val ref = FirebaseDatabase.getInstance().getReference("Comments")
        ref.child(foodId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var total:Long = 0
                    var count:Int = 0
                    for (ds in snapshot.children) {
                        val model = ds.getValue(ModelComment::class.java)!!
                        total += model.rating
                        count++
                    }

                    if (count > 0)  _rating.value = (total / count).toFloat()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
}