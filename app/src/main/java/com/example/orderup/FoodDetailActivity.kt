package com.example.orderup

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.orderup.databinding.ActivityFoodDetailBinding
import com.example.orderup.lib.tool
import com.example.orderup.model.ModelFood
import com.example.orderup.modelview.CartViewModel
import com.example.orderup.modelview.CommentViewModel
import com.example.orderup.rcvAdapter.CommentAdapter
import com.example.orderup.usedashboard.CommentActivity
import com.example.orderup.usedashboard.UserDashboardActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FoodDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFoodDetailBinding
    private lateinit var foodId: String
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var commentViewModel: CommentViewModel
    private var amount = 1
    private lateinit var model: ModelFood
    var currentUderId:String = ""
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        foodId = intent.getStringExtra("foodId")!!
        binding.amountFoodTv.text = amount.toString()
        currentUderId = tool.getCurrentId()
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

        commentViewModel = ViewModelProvider(this)[CommentViewModel::class.java]
        loadComments(foodId)
        binding.addBtn.setOnClickListener {

            if (currentUderId!= "") onAddToCart()
        }
        binding.favBtn.setOnClickListener {
            if (currentUderId!= "") {
                changeFavState(it)
            }else{
                Toast.makeText(this, "You need login first!", Toast.LENGTH_SHORT)
            }
        }
        binding.commentTv.setOnClickListener {
            if (currentUderId!= "") {
                val newintent = Intent(this,CommentActivity::class.java)
                newintent.putExtra("foodId", model.id)
                startActivity(newintent)
            }else{
                Toast.makeText(this, "You need login first!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadComments(foodId: String) {
        commentViewModel.loadComment(foodId)
        commentViewModel.loadRating(foodId)
        commentViewModel.commentArraylist.observeForever {
            binding.countRateTv.text = "(${it.size})"
            commentAdapter = CommentAdapter(this, it)
            binding.commentsRcv.adapter = commentAdapter
        }
        commentViewModel.rating.observeForever {
            binding.ratingBar.rating = commentViewModel.rating.value!!
            binding.starRateIv.text = commentViewModel.rating.value!!.toString()
        }
    }

    private fun changeFavState(it: View?) {
        val uid = tool.getCurrentId()
        if (uid != "") {
            it?.isSelected = !it?.isSelected!!
            if (it.isSelected) {
                val ref = FirebaseDatabase.getInstance().getReference("Favorites")
                ref.child(uid)
                    .child(foodId)
                    .setValue(true)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Add to favorites succeed!", Toast.LENGTH_SHORT).show()
                    }
            } else {
                val ref = FirebaseDatabase.getInstance().getReference("Favorites")
                ref.child(uid)
                    .child(foodId)
                    .removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Remove out favorites succeed!", Toast.LENGTH_SHORT)
                            .show()
                    }
            }
        }
    }


    private fun onAddToCart() {
        val timestamp = System.currentTimeMillis()
        val uid = tool.getCurrentId()
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["id"] = "$timestamp"
        hashMap["foodname"] = model.foodname
        hashMap["foodId"] = model.id
        hashMap["timestamp"] = timestamp
        hashMap["amount"] = amount
        hashMap["imageUrl"] = model.imageUrl
        hashMap["price"] = model.price
        val ref = FirebaseDatabase.getInstance().getReference("Cart/$uid")
        ref.child(timestamp.toString())
            .setValue(hashMap)
            .addOnSuccessListener {
                amount = 1
                setAmount()
                Toast.makeText(this, "Added ${model.foodname} to your cart!", Toast.LENGTH_SHORT)
                    .show()
                onBackPressed()
                finish()
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
                binding.foodNameTv.text = model.foodname.capitalize()
                binding.descriptionTv.text = model.description
                binding.priceTv.text = "${model.price} vnd"
                val imgUri = model.imageUrl.toUri().buildUpon().scheme("https").build()
                Glide.with(binding.foodIv.context)
                    .load(imgUri)
                    .centerCrop()
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.loading_animation)
                            .error(R.drawable.ic_broken_image)
                    )
                    .into(binding.foodIv)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        val uid = tool.getCurrentId()
        if (uid != "") {
            val refFav = FirebaseDatabase.getInstance().getReference("Favorites")
            refFav.child(uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (ds in snapshot.children) {
                            if (ds.key.toString() == foodId && ds.value == true) {
                                binding.favBtn.isSelected = true
                                break;
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
        }
    }


}