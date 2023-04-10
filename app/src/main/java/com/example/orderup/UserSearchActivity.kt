package com.example.orderup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.orderup.databinding.ActivityUserSearchBinding
import com.example.orderup.model.ModelFood
import com.example.orderup.rcvAdapter.FoodsAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserSearchBinding
    private lateinit var searchString:String
    private lateinit var foodsArraylist:ArrayList<ModelFood>
    private lateinit var adapterFood:FoodsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchString = intent.getStringExtra("searchValue")!!
        binding.searchEt.setText(searchString)
        loadFoods()
    }


    private fun loadFoods() {
        foodsArraylist = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Foods")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                foodsArraylist.clear()
                for (ds in snapshot.children) {
                    val model = ds.getValue(ModelFood::class.java)
                    foodsArraylist.add(model!!)
                }
                binding.foodsRcv.layoutManager = GridLayoutManager(this@UserSearchActivity, 2)
                adapterFood = FoodsAdapter(this@UserSearchActivity, foodsArraylist)
                binding.foodsRcv.adapter = adapterFood
                adapterFood.filter.filter(searchString)

            }
            override fun onCancelled(error: DatabaseError) {

            }

        })


    }
}