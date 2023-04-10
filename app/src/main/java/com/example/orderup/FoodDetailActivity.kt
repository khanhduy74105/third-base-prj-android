package com.example.orderup

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.orderup.databinding.ActivityFoodDetailBinding
import com.example.orderup.model.ModelFood
import com.example.orderup.usedashboard.UserDashboardActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FoodDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFoodDetailBinding
    private lateinit var foodId: String
    private var amount = 1
    private lateinit var model: ModelFood
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        foodId = intent.getStringExtra("foodId")!!
        binding.amountFoodTv.text= amount.toString()
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
        binding.minusFoodBtn.setOnClickListener {
            if (amount > 1) amount--
            setAmount()
        }
        binding.plusFoodBtn.setOnClickListener {
            amount++
            setAmount()
        }

        setUI()
        binding.addBtn.setOnClickListener {
            onAddToCart()
        }
    }

    private fun onAddToCart() {
        val timestamp = System.currentTimeMillis()
        val uid = FirebaseAuth.getInstance().uid!!
        val hashMap: HashMap<String, Any?> =  HashMap()
        hashMap["id"]= timestamp
        hashMap["foodname"] = model.foodname
        hashMap["foodId"] = model.id
        hashMap["timestamp"] = timestamp
        hashMap["amount"] = amount
        val ref = FirebaseDatabase.getInstance().getReference("Cart/$uid")
            ref.child(timestamp.toString())
                .setValue(hashMap)
                .addOnSuccessListener {
                    amount = 1
                    setAmount()
                    Toast.makeText(this, "Added ${model.foodname} to your cart!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, UserDashboardActivity::class.java))
                    this.finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed due to ${it.message}", Toast.LENGTH_SHORT).show()
                }
    }

    private fun setAmount() {
        binding.amountFoodTv.text = amount.toString()
    }

    private fun setUI() {
        val ref = FirebaseDatabase.getInstance().getReference("Foods/$foodId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                model = snapshot.getValue(ModelFood::class.java)!!
                binding.foodNameTv.text = model.foodname
                binding.descriptionTv.text = model.description
                binding.priceTv.text = model.price
                val imgUri = model.imageUrl.toUri().buildUpon().scheme("https").build()
                Glide.with(binding.foodIv.context)
                    .load(imgUri)
                    .centerCrop()
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.loading_animation)
                            .error(R.drawable.ic_broken_image))
                    .into(binding.foodIv)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }




}