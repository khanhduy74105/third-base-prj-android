package com.example.orderup.restaurantdashboard


import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.orderup.R
import com.example.orderup.databinding.ActivityListFoodDependCategoryBinding
import com.example.orderup.model.ModelFood
import com.example.orderup.rcvAdapter.FoodsAdapter
import com.example.orderup.rcvAdapter.ListFoodDependCategoryAdapter
import com.example.orderup.restaurantdashboard.ui.ManageFoodFragmentFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.properties.Delegates


class ListFoodDependCategory : AppCompatActivity() {
    private lateinit var binding: ActivityListFoodDependCategoryBinding
    private  lateinit var nameCategory: String
    private lateinit var foodsArraylist: ArrayList<ModelFood>
    private lateinit var adapterFood: ListFoodDependCategoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityListFoodDependCategoryBinding.inflate(layoutInflater)
        loadFoods(binding.root)
        val intent = intent
        nameCategory = intent.getStringExtra("categoryName")!!
        binding.button.setOnClickListener{
            val fragment = ManageFoodFragmentFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.listfood, fragment)
                .addToBackStack(null)
                .commit()
        }
        binding.btnback.setOnClickListener {
            onBackPressed()
        }
        setContentView(binding.root)
    }
    private fun loadFoods(container: ViewGroup) {
        foodsArraylist = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Foods")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                foodsArraylist.clear()
                for (ds in snapshot.children) {
                    val model = ds.getValue(ModelFood::class.java)
                    Log.i("nameCategoryModel",model!!.category)
                    if (model != null) {
                        if(model.category == nameCategory){
                            foodsArraylist.add(model!!)
                        }
                        Log.i("HOMEt", model.foodname)

                    }
                }
                binding.rcvFood.layoutManager = GridLayoutManager(container.context, 2)
                adapterFood = ListFoodDependCategoryAdapter(container.context, foodsArraylist)
                binding.rcvFood.adapter = adapterFood
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}




